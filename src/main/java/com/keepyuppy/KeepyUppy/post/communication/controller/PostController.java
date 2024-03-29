package com.keepyuppy.KeepyUppy.post.communication.controller;

import com.keepyuppy.KeepyUppy.post.communication.request.PostRequest;
import com.keepyuppy.KeepyUppy.post.communication.response.AnnouncementResponse;
import com.keepyuppy.KeepyUppy.post.communication.response.PostResponse;
import com.keepyuppy.KeepyUppy.post.service.PostService;
import com.keepyuppy.KeepyUppy.security.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
@Tag(name = "PostController", description = "게시글 관련 컨트롤러 입니다.")
@SecurityRequirement(name = "Bearer Authentication")
public class PostController {

    private final PostService postService;

    @PostMapping("/{teamId}")
    @Operation(summary = "게시글 작성")
    public ResponseEntity<PostResponse> createPost(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long teamId,
            @RequestBody PostRequest postRequest) {

        return ResponseEntity.ok(postService.createPost(userDetails, teamId, postRequest));
    }

    @GetMapping("/{postId}")
    @Operation(summary = "게시글 조회")
    public ResponseEntity<PostResponse> viewPost(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long postId) {

        return ResponseEntity.ok(postService.viewPost(userDetails, postId));
    }

    @PutMapping("/{postId}")
    @Operation(summary = "게시글 수정")
    public ResponseEntity<PostResponse> updatePost(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long postId,
            @RequestBody PostRequest postRequest) {

        return ResponseEntity.ok(postService.updatePost(userDetails, postId, postRequest));
    }

    @PutMapping("/{postId}/convert")
    @Operation(summary = "게시글을 공지글로 수정")
    public ResponseEntity<AnnouncementResponse> updatePostAsAnnouncement(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long postId,
            @RequestBody PostRequest postRequest) {
        return ResponseEntity.ok(postService.convertAsAnnouncement(userDetails, postId, postRequest));
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "게시글 삭제")
    public ResponseEntity<String> deletePost(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long postId) {

        postService.deletePost(userDetails, postId);
        return ResponseEntity.ok("게시글 삭제 성공");
    }

    @GetMapping("team/{teamId}")
    @Operation(summary = "팀 자유 게시판 페이지 조회")
    public ResponseEntity<Page<PostResponse>> getPostsWithPagination(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long teamId,
            @RequestParam(defaultValue = "1") int page) {

        return ResponseEntity.ok(postService.getPostPaginateByTeam(userDetails, teamId, page));
    }

    @Operation(summary = "팀 통합 자유게시판 조회, 팀 별 필터링 가능")
    @GetMapping("/user")
    public ResponseEntity<Page<PostResponse>> getUserPosts(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) List<Long> teamIds,
            @RequestParam(defaultValue = "1") int page
    ){
        return ResponseEntity.ok(postService.getPostPaginateFilter(userDetails, teamIds, page));
    }

    @Operation(summary = "게시글 좋아요")
    @PostMapping("/like/{postId}")
    public ResponseEntity<PostResponse> likePost(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long postId) {
        return ResponseEntity.ok(postService.likePost(userDetails, postId));
    }

    @Operation(summary = "게시글 좋아요 취소")
    @PostMapping("/unlike/{postId}")
    public ResponseEntity<PostResponse> unLikePost(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long postId) {
        return ResponseEntity.ok(postService.unlikePost(userDetails, postId));
    }
}
