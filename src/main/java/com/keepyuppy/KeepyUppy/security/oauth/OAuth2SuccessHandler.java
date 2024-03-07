package com.keepyuppy.KeepyUppy.security.oauth;

import com.keepyuppy.KeepyUppy.security.communication.response.LoginResponse;
import com.keepyuppy.KeepyUppy.security.jwt.JwtUtils;
import com.keepyuppy.KeepyUppy.user.domain.entity.Users;
import com.keepyuppy.KeepyUppy.user.domain.enums.Provider;
import com.keepyuppy.KeepyUppy.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;
    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String oauthId = oAuth2User.getAttribute("oauthId");

        Users user = userService.findByOauthId(oauthId);

        if (user == null) {
            // user is saved in repo within setTokens method
            user = toUserEntity(oAuth2User);
            setTokens(oauthId, user, response, true);
            log.info("새 계정 생성에 성공했습니다.");
        } else {
            setTokens(oauthId, user, response, false);
            log.info("소셜 로그인에 성공했습니다.");
        }


    }

    public void setTokens(String oauthId, Users user, HttpServletResponse response, boolean newAccount) throws IOException {
        String accessToken = jwtUtils.generateAccessToken(oauthId);
        String refreshToken = jwtUtils.generateRefreshToken();
        userService.updateRefreshToken(user, refreshToken);

        LoginResponse loginResponse = LoginResponse.of(user, accessToken, refreshToken, newAccount);

        response.addHeader(accessToken, "Bearer " + accessToken);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(loginResponse.toString());
    }

    public Users toUserEntity(OAuth2User oAuth2User){
        String username = generateUsername();
        return Users.builder()
                .oauthId(oAuth2User.getAttribute("oauthId"))
                .username(username)
                .provider(Provider.valueOf(oAuth2User.getAttribute("provider")))
                .name(oAuth2User.getAttribute("name"))
                .imageUrl(oAuth2User.getAttribute("imageUrl"))
                .build();
    }

    public static String generateUsername() {
        int length = 10;

        byte[] randomBytes = new byte[length];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(randomBytes);
        String randomString = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
        randomString = randomString.replaceAll("[^a-zA-Z0-9]", "");

        return "user-" + randomString.substring(0, Math.min(length, randomString.length()));
    }

}
