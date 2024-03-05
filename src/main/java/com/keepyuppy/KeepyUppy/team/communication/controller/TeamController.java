package com.keepyuppy.KeepyUppy.team.communication.controller;

import com.keepyuppy.KeepyUppy.team.communication.request.CreateTeamRequest;
import com.keepyuppy.KeepyUppy.team.communication.resopnse.TeamByUserIdResponse;
import com.keepyuppy.KeepyUppy.team.communication.resopnse.TeamResponse;
import com.keepyuppy.KeepyUppy.team.service.TeamService;
import com.keepyuppy.KeepyUppy.user.domain.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    // @AuthenticationPrincipal UserDetails userDetails 로 변경
    @PostMapping("/user/team/create")
    public Long createTeam(Users users,
                           @RequestBody CreateTeamRequest createTeamRequest) {
        return teamService.createTeam(users, createTeamRequest);
    }

    // @AuthenticationPrincipal UserDetails userDetails 로 변경
    @GetMapping("/user/team")
    public TeamResponse getTeamById(Long teamId) {
        return teamService.getTeamById(teamId);
    }

    @GetMapping("/user/myteam")
    public List<TeamByUserIdResponse> getMyTeam(@AuthenticationPrincipal UserDetails userDetails) {
        return teamService.getTeamByUser(userDetails);
    }
}
