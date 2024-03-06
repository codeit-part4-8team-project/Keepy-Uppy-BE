package com.keepyuppy.KeepyUppy.organization.communication.controller;

import com.keepyuppy.KeepyUppy.organization.communication.request.CreateTeamRequest;
import com.keepyuppy.KeepyUppy.organization.communication.request.UpdateTeamLinks;
import com.keepyuppy.KeepyUppy.organization.communication.response.TeamByUserIdResponse;
import com.keepyuppy.KeepyUppy.organization.communication.response.TeamResponse;
import com.keepyuppy.KeepyUppy.organization.service.OrganizationService;
import com.keepyuppy.KeepyUppy.user.domain.entity.Users;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/group")
@Tag(name = "OrganizationController",description = "Organization (팀 , 스터디) 컨트롤러 입니다.")
public class OrganizationController {
    private final OrganizationService organizationService;

    // @AuthenticationPrincipal UserDetails userDetails 로 변경
    @PostMapping("/create")
    @Operation(summary = "프로젝트 팀 생성")
    public Long createTeam(Users user,
                           @RequestBody CreateTeamRequest createTeamRequest) {
        return organizationService.createTeam(user, createTeamRequest);
    }

    // @AuthenticationPrincipal UserDetails userDetails 로 변경
    @Operation(summary = "프로젝트 팀 조회")
    @GetMapping("/{id}")
    public TeamResponse getTeamById(@PathVariable Long id) {
        return organizationService.getTeamById(id);
    }

    @Operation(summary = "로그인한 유저가 속한 팀 조회")
    @GetMapping("/myteam/{userId}")
    public List<TeamByUserIdResponse> getMyTeam(@PathVariable Long userId) {
        return organizationService.getTeamByUser(userId);
    }

    @Operation(summary = "팀 소유자 일경우 팀 삭제")
    @PostMapping("/remove/{teamId}")
    public boolean removeTeam(@AuthenticationPrincipal Users user, @PathVariable Long teamId) {
        return organizationService.removeTeam(user, teamId);
    }

    @Operation(summary = "팀 링크 업데이트")
    @PostMapping("/link/update/{teamId}")
    public void updateLinks(@PathVariable Long teamId , @RequestBody UpdateTeamLinks updateTeamLinks) {
        organizationService.updateLinks(teamId,updateTeamLinks);
    }

    @Operation(summary = "로그인한 유저가 초대받은 응답 대기중인 팀 목록 조회")
    @GetMapping("/team/pending")
    public List<TeamByUserIdResponse> getPendingTeams(Users user) {
        return organizationService.getPendingTeams(user.getId());
    }
}