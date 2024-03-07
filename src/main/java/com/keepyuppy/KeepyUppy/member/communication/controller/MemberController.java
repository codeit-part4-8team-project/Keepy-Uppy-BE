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
@RequestMapping("/api/member")
@Tag(name = "MemberController", description = "팀, 스터디 멤버 관리 컨트롤러 입니다.")
public class MemberController {
    private final MemberService memberService;

    // todo
    // 요청 유저가 매니저 이상의 권한을 가졌는지 확인 로직 추가
    @Operation(summary = "팀에 멤버 초대")
    @PostMapping("/{teamId}")
    public boolean addMember(@PathVariable Long teamId, @RequestBody AddMemberRequest addMemberRequest) {
        return memberService.addMember(teamId,addMemberRequest);
    }

    @Operation(summary = "팀에서 멤버 제외")
    @DeleteMapping("/{teamId}")
    public boolean removeMember(@PathVariable Long teamId, @RequestBody RemoveMemberRequest removeMemberRequest) {
        return memberService.removeMember(teamId, removeMemberRequest);
    }

    @Operation(summary = "팀 초대 수락")
    @PutMapping("/{teamId}")
    public void acceptInvite(
            // 로그인한 유저 정보
            @PathVariable Long teamId
    ) {
        // 유저정보 , teamId 로 찾은 팀정보로 Member Status pending -> accept
        memberService.accept(teamId);
    }

    @Operation(summary = "팀 초대 거절")
    @DeleteMapping("/{teamId}")
    public void rejectInvite(
            // 로그인한 유저정보
            @PathVariable Long teamId
    ) {
        // 유저정보 , teamId 로 찾은 팀정보로 Member delete
        memberService.reject(teamId);
    }
}

