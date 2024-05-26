# 프로젝트 설정 및 실행 방법 

### 개발환경
- Java 17, Spring Boot, JPA, Querydsl
- H2 , Maria DB
- Swagger

### 실행 방법

1. 프로젝트 clone
```
git clone https://github.com/youngreal/cpu-monitoring-project.git
```

2. build 후 jar 생성
```
cd cpu-monitoring-project
./gradlew build
```

3. lib 디렉토리에서 jar 실행
```
cd build/libs
java -jar tera-backend-test-0.0.1-SNAPSHOT.jar # 개발
java -jar -Dspring.profiles.active=prod tera-backend-test-0.0.1-SNAPSHOT.jar # 운영
```

만약, prod 환경으로 실행한다면 maria db 접속 후 아래 추가과정 실행 후 jar 실행
```sql
create database tera;
create user 'user'@'localhost' identified by 'password';
grant all on `tera`.* to 'user'@'localhost' with grant option;
flush privileges;
```


## 요구사항 해결 과정

### 데이터 수집 및 저장
- OperatingSystemMXBean와 @Scheduled를 이용해 데이터를 수집했습니다.
- 표기 형식에 제한이 없었기에, 임의의 정수 %로 반환합니다.

*추가 고려사항, 주의점 
- OperatingSystemMXBean는 Oracle에서 만든 Native class를 내부적으로 활용하기때문에 JVM버전에 따라 호환문제가 발생할 수 있습니다.


###  분, 시, 일 단위 API
- 애플리케이션에선 querydsl로 처리하였으며, 각각 아래 쿼리가 발생합니다.
  
분 단위API
```sql
# startDateTime = 2024-05-20T22:20 , endDateTime = 2024-05-26T18:51 입력 시 
SELECT usage_percent, timestamp
FROM cpu_usage c
WHERE c.timestamp BETWEEN '2024-05-20T22:20' AND '2024-05-26T18:51';
```

시 단위API
```sql
# startDateTime = 2024-05-20T22:20 , endDateTime = 2024-05-26T18:51 입력 시 
SELECT MIN(c.usage_percent), MAX(c.usage_percent), AVG(c.usage_percent), HOUR(c.timestamp)
FROM cpu_usage c
WHERE c.timestamp BETWEEN '2024-05-20T22:20' AND '2024-05-26T18:51
GROUP BY HOUR(c.timestamp);
```

일 단위API
```sql
# startDateTime = 2024-05-20T22:20 , endDateTime = 2024-05-26T18:51 입력 시 
SELECT MIN(c.usage_percent), MAX(c.usage_percent), AVG(c.usage_percent), extract(day FROM c.timestamp)
FROM cpu_usage c
WHERE c.timestamp BETWEEN '2024-05-20T22:20' AND '2024-05-26T18:51
GROUP BY extract(day FROM c.timestamp);
```


### 예외 처리 
- @RestControllerAdvice로 전체적인 에러 핸들링 진행 
  - 프론트엔드와 공통 응답 형식이 정해지면 공통 응답 개선 필요

