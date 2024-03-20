package com.keepyuppy.KeepyUppy.schedule.communication.controller;

import com.keepyuppy.KeepyUppy.schedule.communication.request.CreateScheduleRequest;
import com.keepyuppy.KeepyUppy.schedule.communication.request.UpdateScheduleRequest;
import com.keepyuppy.KeepyUppy.schedule.communication.response.ScheduleResponse;
import com.keepyuppy.KeepyUppy.schedule.communication.response.TeamScheduleResponse;
import com.keepyuppy.KeepyUppy.schedule.communication.response.UserScheduleResponse;
import com.keepyuppy.KeepyUppy.schedule.service.ScheduleService;
import com.keepyuppy.KeepyUppy.security.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedule")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "ScheduleController",description = "Schedule 관련 컨트롤러 입니다.")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @Operation(summary = "유저 스케쥴 생성")
    @PostMapping("/user")
    public ResponseEntity<UserScheduleResponse> createUserSchedule(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody CreateScheduleRequest createScheduleRequest) {
        return ResponseEntity.ok(scheduleService.createUserSchedule(userDetails, createScheduleRequest));
    }

    @Operation(summary = "Id 로 스케쥴 단일 조회")
    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponse> getSchedule(@PathVariable Long scheduleId) {
        return ResponseEntity.ok(scheduleService.getScheduleById(scheduleId));
    }

    @Operation(summary = "userId 로 주단위 스케쥴 리스트 조회")
    @GetMapping("/user/week/{userId}")
    public ResponseEntity<List<UserScheduleResponse>> getUserSchedulesInWeek(@PathVariable Long userId, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime localDateTime) {
        return ResponseEntity.ok(scheduleService.getUserScheduleInWeek(userId, localDateTime));
    }

    @Operation(summary = "userId 로 월단위 스케쥴 리스트 조회")
    @GetMapping("/user/month/{userId}")
    public ResponseEntity<List<UserScheduleResponse>> getUserSchedulesInMonth(@PathVariable Long userId, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime localDateTime) {
        return ResponseEntity.ok(scheduleService.getUserScheduleInMonth(userId, localDateTime));
    }

    @Operation(summary = "팀 스케쥴 생성")
    @PostMapping("/team/{teamId}")
    public ResponseEntity<TeamScheduleResponse> createTeamSchedule(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long teamId, @RequestBody CreateScheduleRequest createScheduleRequest) {
        return ResponseEntity.ok(scheduleService.createTeamSchedule(userDetails, teamId, createScheduleRequest));
    }

    @Operation(summary = "teamId 로 주단위 스케쥴 리스트 조회")
    @GetMapping("/team/week/{teamId}")
    public ResponseEntity<List<TeamScheduleResponse>> getTeamSchedulesInWeek(@PathVariable Long teamId,@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime localDateTime) {
        return ResponseEntity.ok(scheduleService.getTeamSchedulesInweek(teamId,localDateTime));
    }

    @Operation(summary = "teamId 로 월단위 스케쥴 리스트 조회")
    @GetMapping("/team/month/{teamId}")
    public ResponseEntity<List<TeamScheduleResponse>> getTeamSchedulesInMonth(@PathVariable Long teamId,@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime localDateTime) {
        return ResponseEntity.ok(scheduleService.getTeamSchedulesInMonth(teamId,localDateTime));
    }

    @Operation(summary = "스케쥴 수정")
    @PutMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponse> updateSchedule(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long scheduleId, @RequestBody UpdateScheduleRequest updateScheduleRequest) {
        return ResponseEntity.ok(scheduleService.updateSchedule(userDetails, scheduleId, updateScheduleRequest));
    }

    @Operation(summary = "스케쥴 삭제")
    @DeleteMapping("/{scheduleId}")
    public void deleteSchedule(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long scheduleId) {
        scheduleService.deleteSchedule(userDetails, scheduleId);
    }
}
