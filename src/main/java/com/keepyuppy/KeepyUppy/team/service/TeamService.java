package com.keepyuppy.KeepyUppy.team.service;

import com.keepyuppy.KeepyUppy.member.domain.entity.Member;
import com.keepyuppy.KeepyUppy.member.domain.enums.Grade;
import com.keepyuppy.KeepyUppy.member.domain.enums.Status;
import com.keepyuppy.KeepyUppy.team.communication.request.CreateTeamRequest;
import com.keepyuppy.KeepyUppy.team.communication.request.UpdateTeamLinks;
import com.keepyuppy.KeepyUppy.team.communication.response.TeamByUserIdResponse;
import com.keepyuppy.KeepyUppy.team.communication.response.TeamResponse;
import com.keepyuppy.KeepyUppy.team.domain.entity.Team;
import com.keepyuppy.KeepyUppy.team.repository.TeamJpaRepository;
import com.keepyuppy.KeepyUppy.team.repository.TeamRepositoryImpl;
import com.keepyuppy.KeepyUppy.user.domain.entity.Users;
import com.keepyuppy.KeepyUppy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamJpaRepository teamJpaRepository;
    private final UserRepository userRepository;
    private final TeamRepositoryImpl teamRepository;

    // 팀 생성
    // 팀을 생성하는 유저 = 소유자
    // 팀을 생성하는 유저는 그팀의 멤버가 된다.
    @Transactional
    public Long createTeam(Users user, CreateTeamRequest createTeamRequest) {

        // todo
        // UserDetails 구현방식에 따라 변경 예정.

        Team team = Team.builder()
                .type(createTeamRequest.getType())
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

        new Member(user, team, Grade.OWNER, createTeamRequest.getRole() ,Status.ACCEPTED);

        return team.getId();
    }

    // todo
    // @Authentication 으로 가져온 UserDetails 로 변경예정.
    // teamId 로 팀을 조회한다.
    // 이때 멤버는 Status ACCEPTED 인 멤버만. (초대에 수락한 멤버만)
    public TeamResponse getTeamById(Long teamId) {
        Team team = teamJpaRepository.findById(teamId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 teamId 입니다."));

        return new TeamResponse(team);
    }

    // todo
    // @Authentication 으로 가져온 UserDetails 로 변경예정.
    // userId 로 유저가 속한 팀을 조회한다.
    // 이때 조회되는 팀은 초대에 수락한 팀원.
    public List<TeamByUserIdResponse> getTeamByUser(Long userId) {

        return teamRepository.findProjectByUsersId(userId).stream().map(TeamByUserIdResponse::new).toList();
    }

    // 팀을 삭제한다.
    // 팀 삭제를 신청한 유저의 정보가 팀 소유자 일경우만 실행.
    @Transactional
    public boolean removeTeam(Users user, Long teamId) {
        return false;
    }

    @Transactional
    public void updateLinks(Long teamId, UpdateTeamLinks updateTeamLinks) {

    }

    // todo
    // @Authentication
    // 초대받은 팀 목록 조회
    public List<TeamByUserIdResponse> getPendingTeams(Long userId) {
        return teamRepository.findInvitedTeamByUsersId(userId).stream().map(TeamByUserIdResponse::new).toList();
    }
}
