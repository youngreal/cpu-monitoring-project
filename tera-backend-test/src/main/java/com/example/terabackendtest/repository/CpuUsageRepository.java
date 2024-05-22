package com.example.terabackendtest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.terabackendtest.domain.CpuUsage;

public interface CpuUsageRepository extends JpaRepository<CpuUsage, Long> {
}
