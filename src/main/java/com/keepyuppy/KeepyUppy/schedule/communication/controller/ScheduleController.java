package com.keepyuppy.KeepyUppy.schedule.communication.controller;

import com.keepyuppy.KeepyUppy.schedule.communication.request.CreateScheduleRequest;
import com.keepyuppy.KeepyUppy.schedule.service.ScheduleService;
import com.keepyuppy.KeepyUppy.security.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping("/")
    public void createUserSchedule(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody CreateScheduleRequest createScheduleRequest) {
        scheduleService.createUserSchedule(userDetails, createScheduleRequest);
    }

    @GetMapping("/")
    public void getUserSchedule(@AuthenticationPrincipal CustomUserDetails userDetails) {
        scheduleService.getUserSchedule(userDetails);
    }

    @PostMapping("/{teamId}")
    public void createTeamSchedule(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long teamId, @RequestBody CreateScheduleRequest createScheduleRequest) {
        scheduleService.createTeamSchedule(userDetails, teamId, createScheduleRequest);
    }
}
