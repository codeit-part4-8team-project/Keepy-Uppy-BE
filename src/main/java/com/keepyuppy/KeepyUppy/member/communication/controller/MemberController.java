package com.keepyuppy.KeepyUppy.member.communication.controller;

import com.keepyuppy.KeepyUppy.member.communication.request.AddMemberRequest;
import com.keepyuppy.KeepyUppy.member.communication.request.RemoveMemberRequest;
import com.keepyuppy.KeepyUppy.member.communication.request.UpdateMemberRequest;
import com.keepyuppy.KeepyUppy.member.communication.response.MemberResponse;
import com.keepyuppy.KeepyUppy.member.service.MemberService;
import com.keepyuppy.KeepyUppy.security.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
@Tag(name = "MemberController", description = "Member 관련 컨트롤러 입니다.")
@SecurityRequirement(name = "Bearer Authentication")
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "팀에 멤버 초대")
    @PostMapping("/{teamId}")
    public ResponseEntity<Boolean> addMember(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long teamId, @RequestBody AddMemberRequest addMemberRequest) {
        return ResponseEntity.ok(memberService.addMember(userDetails, teamId, addMemberRequest));
    }

    @Operation(summary = "팀에서 멤버 제외")
    @DeleteMapping("/{teamId}")
    public ResponseEntity<Boolean> removeMember(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long teamId, @RequestBody RemoveMemberRequest removeMemberRequest) {
        return ResponseEntity.ok(memberService.removeMember(userDetails, teamId, removeMemberRequest));
    }

    @Operation(summary = "팀원 조회")
    @GetMapping("/{teamId}")
    public ResponseEntity<List<MemberResponse>> getMembers(@PathVariable Long teamId) {
        return ResponseEntity.ok(memberService.getMembers(teamId));
    }

    @Operation(summary = "정보 수정")
    @PutMapping("/{memberId}")
    public ResponseEntity<Boolean> updateMember(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long memberId, @RequestBody UpdateMemberRequest updateMemberRequest) {
        return ResponseEntity.ok(memberService.updateMember(customUserDetails, memberId, updateMemberRequest));
    }

    @Operation(summary = "팀 초대 수락")
    @PutMapping("/invite/{teamId}")
    public ResponseEntity<Void> acceptInvite(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long teamId) {
        memberService.accept(userDetails, teamId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "팀 초대 거절")
    @DeleteMapping("/invite/{teamId}")
    public ResponseEntity<Void> rejectInvite(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long teamId) {
        memberService.reject(userDetails, teamId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "팀 내에서 유저네임 부분 일치 검색")
    @GetMapping("/{teamId}/search")
    public ResponseEntity<List<MemberResponse>> findMemberByUsernamePattern(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long teamId,
            @RequestParam(defaultValue = "") String username
    ){
        return ResponseEntity.ok(memberService.findByUsernamePattern(userDetails, teamId, username));
    }
}


