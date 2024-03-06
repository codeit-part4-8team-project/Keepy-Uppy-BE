package com.keepyuppy.KeepyUppy.member.service;

import com.keepyuppy.KeepyUppy.member.communication.request.AddMemberRequest;
import com.keepyuppy.KeepyUppy.member.communication.request.RemoveMemberRequest;
import com.keepyuppy.KeepyUppy.member.domain.entity.Member;
import com.keepyuppy.KeepyUppy.member.domain.enums.Grade;
import com.keepyuppy.KeepyUppy.member.domain.enums.Role;
import com.keepyuppy.KeepyUppy.member.domain.enums.Status;
import com.keepyuppy.KeepyUppy.member.repository.MemberJpaRepoisitory;
import com.keepyuppy.KeepyUppy.member.repository.MemberRepositoryImpl;
import com.keepyuppy.KeepyUppy.organization.domain.entity.Organization;
import com.keepyuppy.KeepyUppy.organization.repository.OrganizationJpaRepository;
import com.keepyuppy.KeepyUppy.user.domain.entity.Users;
import com.keepyuppy.KeepyUppy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberJpaRepoisitory memberJpaRepoisitory;
    private final OrganizationJpaRepository organizationJpaRepository;
    private final UserRepository userRepository;
    private final MemberRepositoryImpl memberRepository;

    @Transactional
    public boolean addMember(Long teamId, AddMemberRequest addMemberRequest) {
        Organization organization = organizationJpaRepository.findById(teamId).orElseThrow(() -> new IllegalArgumentException(teamId + " 는 존재하지 않는 팀 아이디 입니다."));
        Users users = userRepository.findById(addMemberRequest.getUsersId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Member member = new Member(users, organization, Grade.getInstance(addMemberRequest.getGrade()), Role.getInstance(addMemberRequest.getRole()), Status.PENDING);

        organization.addMember(member);
        users.addMember(member);

        memberJpaRepoisitory.save(member);

        return true;
    }

    @Transactional
    public boolean removeMember(Long teamId, RemoveMemberRequest removeMemberRequest) {
        Organization organization = organizationJpaRepository.findById(teamId).orElseThrow(() -> new IllegalArgumentException(teamId + " 는 존재하지 않는 팀 아이디 입니다."));
        Users users = userRepository.findById(removeMemberRequest.getMemberId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Member member = memberRepository.findByUserId(users.getId()).orElseThrow(() -> new IllegalArgumentException(removeMemberRequest.getMemberId() + " 를 찾을수 없습니다."));
        organization.removeMember(member);

        return true;
    }

    public void changeRole() {

    }


}
