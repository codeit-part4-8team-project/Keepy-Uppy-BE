package com.keepyuppy.KeepyUppy.content.repository;

import com.keepyuppy.KeepyUppy.content.domain.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleJpaRepository extends JpaRepository<Schedule,Long>{
}