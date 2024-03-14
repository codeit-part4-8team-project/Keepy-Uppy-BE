package com.keepyuppy.KeepyUppy.member.service;

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
        return memberRepository.findMembersByTeamId(teamId).orElseThrow(() -> new IllegalStateException("팀에 속한 멤버가 존재하지 않습니다.")).stream().map(MemberResponse::new).toList();

    }

    @Transactional
    public boolean addMember(CustomUserDetails userDetails, Long teamId, AddMemberRequest addMemberRequest) {

        Member loginMember = memberRepository.findMemberInTeamByUserId(userDetails.getUserId(), teamId).orElseThrow(IllegalArgumentException::new);

        if (isManagerOrOwner(loginMember)) {

            Team team = teamJpaRepository.findById(teamId)
                    .orElseThrow(() -> new IllegalArgumentException(teamId + " 는 존재하지 않는 팀 아이디 입니다."));

            Users user = userRepository.findByUsername(addMemberRequest.getUserName())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

            if (memberRepository.findMemberInTeamByUserName(addMemberRequest.getUserName(), teamId).isPresent()) {
                throw new IllegalArgumentException("이미 팀에 포함된 회원입니다.");
            }

            Member member = new Member(user, team, Grade.TEAM_MEMBER, Status.PENDING);

            team.addMember(member);
            user.addMember(member);
            memberJpaRepository.save(member);
            return true;
        } else {
            throw new IllegalStateException("팀원 초대는 매니저 이상의 권한이 필요합니다.");
        }
    }

    @Transactional
    public boolean removeMember(CustomUserDetails userDetails, Long teamId, RemoveMemberRequest removeMemberRequest) {

        Member loginMember = memberRepository.findMemberInTeamByUserId(userDetails.getUserId(), teamId).orElseThrow(IllegalArgumentException::new);

        Team team = teamJpaRepository.findById(teamId).orElseThrow(IllegalArgumentException::new);

        Member member = memberRepository.findMemberInTeamByUserName(removeMemberRequest.getMemberName(), teamId).orElseThrow(() -> new IllegalArgumentException(removeMemberRequest.getMemberName() + " 를 찾을수 없습니다."));

        if (member.canUpdate(loginMember)) {
            memberJpaRepository.delete(member);
            team.removeMember(member);
            return true;
        } else {
            throw new IllegalStateException();
        }
    }

    @Transactional
    public void accept(@AuthenticationPrincipal CustomUserDetails userDetails, Long teamId) {
        Member member = memberRepository.findInviteByUserId(userDetails.getUserId(), teamId).orElseThrow(IllegalArgumentException::new);
        member.setStatus(Status.ACCEPTED);
    }

    @Transactional
    public void reject(@AuthenticationPrincipal CustomUserDetails userDetails, Long teamId) {
        Member member = memberRepository.findInviteByUserId(userDetails.getUserId(), teamId).orElseThrow(IllegalArgumentException::new);
        member.getTeam().removeMember(member);
        memberJpaRepository.delete(member);
    }


    private boolean isManagerOrOwner(Member member) {

        return member.getGrade().equals(Grade.OWNER) || member.getGrade().equals(Grade.MANAGER);
    }

    @Transactional
    public boolean updateMember(CustomUserDetails customUserDetails,Long memberId, UpdateMemberRequest updateMemberRequest) {

        // 수정될 member
        Member member = memberJpaRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException(memberId + " 를 찾을수 없습니다."));

        // 수정하는 사람
        Member updater = memberRepository.findByUserId(customUserDetails.getUserId()).orElseThrow(IllegalArgumentException::new);

        return member.update(updater, updateMemberRequest);
    }
}


