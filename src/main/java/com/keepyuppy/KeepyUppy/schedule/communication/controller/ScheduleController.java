package com.keepyuppy.KeepyUppy.schedule.communication.controller;

import com.keepyuppy.KeepyUppy.schedule.communication.request.CreateScheduleRequest;
import com.keepyuppy.KeepyUppy.schedule.communication.request.UpdateScheduleRequest;
import com.keepyuppy.KeepyUppy.schedule.communication.response.ScheduleResponse;
import com.keepyuppy.KeepyUppy.schedule.communication.response.TeamScheduleResponse;
import com.keepyuppy.KeepyUppy.schedule.communication.response.UserScheduleResponse;
import com.keepyuppy.KeepyUppy.schedule.domain.entity.Schedule;
import com.keepyuppy.KeepyUppy.schedule.service.ScheduleService;
import com.keepyuppy.KeepyUppy.security.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedule")
@SecurityRequirement(name = "Bearer Authentication")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @Operation(summary = "유저 스케쥴 생성")
    @PostMapping("/")
    public ResponseEntity<UserScheduleResponse> createUserSchedule(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody CreateScheduleRequest createScheduleRequest) {
        return ResponseEntity.ok(scheduleService.createUserSchedule(userDetails, createScheduleRequest));
    }

    @Operation(summary = "Id 로 스케쥴 단일 조회")
    @GetMapping("/{scheduleId}")
    public ResponseEntity<Schedule> getSchedule(@PathVariable Long scheduleId) {
        return ResponseEntity.ok(scheduleService.getScheduleById(scheduleId));
    }

    @Operation(summary = "userId 로 스케쥴 리스트 조회")
    @GetMapping("/{userId}")
    public ResponseEntity<List<UserScheduleResponse>> getUserSchedules(@PathVariable Long userId) {
        return ResponseEntity.ok(scheduleService.getUserSchedule(userId));
    }

    @Operation(summary = "팀 스케쥴 생성")
    @PostMapping("/{teamId}")
    public ResponseEntity<TeamScheduleResponse> createTeamSchedule(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long teamId, @RequestBody CreateScheduleRequest createScheduleRequest) {
        return ResponseEntity.ok(scheduleService.createTeamSchedule(userDetails, teamId, createScheduleRequest));
    }

    @Operation(summary = "teamId 로 스케쥴 리스트 조회")
    @GetMapping("/{teamId}")
    public ResponseEntity<List<TeamScheduleResponse>> getTeamSchedules(@PathVariable Long teamId) {
        return ResponseEntity.ok(scheduleService.getTeamSchedules(teamId));
    }

    @Operation(summary = "스케쥴 수정")
    @PutMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponse> updateSchedule(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long scheduleId, @RequestBody UpdateScheduleRequest updateScheduleRequest) {
        return ResponseEntity.ok(scheduleService.updateSchedule(userDetails, scheduleId, updateScheduleRequest));
    }

    @DeleteMapping("/{scheduleId}")
    public void deleteSchedule(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long scheduleId) {
        scheduleService.deleteSchedule(userDetails,scheduleId);
    }


}
