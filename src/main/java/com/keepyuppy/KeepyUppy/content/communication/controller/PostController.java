package com.keepyuppy.KeepyUppy.content.communication.controller;

import com.keepyuppy.KeepyUppy.content.communication.request.CreatePostRequest;
import com.keepyuppy.KeepyUppy.content.communication.response.PostResponse;
import com.keepyuppy.KeepyUppy.content.service.PostService;
import com.keepyuppy.KeepyUppy.security.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/{teamId}/post")
@Tag(name = "PostController", description = "게시글 관련 컨트롤러 입니다.")
@SecurityRequirement(name = "Bearer Authentication")
public class PostController {

    private final PostService postService;

    @PostMapping("/")
    @Operation(summary = "게시글 작성")
    public ResponseEntity<PostResponse> createPost(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long teamId,
            @RequestBody CreatePostRequest createPostRequest) {

        return ResponseEntity.ok(postService.createPost(userDetails, teamId, createPostRequest));
    }

    @GetMapping("/{postId}")
    @Operation(summary = "게시글 조회")
    public ResponseEntity<PostResponse> viewPost(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long teamId,
            @PathVariable Long postId) {

        return ResponseEntity.ok(postService.viewPost(userDetails, teamId, postId));
    }
}
