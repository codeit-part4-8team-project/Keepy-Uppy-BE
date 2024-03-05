package com.keepyuppy.KeepyUppy.team.service;

import com.keepyuppy.KeepyUppy.team.communication.request.CreateTeamRequest;
import com.keepyuppy.KeepyUppy.team.communication.resopnse.TeamByUserIdResponse;
import com.keepyuppy.KeepyUppy.team.communication.resopnse.TeamResponse;
import com.keepyuppy.KeepyUppy.team.domain.entity.Team;
import com.keepyuppy.KeepyUppy.team.repository.TeamJpaRepository;
import com.keepyuppy.KeepyUppy.team.repository.TeamRepositoryImpl;
import com.keepyuppy.KeepyUppy.user.domain.entity.Users;
import com.keepyuppy.KeepyUppy.user.domain.enums.Provider;
import com.keepyuppy.KeepyUppy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamJpaRepository teamJpaRepository;
    private final UserRepository userRepository;
    private final TeamRepositoryImpl teamRepositoryImpl;

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

    public List<TeamByUserIdResponse> getTeamByUser(UserDetails userDetails) {
        // 유저 가져오기.
        Users users = new Users("test", "null", "123123", Provider.GOOGLE);

        return teamRepositoryImpl.findTeamByUsersId(users.getId()).stream().map(TeamByUserIdResponse::new).toList();
    }
}
