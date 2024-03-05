package com.keepyuppy.KeepyUppy.member.service;

import com.keepyuppy.KeepyUppy.member.communication.request.AddMemberRequest;
import com.keepyuppy.KeepyUppy.member.domain.entity.Member;
import com.keepyuppy.KeepyUppy.member.domain.enums.Grade;
import com.keepyuppy.KeepyUppy.member.domain.enums.Role;
import com.keepyuppy.KeepyUppy.member.repository.MemberJpaRepoisitory;
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

    @Transactional
    public Member addMember(Long id, AddMemberRequest addMemberRequest) {
        Team team = teamJpaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(id + " 는 존재하지 않는 팀 아이디 입니다."));
        Users users = userRepository.findById(addMemberRequest.getUsersId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Member member = new Member(users, team, Grade.getInstance(addMemberRequest.getGrade()), Role.getInstance(addMemberRequest.getRole()));

        team.addMember(member);
        users.addMember(member);

        memberJpaRepoisitory.save(member);

        return member;
    }
}
