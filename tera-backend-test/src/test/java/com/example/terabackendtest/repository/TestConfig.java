package com.example.terabackendtest.repository;

import java.lang.management.ManagementFactory;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.terabackendtest.service.CpuUsageCollector;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sun.management.OperatingSystemMXBean;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@TestConfiguration
public class TestConfig {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}