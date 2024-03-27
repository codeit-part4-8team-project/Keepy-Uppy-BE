package com.keepyuppy.KeepyUppy.user.communication.controller;

import com.keepyuppy.KeepyUppy.security.jwt.CustomUserDetails;
import com.keepyuppy.KeepyUppy.user.communication.request.UpdateUserRequest;
import com.keepyuppy.KeepyUppy.user.communication.response.UserResponse;
import com.keepyuppy.KeepyUppy.user.domain.entity.Users;
import com.keepyuppy.KeepyUppy.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "UserController", description = "User 관련 컨트롤러 입니다.")
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {

    private final UserService userService;

    @GetMapping("/")
    @Operation(summary = "회원(본인) 프로필 조회")
    public ResponseEntity<UserResponse> getUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Users user = userService.findById(userDetails.getUserId());
        return ResponseEntity.ok(UserResponse.of(user));
    }

    @PutMapping("/")
    @Operation(summary = "회원 정보 업데이트")
    public ResponseEntity<UserResponse> updateUser(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UpdateUserRequest updateUserRequest
    ) {
        return ResponseEntity.ok(userService.updateUser(userDetails.getUserId(), updateUserRequest));
    }

    @DeleteMapping("/")
    @Operation(summary = "회원 탈퇴")
    public ResponseEntity<String> deleteUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        userService.deleteUser(userDetails.getUserId());
        return ResponseEntity.ok("회원 탈퇴 성공");
    }

    @GetMapping("/check-username/{username}")
    @Operation(summary = "유저네임 중복 확인")
    public ResponseEntity<Boolean> checkUsername(@PathVariable String username) {
        boolean exists = userService.existsByUsername(username);
        return ResponseEntity.ok(exists);
    }

    @PutMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "회원 프로필 이미지 변경")
    public void updateProfileImage(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestPart(value = "image") MultipartFile multipartFile) {
        userService.updateProfileImage(userDetails, multipartFile);
    }

    @GetMapping("/my-issue")
    @Operation(summary = "내 이슈 조회, 팀 별 필터링 가능")
    public ResponseEntity<UserIssueBoardResponse> getMyIssues(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) List<Long> teamIds) {
        if (teamIds == null || teamIds.isEmpty()) {
            return ResponseEntity.ok(issueService.getMyIssueBoard(userDetails));
        } else {
            return ResponseEntity.ok(issueService.getIssueBoardByUserAndTeams(userDetails, teamIds));
        }
    }

    @Operation(summary = "유저네임이 일치하는 유저 조회")
    @GetMapping("/search")
    public ResponseEntity<UserResponse> findByUsername(@RequestParam String username) {
        return ResponseEntity.ok(userService.findByUsername(username));
    }

    @Operation(summary = "팀 통합 자유게시판 조회, 팀 별 필터링 가능")
    @GetMapping("/post")
    public ResponseEntity<Page<PostResponse>> getUserPosts(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) List<Long> teamIds,
            @RequestParam(defaultValue = "1") int page
    ){
        if (teamIds == null || teamIds.isEmpty()) {
            return ResponseEntity.ok(postService.getPostPaginateByUser(userDetails, page));
        } else {
            return ResponseEntity.ok(postService.getPostPaginateFilter(userDetails, teamIds, page));
        }
    }
}

