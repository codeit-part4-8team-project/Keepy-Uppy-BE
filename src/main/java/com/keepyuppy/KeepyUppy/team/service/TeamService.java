package com.keepyuppy.KeepyUppy.team.service;

import com.keepyuppy.KeepyUppy.member.domain.entity.Member;
import com.keepyuppy.KeepyUppy.member.domain.enums.Grade;
import com.keepyuppy.KeepyUppy.member.domain.enums.Status;
import com.keepyuppy.KeepyUppy.member.repository.MemberJpaRepository;
import com.keepyuppy.KeepyUppy.member.repository.MemberRepositoryImpl;
import com.keepyuppy.KeepyUppy.security.jwt.CustomUserDetails;
import com.keepyuppy.KeepyUppy.team.communication.request.ChangeTeamOwnerRequest;
import com.keepyuppy.KeepyUppy.team.communication.request.CreateTeamRequest;
import com.keepyuppy.KeepyUppy.team.communication.request.UpdateTeam;
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
    private final MemberJpaRepository memberJpaRepository;
    private final MemberRepositoryImpl memberRepository;


    // 팀 생성
    // 팀을 생성하는 유저 = 소유자
    // 팀을 생성하는 유저는 그팀의 멤버가 된다.
    @Transactional
    public TeamResponse createTeam(CustomUserDetails userDetails, CreateTeamRequest createTeamRequest) {
        Team team = Team.builder()
                .name(createTeamRequest.getName())
                .description(createTeamRequest.getDescription())
                .color(createTeamRequest.getColor())
                .startDate(createTeamRequest.getStartDate())
                .endDate(createTeamRequest.getEndDate())
                .figma(createTeamRequest.getFigmaLink())
                .github(createTeamRequest.getGithubLink())
                .discord(createTeamRequest.getDiscordLink())
                .build();

        Users user = userRepository.findById(userDetails.getUserId()).orElseThrow(IllegalArgumentException::new);

        Member member = new Member(user, team, Grade.OWNER, Status.ACCEPTED);

        team.addMember(member);

        team.setOwnerId(member.getId());

        teamJpaRepository.save(team);

        memberJpaRepository.save(member);

        return TeamResponse.of(team);
    }

    public List<TeamResponse> getTeamByUser(CustomUserDetails userDetails) {

        return teamRepository.findTeamByUsersId(userDetails.getUserId()).stream().map(TeamResponse::of).toList();
    }

    // 팀을 삭제한다.
    // 팀 삭제를 신청한 유저의 정보가 팀 소유자 일경우만 실행.
    @Transactional
    public boolean removeTeam(CustomUserDetails userDetails, Long teamId) {
        Team team = teamJpaRepository.findById(teamId).orElseThrow(() -> new IllegalArgumentException(teamId + " 는 없는 아이디 입니다."));
        // todo
        // 뭔가 개선할수 있을거같음
        Member teamOwner = team.getMembers().stream().filter(member -> member.getGrade().equals(Grade.OWNER)).findFirst().orElseThrow(() -> new IllegalStateException("팀 소유자가 존재하지 않습니다."));
        if (teamOwner.getUser().getId().equals(userDetails.getUserId())) {
            teamJpaRepository.delete(team);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean updateTeam(CustomUserDetails userDetails, Long teamId, UpdateTeam updateTeam) {
        Team team = teamJpaRepository.findById(teamId).orElseThrow(() -> new IllegalArgumentException(teamId + " 는 없는 아이디 입니다."));

        Member teamOwner = team.getMembers().stream().filter(member -> member.getGrade().equals(Grade.OWNER)).findFirst().orElseThrow(() -> new IllegalStateException("팀 소유자가 존재하지 않습니다."));

        if (teamOwner.getUser().getId().equals(userDetails.getUserId())) {
            team.update(updateTeam);
            return true;
        }
        return false;
    }

    public List<TeamResponse> getPendingTeams(CustomUserDetails userDetails) {
        return teamRepository.findInvitedTeamByUsersId(userDetails.getUserId()).stream().map(TeamResponse::of).toList();
    }

    @Transactional
    public boolean changeTeamOwner(CustomUserDetails userDetails, Long teamId, ChangeTeamOwnerRequest changeTeamOwnerRequest) {
        Team team = teamJpaRepository.findById(teamId).orElseThrow(() -> new IllegalArgumentException(teamId + " 를 찾을수 없습니다."));
        Member beforeOwner = memberRepository.findMemberInTeamByUserId(userDetails.getUserId(), teamId).orElseThrow(() -> new IllegalArgumentException(userDetails.getUserId() + " 를 찾을수 없습니다."));
        Member afterOwner = memberJpaRepository.findById(changeTeamOwnerRequest.getNewOwnerId()).orElseThrow(() -> new IllegalArgumentException(changeTeamOwnerRequest.getNewOwnerId() + " 를 찾을수 없습니다."));

        if (beforeOwner.getGrade().equals(Grade.OWNER)) {
            afterOwner.setGrade(Grade.OWNER);
            beforeOwner.setGrade(Grade.TEAM_MEMBER);
            team.setOwnerId(afterOwner.getId());

            return true;
        }

        return false;
    }
}

