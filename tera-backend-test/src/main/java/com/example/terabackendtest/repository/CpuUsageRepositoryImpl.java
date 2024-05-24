package com.example.terabackendtest.repository;

import static com.example.terabackendtest.domain.QCpuUsage.cpuUsage;

import java.time.LocalDateTime;
import java.util.List;

import com.example.terabackendtest.dto.CpuDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CpuUsageRepositoryImpl implements CpuUsageRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<CpuDto> findCpuUsageBetween(final LocalDateTime startTime, final LocalDateTime endTime) {
		return jpaQueryFactory.select(Projections.constructor(CpuDto.class,
				cpuUsage.usagePercent, cpuUsage.timestamp))
			.from(cpuUsage)
			.where(cpuUsage.timestamp.between(startTime, endTime))
			.fetch();
	}
}
