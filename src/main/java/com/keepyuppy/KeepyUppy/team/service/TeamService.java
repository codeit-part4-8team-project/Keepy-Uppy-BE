package com.keepyuppy.KeepyUppy.team.service;

import com.keepyuppy.KeepyUppy.global.exception.CustomException;
import com.keepyuppy.KeepyUppy.global.exception.ExceptionType;
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

        Users user = userRepository.findById(userDetails.getUserId()).orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_FOUND));

        Member member = new Member(user, team, Grade.OWNER, Status.ACCEPTED);

        team.addMember(member);

        team.setOwnerId(member.getId());

        if (createTeamRequest.getMembers() != null) {
            createTeamRequest.getMembers().forEach(memberName -> {
                Member addMember = new Member(userRepository.findByUsername(memberName).orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_FOUND)), team, Grade.TEAM_MEMBER, Status.PENDING);
                memberJpaRepository.save(addMember);
                team.addMember(addMember);
            });
        }


        teamJpaRepository.save(team);

        memberJpaRepository.save(member);

        return TeamResponse.of(team);
    }

    @Transactional
    public List<TeamResponse> getTeamByUser(CustomUserDetails userDetails) {

        return teamRepository.findTeamByUsersId(userDetails.getUserId()).stream().map(TeamResponse::of).toList();
    }

    @Transactional
    public boolean removeTeam(CustomUserDetails userDetails, Long teamId) {
        Team team = getTeamById(teamId);

        Member teamOwner = getTeamOwner(team);
        if (teamOwner.getUser().getId().equals(userDetails.getUserId())) {
            teamJpaRepository.delete(team);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean updateTeam(CustomUserDetails userDetails, Long teamId, UpdateTeam updateTeam) {
        Team team = teamJpaRepository.findById(teamId).orElseThrow(() -> new CustomException(ExceptionType.TEAM_NOT_FOUND));

        Member teamOwner = getTeamOwner(team);

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
        Team team = teamJpaRepository.findById(teamId).orElseThrow(() -> new CustomException(ExceptionType.TEAM_NOT_FOUND));
        Member beforeOwner = getMemberByUsernameAndTeamId(userDetails.getUsername(), teamId);
        Member afterOwner = getMemberByUsernameAndTeamId(changeTeamOwnerRequest.getUsername(), teamId);

        if (beforeOwner.getGrade().equals(Grade.OWNER)) {
            afterOwner.setGrade(Grade.OWNER);
            beforeOwner.setGrade(Grade.TEAM_MEMBER);
            team.setOwnerId(afterOwner.getId());

            return true;
        }

        return false;
    }

    private Member getMemberByUsernameAndTeamId(String username, Long teamId) {
        return memberRepository.findMemberInTeamByUsername(username, teamId).orElseThrow(() -> new CustomException(ExceptionType.MEMBER_NOT_FOUND));
    }

    private Team getTeamById(Long teamId) {
        return teamJpaRepository.findById(teamId).orElseThrow(() -> new CustomException(ExceptionType.TEAM_NOT_FOUND));
    }

    private Member getTeamOwner(Team team) {
        return team.getMembers().stream().filter(member -> member.getGrade().equals(Grade.OWNER)).findFirst().orElseThrow(() -> new CustomException(ExceptionType.MEMBER_NOT_FOUND));
    }
}



