package com.keepyuppy.KeepyUppy.user.communication.controller;

import com.keepyuppy.KeepyUppy.security.jwt.CustomUserDetails;
import com.keepyuppy.KeepyUppy.user.communication.request.UpdateUserRequest;
import com.keepyuppy.KeepyUppy.user.communication.response.UserResponse;
import com.keepyuppy.KeepyUppy.user.domain.entity.Users;
import com.keepyuppy.KeepyUppy.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "UserController",description = "User 관련 컨트롤러 입니다.")
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원(본인) 프로필 조회")
    @GetMapping("/")
    public ResponseEntity<UserResponse> getUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Users user = userService.findById(userDetails.getUserId());
        return ResponseEntity.ok(UserResponse.of(user));
    }

    @Operation(summary = "회원 정보 업데이트")
    @PutMapping( "/")
    public ResponseEntity<UserResponse> updateUser(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UpdateUserRequest updateUserRequest
    ) {
        Users user = userService.updateUser(userDetails.getUserId(), updateUserRequest);
        return ResponseEntity.ok(UserResponse.of(user));
    }

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping( "/")
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        userService.deleteUser(userDetails.getUserId());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "유저네임 중복 확인")
    @GetMapping( "/checkUsername/{username}")
    public ResponseEntity<Boolean> checkUsername(@PathVariable String username) {
        boolean exists = userService.existsByUsername(username);
        return ResponseEntity.ok(exists);
    }
}
