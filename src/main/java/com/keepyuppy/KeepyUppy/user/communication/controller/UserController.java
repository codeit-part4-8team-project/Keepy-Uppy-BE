package com.keepyuppy.KeepyUppy.user.communication.controller;

import com.keepyuppy.KeepyUppy.post.communication.response.AnnouncementResponse;
import com.keepyuppy.KeepyUppy.post.service.AnnouncementService;
import com.keepyuppy.KeepyUppy.post.service.PostService;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "UserController", description = "User 관련 컨트롤러 입니다.")
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {

    private final UserService userService;
    private final AnnouncementService announcementService;

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

    @GetMapping("/unread")
    @Operation(summary = "읽지 않은 공지글 조회")
    public ResponseEntity<List<AnnouncementResponse>> getUnreadAnnouncements(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(announcementService.getUnreadAnnouncementsByUser(userDetails));
    }

    @PutMapping("/read/{announcementId}")
    @Operation(summary = "공지글 읽음 표시")
    public ResponseEntity<String> readAnnouncement(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long announcementId) {
        announcementService.markAsRead(userDetails, announcementId);
        return ResponseEntity.ok("공지 읽음 표시 성공");
    }

    @Operation(summary = "유저네임이 일치하는 유저 조회")
    @GetMapping("/search")
    public ResponseEntity<UserResponse> findByUsername(@RequestParam String username) {
        return ResponseEntity.ok(userService.findByUsername(username));
    }
}

