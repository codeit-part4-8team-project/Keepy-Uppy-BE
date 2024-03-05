package com.keepyuppy.KeepyUppy.security.communication;

import com.keepyuppy.KeepyUppy.security.communication.response.TokenResponse;
import com.keepyuppy.KeepyUppy.security.jwt.JwtUtils;
import com.keepyuppy.KeepyUppy.user.domain.entity.Users;
import com.keepyuppy.KeepyUppy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    //TODO: Deal with exceptions

    @PostMapping("auth/signout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") final String accessToken) {

        // delete refresh token saved in repo
        String oauthId = jwtUtils.parseClaims(accessToken);
        Users user = userRepository.findByOauthId(oauthId).orElse(null);

        user.updateRefreshToken("");
        userRepository.save(user);

        return ResponseEntity.ok("로그아웃 성공");
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<Void> refresh(@RequestHeader("Authorization") final String refreshToken) {

        // generate new access token if refresh token is valid
        Users user = userRepository.findByRefreshToken(refreshToken).orElse(null);
        if (user != null && jwtUtils.verifyToken(refreshToken) && refreshToken.equals(user.getRefreshToken())) {

            String newAccessToken = jwtUtils.generateAccessToken(user.getOauthId());
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + newAccessToken);
            return ResponseEntity.ok().headers(headers).build();
        }

        return ResponseEntity.badRequest().build();
    }
}
