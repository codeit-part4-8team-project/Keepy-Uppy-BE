package com.keepyuppy.KeepyUppy.team.communication.controller;

import com.keepyuppy.KeepyUppy.team.communication.request.CreateTeamRequest;
import com.keepyuppy.KeepyUppy.team.communication.resopnse.TeamByUserIdResponse;
import com.keepyuppy.KeepyUppy.team.communication.resopnse.TeamResponse;
import com.keepyuppy.KeepyUppy.team.service.TeamService;
import com.keepyuppy.KeepyUppy.user.domain.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/user/team/{id}")
    public TeamResponse getTeamById(@PathVariable Long id) {
        return teamService.getTeamById(id);
    }

    @GetMapping("/user/myteam/{userId}")
    public List<TeamByUserIdResponse> getMyTeam(@PathVariable Long userId) {
        return teamService.getTeamByUser(userId);
    }
}
