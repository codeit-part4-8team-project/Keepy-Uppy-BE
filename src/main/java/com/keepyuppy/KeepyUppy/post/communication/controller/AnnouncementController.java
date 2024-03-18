package com.keepyuppy.KeepyUppy.post.communication.controller;

import com.keepyuppy.KeepyUppy.post.communication.request.PostRequest;
import com.keepyuppy.KeepyUppy.post.communication.response.AnnouncementResponse;
import com.keepyuppy.KeepyUppy.post.communication.response.PostResponse;
import com.keepyuppy.KeepyUppy.post.domain.enums.ContentType;
import com.keepyuppy.KeepyUppy.post.service.AnnouncementService;
import com.keepyuppy.KeepyUppy.security.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/{teamId}/announcement")
@Tag(name = "AnnouncementController", description = "공지글 관련 컨트롤러 입니다.")
@SecurityRequirement(name = "Bearer Authentication")
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @PostMapping("/")
    @Operation(summary = "공지글 작성")
    public ResponseEntity<AnnouncementResponse> createAnnouncement(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long teamId,
            @RequestBody PostRequest postRequest) {

        return ResponseEntity.ok(announcementService.createAnnouncement(userDetails, teamId, postRequest));
    }

    @GetMapping("/{announcementId}")
    @Operation(summary = "공지글 조회")
    public ResponseEntity<AnnouncementResponse> viewAnnouncement(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long teamId,
            @PathVariable Long announcementId) {

        return ResponseEntity.ok(announcementService.viewAnnouncement(userDetails, teamId, announcementId));
    }

    @PutMapping("/{announcementId}")
    @Operation(summary = "공지글 수정")
    public ResponseEntity<AnnouncementResponse> updateAnnouncement(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long teamId,
            @PathVariable Long announcementId,
            @RequestBody PostRequest postRequest) {

        return ResponseEntity.ok(announcementService.updateAnnouncement(userDetails, teamId, announcementId, postRequest));
    }

    @DeleteMapping("/{announcementId}")
    @Operation(summary = "공지글 삭제")
    public ResponseEntity<String> deleteAnnouncement(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long teamId,
            @PathVariable Long announcementId) {

        announcementService.deleteAnnouncement(userDetails, teamId, announcementId);
        return ResponseEntity.ok("게시글 삭제 성공");
    }

    @PutMapping("/{announcementId}/pin")
    @Operation(summary = "공지글 고정")
    public ResponseEntity<String> pinAnnouncement(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long teamId,
            @PathVariable Long announcementId) {

        announcementService.pinAnnouncement(userDetails, teamId, announcementId, true);
        return ResponseEntity.ok("공지글 고정 성공");
    }

    @PutMapping("/{announcementId}/unpin")
    @Operation(summary = "공지글 고정 해제")
    public ResponseEntity<String> unpinAnnouncement(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long teamId,
            @PathVariable Long announcementId) {

        announcementService.pinAnnouncement(userDetails, teamId, announcementId, false);
        return ResponseEntity.ok("공지글 고정 해제 성공");
    }

    @GetMapping("/")
    @Operation(summary = "팀 공지 게시판 조회")
    public ResponseEntity<Page<AnnouncementResponse>> viewTeamAnnouncements(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long teamId,
            @RequestParam(defaultValue = "1") int page) {

        return ResponseEntity.ok(announcementService.getAnnouncementsByTeam(userDetails, teamId, page));
    }

}