**파라미터 검증 관련**
- 초반에는 파라미터에 사용할 Custom Validation @StartTime, @EndTime을 정의 하여 입력값의 범위를 검증했습니다. [관련 커밋](https://github.com/youngreal/cpu-monitoring-project/commit/156c2fca39e338e0218c3e396accf789e55ee404)
  
- 그러나, 기능이 추가되면서 Controller layer에서는 값의 형식만 검증하는 방식으로 리팩토링했고 **아래의 이유로 해당 검증을 Service layer로 이동**시켰습니다
  - API마다 다른 검증조건들을 가질때마다 어노테이션과 Validator 클래스가 추가된다.
    
  - 중복되는 코드가 있으며 이 **코드를 재사용하기 힘들어진다**.
    
  - Controller에서 처리해도 나름의 장점이있지만, **데이터 제공 기한이 변경된다면 controller layer를 변경하는게 맞을까?** 에 대한 고민을 거친결과 이동하기로 결정
    
- service layer에 존재하는 TimeValidator로 해당 검증내용 이동 
   
   

**데이터 수집 실패 시**

OperatingSystemMXBean이 데이터 수집에 실패하는 경우 -1.0을 반환하는데 해당 경우 로그를 남기고 예외를 던지는 방식으로 해결했습니다.
```java
@Slf4j
@RequiredArgsConstructor
@Component
public class CpuUsageCollector {

	private static final int PERCENTAGE = 100;
	private static final int LOWEST_CPU_USAGE_RATE = 0;
	private final OperatingSystemMXBean operatingSystemMXBean;

	public int usagePercent() {
		final double cpuUsage = operatingSystemMXBean.getCpuLoad();
		if (cpuUsage < LOWEST_CPU_USAGE_RATE) {
			log.error("cpu collection failed");
			throw new CpuUsageCollectFailException();
		}
		return (int)(operatingSystemMXBean.getCpuLoad() * PERCENTAGE);
	}
}
```

추가적으로 JVM 호환이 안되는 경우 문제가 될수있는데 JavaDoc 주석으로 알리거나 try-catch로 RuntimeExcpetion을 잡아서 별도의 로깅을 남기는 식으로 개선해 볼수 있습니다.

**테스트**
- controller layer 통합테스트,  service와 repository layer는 단위 테스트를 작성했습니다.

- controller layer에는 @SpringBootTest로 모든 빈들을 스캔해 테스트를 실행했지만, AutoConfigureMockMvc는 실제 웹환경이 아닌 모킹된 서블릿 서버로 동작하므로, 실제 요청까지 보내보는 .http파일을 작성했습니다.
  
- 시간을 테스트 하기위한 테스트 픽스처에는 날짜가 지정되어있는데 개발 코드에 LocalDateTime.now()를 사용하기에 **현재시간이 지나면 테스트가 깨질수 있는 문제를 Clock을 수동 빈으로 등록해 Mocking 하여 시간이 지나도 테스트가 깨지지 않도록 유지**했습니다.


## 추가로 고민/개선 한 점 
- DateTime에 **인덱스** 설정에 관해
  1. 프로젝트가 1년간 지속된다는 가정하에 분당 하나의 로우가 추가된다면 일년에 525,600개의 로우가 축적 
  2. 이 상황에서 만약 분 단위 API를 호출해 between 쿼리로 특정 날짜의 1주간의 데이터(최대 10080개, 대략 2%)를 호출하게 된다면 50만개의 로우의 full table scan이 발생함
  3. full table scan이 느려지는 로우 수를 체크하여 인덱스를 고려할 필요 있음

- Controller에 침투되는 Swagger 코드를 걷어내기 위해 Swagger인터페이스 적용
  -  Controller의 가독성을 고려하여 도입  [관련 코드](https://github.com/youngreal/cpu-monitoring-project/blob/main/src/main/java/com/example/terabackendtest/controller/swagger/CpuUsageSwagger.java) 

- 응집도와 유지보수성을 고려해 **양방향 패키지 참조를 지양**하고 단방향으로 구현




## API 명세 
서버 실행후, 아래 링크에서 Swagger 확인할 수 있습니다.   
Swagger : http://localhost:8080/swagger-ui/index.html

**분 단위 API 사용률 조회**
GET ``/cpu-usages/minute``
##### Parameters

> | name   |  type     | data type | description                                 |
> |--------|-----------|-----------|---------------------------------------------|
> | `startTime` |  required | date-time    | ISO DateTime format (yyyy-MM-ddTHH:mm:ss) |
> | `endTime`   |  required | date-time    | ISO DateTime format (yyyy-MM-ddTHH:mm:ss) |

##### Response Body
CpuUsageResponse
```json
{
  "usagePercent": 0,
  "timestamp": "2024-05-26T09:38:25.860Z"
}
```

**시 단위 CPU 최소/최대/평균 사용률 조회**
GET ``/cpu-usages/hourly``
##### Parameters

> | name   |  type     | data type | description                   |
> |--------|-----------|-----------|-------------------------------|
> | `date` |  required | date    | ISO Date format (yyyy-MM-dd)  |

##### Response Body
CpuUsagePerHourResponse
```json
{
    "hour": 0,
    "minCpuUsage": 0,
    "maxCpuUsage": 0,
    "averageCpuUsage": 0
}
```

일 단위 CPU 최소/최대/평균 사용률 조회
GET ``/cpu-usages/daily``
##### Parameters

> | name   |  type     | data type | description                   |
> |--------|-----------|-----------|-------------------------------|
> | `startDate` |  required | date    | ISO Date format (yyyy-MM-dd)  |
> | `endDate`   |  required | date    | ISO Date format (yyyy-MM-dd)  |

##### Response Body
CpuUsagePerDailyResponse
```json
{
    "day": 0,
    "minCpuUsage": 0,
    "maxCpuUsage": 0,
    "averageCpuUsage": 0
}
```
