package com.keepyuppy.KeepyUppy.team.communication.controller;

import com.keepyuppy.KeepyUppy.security.jwt.CustomUserDetails;
import com.keepyuppy.KeepyUppy.team.communication.request.ChangeTeamOwnerRequest;
import com.keepyuppy.KeepyUppy.team.communication.request.CreateTeamRequest;
import com.keepyuppy.KeepyUppy.team.communication.request.UpdateTeamRequest;
import com.keepyuppy.KeepyUppy.team.communication.response.TeamResponse;
import com.keepyuppy.KeepyUppy.team.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/team")
@Tag(name = "TeamController", description = "Team 관련 컨트롤러 입니다.")
@SecurityRequirement(name = "Bearer Authentication")
public class TeamController {
    private final TeamService teamService;

    @Operation(summary = "팀 생성")
    @PostMapping("/")
    public ResponseEntity<TeamResponse> createTeam(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody CreateTeamRequest createTeamRequest) {
        return ResponseEntity.ok(teamService.createTeam(userDetails, createTeamRequest));
    }

    @Operation(summary = "팀 Id 로 팀 조회")
    @GetMapping("/{teamId}")
    public ResponseEntity<TeamResponse> getTeamById(@PathVariable Long teamId) {
        return ResponseEntity.ok(teamService.getTeam(teamId));
    }

    @Operation(summary = "팀 소유자 변경")
    @PutMapping("/owner/{teamId}")
    public ResponseEntity<Boolean> changeOwner(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long teamId, @RequestBody ChangeTeamOwnerRequest changeTeamOwnerRequest) {
        return ResponseEntity.ok(teamService.changeTeamOwner(userDetails, teamId, changeTeamOwnerRequest));
    }

    @Operation(summary = "로그인한 유저가 속한 팀 조회")
    @GetMapping("/my-team")
    public ResponseEntity<List<TeamResponse>> getMyTeam(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(teamService.getTeamByUser(userDetails));
    }

    @Operation(summary = "팀 소유자 일경우 팀 삭제")
    @DeleteMapping("/{teamId}")
    public ResponseEntity<Boolean> removeTeam(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long teamId) {
        return ResponseEntity.ok(teamService.removeTeam(userDetails, teamId));
    }

    @Operation(summary = "팀 소유자 일경우 정보 변경")
    @PutMapping("/{teamId}")
    public ResponseEntity<Boolean> updateLinks(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long teamId, @RequestBody UpdateTeamRequest updateTeamRequest) {
        return ResponseEntity.ok(teamService.updateTeam(userDetails, teamId, updateTeamRequest));
    }

    @Operation(summary = "로그인한 유저가 초대받은 응답 대기중인 팀 목록 조회")
    @GetMapping("/pending")
    public ResponseEntity<List<TeamResponse>> getPendingTeams(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(teamService.getPendingTeams(userDetails));
    }
}

