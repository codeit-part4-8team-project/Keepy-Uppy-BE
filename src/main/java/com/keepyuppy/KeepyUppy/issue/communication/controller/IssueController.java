package com.keepyuppy.KeepyUppy.issue.communication.controller;

import com.keepyuppy.KeepyUppy.issue.communication.request.IssueRequest;
import com.keepyuppy.KeepyUppy.issue.communication.request.IssueStatusRequest;
import com.keepyuppy.KeepyUppy.issue.communication.request.IssueTagRequest;
import com.keepyuppy.KeepyUppy.issue.communication.response.IssueBoardResponse;
import com.keepyuppy.KeepyUppy.issue.communication.response.IssueResponse;
import com.keepyuppy.KeepyUppy.issue.communication.response.IssueTagResponse;
import com.keepyuppy.KeepyUppy.issue.service.IssueService;
import com.keepyuppy.KeepyUppy.issue.service.IssueTagService;
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
@RequestMapping("/api/{teamId}/issue")
@Tag(name = "IssueController", description = "이슈 관련 컨트롤러 입니다.")
@SecurityRequirement(name = "Bearer Authentication")
public class IssueController {

    private final IssueService issueService;
    private final IssueTagService tagService;

    @PostMapping("/")
    @Operation(summary = "이슈 작성")
    public ResponseEntity<IssueResponse> createIssue(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long teamId,
            @RequestBody IssueRequest issueRequest) {

        return ResponseEntity.ok(issueService.createIssue(userDetails, teamId, issueRequest));
    }

    @GetMapping("/{issueId}")
    @Operation(summary = "이슈 조회")
    public ResponseEntity<IssueResponse> viewIssue(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long teamId,
            @PathVariable Long issueId) {

        return ResponseEntity.ok(issueService.viewIssue(userDetails, teamId, issueId));
    }

    @PutMapping("/{issueId}")
    @Operation(summary = "이슈 수정")
    public ResponseEntity<IssueResponse> updateIssue(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long teamId,
            @PathVariable Long issueId,
            @RequestBody IssueRequest issueRequest) {

        return ResponseEntity.ok(issueService.updateIssue(userDetails, teamId, issueId, issueRequest));
    }

    @DeleteMapping("/{issueId}")
    @Operation(summary = "이슈 삭제")
    public ResponseEntity<String> deleteIssue(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long teamId,
            @PathVariable Long issueId) {

        issueService.deleteIssue(userDetails, teamId, issueId);
        return ResponseEntity.ok("이슈 삭제 성공");
    }

    @PutMapping("/{issueId}/status")
    @Operation(summary = "이슈 진행상황 수정")
    public ResponseEntity<IssueResponse> updateIssueStatus(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long teamId,
            @PathVariable Long issueId,
            @RequestBody IssueStatusRequest statusRequest) {

        return ResponseEntity.ok(issueService.updateStatus(userDetails, teamId, issueId, statusRequest));
    }

    @GetMapping("/")
    @Operation(summary = "팀 이슈 보드 조회")
    public ResponseEntity<IssueBoardResponse> getTeamIssueBoard(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long teamId) {
        return ResponseEntity.ok(issueService.getTeamIssueBoard(userDetails, teamId));
    }

    @PostMapping("/tag")
    @Operation(summary = "팀 이슈 태그 생성")
    public ResponseEntity<IssueTagResponse> createTeamTag(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long teamId,
            @RequestBody IssueTagRequest tagRequest) {
        return ResponseEntity.ok(tagService.createTag(userDetails, teamId, tagRequest));
    }

    @GetMapping("/tag")
    @Operation(summary = "팀 이슈 태그 조회")
    public ResponseEntity<List<IssueTagResponse>> getTeamTags(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long teamId) {
        return ResponseEntity.ok(tagService.getTeamTags(userDetails, teamId));
    }

    @DeleteMapping("/tag")
    @Operation(summary = "팀 이슈 태그 삭제")
    public ResponseEntity<String> deleteTeamTag(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long teamId,
            @RequestBody String tagName) {
        tagService.deleteTag(userDetails, teamId, tagName);
        return ResponseEntity.ok("태그 삭제 성공");
    }
}
