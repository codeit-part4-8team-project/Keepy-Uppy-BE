package com.keepyuppy.KeepyUppy.team.service;

import com.keepyuppy.KeepyUppy.team.communication.request.CreateTeamRequest;
import com.keepyuppy.KeepyUppy.team.communication.resopnse.TeamResponse;
import com.keepyuppy.KeepyUppy.team.domain.entity.Team;
import com.keepyuppy.KeepyUppy.team.repository.TeamJpaRepository;
import com.keepyuppy.KeepyUppy.user.domain.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamJpaRepository teamJpaRepository;

    public Long createTeam(Users users, CreateTeamRequest createTeamRequest) {

        // todo
        // UserDetails 구현방식에 따라 변경 예정.

        Team team = Team.builder()
                .name(createTeamRequest.getName())
                .description(createTeamRequest.getDescription())
                .startDate(createTeamRequest.getStartDate())
                .endDate(createTeamRequest.getEndDate())
                .figma(createTeamRequest.getFigmaLink())
                .github(createTeamRequest.getGithubLink())
                .discord(createTeamRequest.getDiscordLink())
                .build();

        // todo
        // team.addMember(소유자);

        teamJpaRepository.save(team);

        return team.getId();
    }

    // todo
    // @Authentication 으로 가져온 UserDetails 로 변경예정.
    public TeamResponse getTeamById(Long teamId) {
        Team team = teamJpaRepository.findById(teamId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 teamId 입니다."));

        return new TeamResponse(team);
    }
}
