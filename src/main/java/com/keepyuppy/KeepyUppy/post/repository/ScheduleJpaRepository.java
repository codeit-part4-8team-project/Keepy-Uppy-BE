package com.keepyuppy.KeepyUppy.post.repository;

import com.keepyuppy.KeepyUppy.post.domain.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleJpaRepository extends JpaRepository<Schedule,Long>{
}