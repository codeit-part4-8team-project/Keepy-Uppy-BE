package com.keepyuppy.KeepyUppy.schedule.repository;

import com.keepyuppy.KeepyUppy.schedule.domain.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleJpaRepository extends JpaRepository<Schedule,Long> {
}
