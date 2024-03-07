package com.keepyuppy.KeepyUppy.team.communication.controller;

import com.keepyuppy.KeepyUppy.team.communication.request.CreateTeamRequest;
import com.keepyuppy.KeepyUppy.team.communication.request.UpdateTeamLinks;
import com.keepyuppy.KeepyUppy.team.communication.response.TeamByUserIdResponse;
import com.keepyuppy.KeepyUppy.team.communication.response.TeamResponse;
import com.keepyuppy.KeepyUppy.team.service.TeamService;
import com.keepyuppy.KeepyUppy.user.domain.entity.Users;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/team")
@Tag(name = "TeamController",description = "Team (팀 , 스터디) 컨트롤러 입니다.")
public class TeamController {
    private final TeamService teamService;

    // @AuthenticationPrincipal UserDetails userDetails 로 변경
    @PostMapping("/")
    @Operation(summary = "팀 생성")
    public Long createTeam(Users user,
                           @RequestBody CreateTeamRequest createTeamRequest) {
        return teamService.createTeam(user, createTeamRequest);
    }

    // @AuthenticationPrincipal UserDetails userDetails 로 변경
    @Operation(summary = "팀 조회")
    @GetMapping("/{id}")
    public TeamResponse getTeamById(@PathVariable Long id) {
        return teamService.getTeamById(id);
    }

    // todo
    // /my-teams 로 변경
    @Operation(summary = "로그인한 유저가 속한 팀 조회")
    @GetMapping("/team/{userId}")
    public List<TeamByUserIdResponse> getMyTeam(@PathVariable Long userId) {
        return teamService.getTeamByUser(userId);
    }

    @Operation(summary = "팀 소유자 일경우 팀 삭제")
    @DeleteMapping("/{teamId}")
    public boolean removeTeam(@AuthenticationPrincipal Users user, @PathVariable Long teamId) {
        return teamService.removeTeam(user, teamId);
    }

    @Operation(summary = "팀 링크 업데이트")
    @PutMapping("/{teamId}/link")
    public void updateLinks(@PathVariable Long teamId , @RequestBody UpdateTeamLinks updateTeamLinks) {
        teamService.updateLinks(teamId,updateTeamLinks);
    }

    @Operation(summary = "로그인한 유저가 초대받은 응답 대기중인 팀 목록 조회")
    @GetMapping("/pending")
    public List<TeamByUserIdResponse> getPendingTeams(Users user) {
        return teamService.getPendingTeams(user.getId());
    }
}