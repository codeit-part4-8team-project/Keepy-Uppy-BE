package com.keepyuppy.KeepyUppy.security.communication;

import com.keepyuppy.KeepyUppy.security.communication.response.TokenResponse;
import com.keepyuppy.KeepyUppy.security.jwt.JwtUtils;
import com.keepyuppy.KeepyUppy.user.domain.entity.Users;
import com.keepyuppy.KeepyUppy.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtils jwtUtils;

    @PostMapping("/logout")
    public ResponseEntity<String> logout(final HttpServletRequest request) {

        // delete refresh token saved in repo
        String accessToken = jwtUtils.resolveToken(request);
        String oauthId = jwtUtils.parseClaims(accessToken);
        Users user = userService.findByOauthId(oauthId);

        if (user != null){
            userService.updateRefreshToken(user, "");
            return ResponseEntity.ok("로그아웃 성공");
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(final HttpServletRequest request) {

        // generate new access token if refresh token is valid
        String refreshToken = jwtUtils.resolveToken(request);
        Users user = userService.findByRefreshToken(refreshToken);
        if (user != null && jwtUtils.verifyToken(refreshToken, false) && refreshToken.equals(user.getRefreshToken())) {
            String newAccessToken = jwtUtils.generateAccessToken(user.getOauthId());
            return ResponseEntity.ok(new TokenResponse(newAccessToken));
        }

        return ResponseEntity.badRequest().build();
    }
}
