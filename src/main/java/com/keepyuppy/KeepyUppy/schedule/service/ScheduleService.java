package com.keepyuppy.KeepyUppy.schedule.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleJpaRepository scheduleJpaRepository;
}
