package com.keepyuppy.KeepyUppy.security.oauth;

import com.keepyuppy.KeepyUppy.security.communication.response.TokenResponse;
import com.keepyuppy.KeepyUppy.security.jwt.JwtUtils;
import com.keepyuppy.KeepyUppy.user.domain.entity.Users;
import com.keepyuppy.KeepyUppy.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String oauthId = oAuth2User.getAttribute("oauthId");

        Users user = userRepository.findByOauthId(oauthId).orElse(null);

        if (user == null){
            // register new user
            // TODO: 새 유저 정보 받기 및 저장

        } else {
            String accessToken = jwtUtils.generateAccessToken(oauthId);
            response.addHeader("Authorization", "Bearer " + accessToken);

            String refreshToken = jwtUtils.generateRefreshToken();
            jwtUtils.updateRefreshToken(oauthId, refreshToken);

            TokenResponse tokens = new TokenResponse(accessToken, refreshToken);
            String tokensJson = tokens.toString();

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.getWriter().write(tokensJson);

            log.info("소셜 로그인에 성공했습니다.");
        }
    }

}
