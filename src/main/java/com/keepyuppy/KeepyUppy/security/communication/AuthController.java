package com.keepyuppy.KeepyUppy.security.communication;

import com.keepyuppy.KeepyUppy.global.exception.CustomException;
import com.keepyuppy.KeepyUppy.global.exception.ExceptionType;
import com.keepyuppy.KeepyUppy.security.communication.response.LoginResponse;
import com.keepyuppy.KeepyUppy.security.communication.response.TokenResponse;
import com.keepyuppy.KeepyUppy.security.communication.response.UrlResponse;
import com.keepyuppy.KeepyUppy.security.jwt.JwtUtils;
import com.keepyuppy.KeepyUppy.security.oauth.OAuth2RequestUrlProvider;
import com.keepyuppy.KeepyUppy.security.oauth.OAuth2Service;
import com.keepyuppy.KeepyUppy.user.domain.entity.Users;
import com.keepyuppy.KeepyUppy.user.domain.enums.Provider;
import com.keepyuppy.KeepyUppy.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "AuthController",description = "Authentication 관련 컨트롤러 입니다.")
public class AuthController {

    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final OAuth2RequestUrlProvider urlProvider;
    private final OAuth2Service oAuth2Service;

    @Operation(summary = "OAuth 로그인 페이지 Url 생성")
    @GetMapping("/oauth/url/{provider}")
    public ResponseEntity<UrlResponse> redirectOAuth(@PathVariable String provider) {

        String redirectUrl = urlProvider.provide(Provider.of(provider));
        return ResponseEntity.ok(new UrlResponse(redirectUrl));
    }

    @Operation(summary = "OAuth 로그인")
    @GetMapping("/oauth/login/{provider}")
    public ResponseEntity<LoginResponse> loginOAuth(
            @PathVariable String provider,
            @RequestParam("code") String code
    ) {
        if (provider.equals("google")) {
            code = URLDecoder.decode(code, StandardCharsets.UTF_8);
        }
        return ResponseEntity.ok().body(oAuth2Service.login(provider, code));
    }

    @Operation(summary = "회원 로그아웃")
    @PostMapping("/auth/logout")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<String> logout(final HttpServletRequest request) {

        // delete refresh token saved in repo
        String accessToken = jwtUtils.resolveToken(request);
        String oauthId = jwtUtils.parseClaims(accessToken);
        Users user = userService.findByOauthId(oauthId);

        userService.updateRefreshToken(user, "");
        return ResponseEntity.ok("로그아웃 성공");
    }

    @Operation(summary = "새 Refresh 토큰 발급")
    @PostMapping("/auth/refresh")
    public ResponseEntity<TokenResponse> refresh(final HttpServletRequest request) {

        // generate new access token if refresh token is valid
        String refreshToken = jwtUtils.resolveToken(request);
        Users user = userService.findByRefreshToken(refreshToken);
        if (jwtUtils.verifyToken(refreshToken, false) && refreshToken.equals(user.getRefreshToken())) {
            String newAccessToken = jwtUtils.generateAccessToken(user.getOauthId());
            return ResponseEntity.ok(new TokenResponse(newAccessToken));
        } else {
            throw new CustomException(ExceptionType.UNAUTHORIZED);
        }
    }
}
