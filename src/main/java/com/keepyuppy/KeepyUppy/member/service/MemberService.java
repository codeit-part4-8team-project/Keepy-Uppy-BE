package com.keepyuppy.KeepyUppy.member.service;

import com.keepyuppy.KeepyUppy.global.exception.AccessDeniedException;
import com.keepyuppy.KeepyUppy.global.exception.NotFoundException;
import com.keepyuppy.KeepyUppy.member.communication.request.AddMemberRequest;
import com.keepyuppy.KeepyUppy.member.communication.request.RemoveMemberRequest;
import com.keepyuppy.KeepyUppy.member.communication.request.UpdateMemberRequest;
import com.keepyuppy.KeepyUppy.member.communication.response.MemberResponse;
import com.keepyuppy.KeepyUppy.member.domain.entity.Member;
import com.keepyuppy.KeepyUppy.member.domain.enums.Grade;
import com.keepyuppy.KeepyUppy.member.domain.enums.Status;
import com.keepyuppy.KeepyUppy.member.repository.MemberJpaRepository;
import com.keepyuppy.KeepyUppy.member.repository.MemberRepositoryImpl;
import com.keepyuppy.KeepyUppy.security.jwt.CustomUserDetails;
import com.keepyuppy.KeepyUppy.team.domain.entity.Team;
import com.keepyuppy.KeepyUppy.team.repository.TeamJpaRepository;
import com.keepyuppy.KeepyUppy.user.domain.entity.Users;
import com.keepyuppy.KeepyUppy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final MemberJpaRepository memberJpaRepository;
    private final TeamJpaRepository teamJpaRepository;
    private final UserRepository userRepository;
    private final MemberRepositoryImpl memberRepository;

    public List<MemberResponse> getMembers(Long teamId) {
        return memberRepository.findMembersByTeamId(teamId).orElseThrow(NotFoundException.MemberNotFoundException::new).stream().map(MemberResponse::new).toList();

    }

    @Transactional
    public boolean addMember(CustomUserDetails userDetails, Long teamId, AddMemberRequest addMemberRequest) {

        Member loginMember = findMemberInTeamByUserId(userDetails.getUserId(), teamId);

        if (isManagerOrOwner(loginMember)) {

            Team team = findTeamById(teamId);

            Users user = findUserById(userDetails.getUserId());

            if (alreadyMemberInTeam(addMemberRequest.getUserName(), teamId)) {
                throw new IllegalArgumentException("이미 팀에 속해있는 멤버입니다.");
            }

            Member member = new Member(user, team, Grade.TEAM_MEMBER, Status.PENDING);

            team.addMember(member);
            user.addMember(member);
            memberJpaRepository.save(member);
            return true;
        } else {
            throw new AccessDeniedException.ActionAccessDeniedException();
        }
    }

    @Transactional
    public boolean removeMember(CustomUserDetails userDetails, Long teamId, RemoveMemberRequest removeMemberRequest) {

        Member loginMember = findMemberInTeamByUserId(userDetails.getUserId(), teamId);

        Team team = findTeamById(teamId);

        Member member = findMemberInTeamByUserName(removeMemberRequest.getMemberName(), teamId);

        if (member.canUpdate(loginMember)) {
            memberJpaRepository.delete(member);
            team.removeMember(member);
            return true;
        } else {
            throw new RuntimeException("팀 멤버 삭제에 실패하였습니다");
        }
    }

    @Transactional
    public void accept(@AuthenticationPrincipal CustomUserDetails userDetails, Long teamId) {
        Member member = findPendingByUserId(userDetails.getUserId(), teamId);
        member.setStatus(Status.ACCEPTED);
    }

    @Transactional
    public void reject(@AuthenticationPrincipal CustomUserDetails userDetails, Long teamId) {
        Member member = findPendingByUserId(userDetails.getUserId(), teamId);
        member.getTeam().removeMember(member);
        memberJpaRepository.delete(member);
    }


    private boolean isManagerOrOwner(Member member) {

        return member.getGrade().equals(Grade.OWNER) || member.getGrade().equals(Grade.MANAGER);
    }

    @Transactional
    public boolean updateMember(CustomUserDetails customUserDetails,Long memberId, UpdateMemberRequest updateMemberRequest) {

        // 수정될 member
        Member member = memberJpaRepository.findById(memberId).orElseThrow(NotFoundException.MemberNotFoundException::new);

        // 수정하는 사람
        Member updater = memberRepository.findByUserId(customUserDetails.getUserId()).orElseThrow(NotFoundException.MemberNotFoundException::new);

        return member.update(updater, updateMemberRequest);
    }

    private Member findMemberInTeamByUserId(Long userId, Long teamId) {
        return memberRepository.findMemberInTeamByUserId(userId, teamId).orElseThrow(NotFoundException.MemberNotFoundException::new);
    }

    private Team findTeamById(Long teamId) {
        return teamJpaRepository.findById(teamId).orElseThrow(NotFoundException.TeamNotFoundException::new);
    }

    private Users findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(NotFoundException.UserNotFoundException::new);
    }

    private boolean alreadyMemberInTeam(String userName,Long teamId) {
        return (memberRepository.findMemberInTeamByUserName(userName, teamId).isPresent());
    }

    private Member findMemberInTeamByUserName(String userName, Long teamId) {
        return memberRepository.findMemberInTeamByUserName(userName, teamId).orElseThrow(NotFoundException.MemberNotFoundException::new);
    }

    private Member findPendingByUserId(Long userId, Long teamId) {
        return memberRepository.findPendingByUserId(userId, teamId).orElseThrow(NotFoundException.MemberNotFoundException::new);
    }
}



