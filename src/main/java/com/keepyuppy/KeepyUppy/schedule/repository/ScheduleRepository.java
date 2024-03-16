package com.keepyuppy.KeepyUppy.schedule.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ScheduleRepository {
    private final JPAQueryFactory jpaQueryFactory;
}
