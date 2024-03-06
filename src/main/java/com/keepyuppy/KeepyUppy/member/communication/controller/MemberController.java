package com.keepyuppy.KeepyUppy.member.communication.controller;

import com.keepyuppy.KeepyUppy.member.communication.request.AddMemberRequest;
import com.keepyuppy.KeepyUppy.member.communication.request.RemoveMemberRequest;
import com.keepyuppy.KeepyUppy.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/member")
@Tag(name = "MemberController", description = "팀, 스터디 멤버 관리 컨트롤러 입니다.")
public class MemberController {
    private final MemberService memberService;

    // todo
    // 요청 유저가 매니저 이상의 권한을 가졌는지 확인 로직 추가
    @Operation(summary = "organization 에 멤버 초대")
    @PostMapping("/add/{teamId}")
    public boolean addMember(@PathVariable Long teamId, @RequestBody AddMemberRequest addMemberRequest) {
        return memberService.addMember(teamId,addMemberRequest);
    }

    @Operation(summary = "organization 에서 멤버 제외")
    @PostMapping("/remove/{teamId}")
    public boolean removeMember(@PathVariable Long teamId, @RequestBody RemoveMemberRequest removeMemberRequest) {
        return memberService.removeMember(teamId, removeMemberRequest);
    }
}
