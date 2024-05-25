package com.example.terabackendtest.repository;

import static com.example.terabackendtest.domain.QCpuUsage.cpuUsage;

import java.time.LocalDateTime;
import java.util.List;

import com.example.terabackendtest.repository.dto.CpuDtoForPerHour;
import com.example.terabackendtest.repository.dto.CpuDtoForPerMinute;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CpuUsageRepositoryImpl implements CpuUsageRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<CpuDtoForPerMinute> findCpuUsageBetween(final LocalDateTime startTime, final LocalDateTime endTime) {
		return jpaQueryFactory.select(Projections.constructor(CpuDtoForPerMinute.class,
				cpuUsage.usagePercent, cpuUsage.timestamp))
			.from(cpuUsage)
			.where(cpuUsage.timestamp.between(startTime, endTime))
			.fetch();
	}

	@Override
	public List<CpuDtoForPerHour> findCpuUsagesForDay(final LocalDateTime startDate, final LocalDateTime endDate) {
		return jpaQueryFactory.select(Projections.constructor(CpuDtoForPerHour.class,
				cpuUsage.usagePercent.min(), cpuUsage.usagePercent.max(), cpuUsage.usagePercent.avg(),
				cpuUsage.timestamp.hour()))
			.from(cpuUsage)
			.where(cpuUsage.timestamp.between(startDate, endDate))
			.groupBy(cpuUsage.timestamp.hour())
			.fetch();
	}
}
