package com.keepyuppy.KeepyUppy.member.communication.controller;

import com.keepyuppy.KeepyUppy.member.communication.request.AddMemberRequest;
import com.keepyuppy.KeepyUppy.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    @PostMapping("/user/team/member/add/{id}")
    public void addMember(@PathVariable Long id, @RequestBody AddMemberRequest addMemberRequest) {
        memberService.addMember(id,addMemberRequest);
    }
}
