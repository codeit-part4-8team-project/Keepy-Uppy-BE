package com.keepyuppy.KeepyUppy.user.communication;

import com.keepyuppy.KeepyUppy.security.jwt.CustomUserDetails;
import com.keepyuppy.KeepyUppy.user.communication.response.UserResponse;
import com.keepyuppy.KeepyUppy.user.domain.entity.Users;
import com.keepyuppy.KeepyUppy.user.repository.UserRepository;
import com.keepyuppy.KeepyUppy.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/dashboard")
    public UserResponse test(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Users user = userService.findByOauthId(userDetails.getOauthId());
        return UserResponse.of(user);
    }

    @PutMapping( "/")
    public UserResponse updateUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Users user = userService.findByOauthId(userDetails.getOauthId());
        return UserResponse.of(user);
    }
}
