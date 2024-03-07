package com.keepyuppy.KeepyUppy.member.service;

import com.keepyuppy.KeepyUppy.member.communication.request.AddMemberRequest;
import com.keepyuppy.KeepyUppy.member.communication.request.RemoveMemberRequest;
import com.keepyuppy.KeepyUppy.member.domain.entity.Member;
import com.keepyuppy.KeepyUppy.member.domain.enums.Grade;
import com.keepyuppy.KeepyUppy.member.domain.enums.Status;
import com.keepyuppy.KeepyUppy.member.repository.MemberJpaRepoisitory;
import com.keepyuppy.KeepyUppy.member.repository.MemberRepositoryImpl;
import com.keepyuppy.KeepyUppy.team.domain.entity.Team;
import com.keepyuppy.KeepyUppy.team.repository.TeamJpaRepository;
import com.keepyuppy.KeepyUppy.user.domain.entity.Users;
import com.keepyuppy.KeepyUppy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberJpaRepoisitory memberJpaRepoisitory;
    private final TeamJpaRepository teamJpaRepository;
    private final UserRepository userRepository;
    private final MemberRepositoryImpl memberRepository;

    @Transactional
    public boolean addMember(Long teamId, AddMemberRequest addMemberRequest) {
        Team team = teamJpaRepository.findById(teamId).orElseThrow(() -> new IllegalArgumentException(teamId + " 는 존재하지 않는 팀 아이디 입니다."));
        Users user = userRepository.findById(addMemberRequest.getUserId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Member member = new Member(user, team, Grade.getInstance(addMemberRequest.getGrade()), addMemberRequest.getRole(), Status.PENDING);

        team.addMember(member);
        user.addMember(member);

        memberJpaRepoisitory.save(member);

        return true;
    }

    @Transactional
    public boolean removeMember(Long teamId, RemoveMemberRequest removeMemberRequest) {
        Team team = teamJpaRepository.findById(teamId).orElseThrow(() -> new IllegalArgumentException(teamId + " 는 존재하지 않는 팀 아이디 입니다."));
        Users user = userRepository.findById(removeMemberRequest.getMemberId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Member member = memberRepository.findByUserId(user.getId()).orElseThrow(() -> new IllegalArgumentException(removeMemberRequest.getMemberId() + " 를 찾을수 없습니다."));
        team.removeMember(member);

        return true;
    }

    public void changeRole() {

    }


}
