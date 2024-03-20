package com.keepyuppy.KeepyUppy.security.oauth;

import com.keepyuppy.KeepyUppy.global.config.OAuth2ProviderConfig;
import com.keepyuppy.KeepyUppy.global.exception.CustomException;
import com.keepyuppy.KeepyUppy.security.communication.response.LoginResponse;
import com.keepyuppy.KeepyUppy.security.communication.response.OAuth2TokenResponse;
import com.keepyuppy.KeepyUppy.security.jwt.JwtUtils;
import com.keepyuppy.KeepyUppy.user.domain.entity.Users;
import com.keepyuppy.KeepyUppy.user.domain.enums.Provider;
import com.keepyuppy.KeepyUppy.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2Service {

    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final OAuth2ProviderConfig providerConfig;

    public LoginResponse login(String providerName, String code) {

        OAuth2Provider provider = providerConfig.getProviders().get(providerName);

        OAuth2TokenResponse tokenResponse = getToken(code, provider);
        OAuth2Attributes oAuth2Attributes = getOAuthAttributes(provider, tokenResponse, providerName);

        try {
            Users user = userService.findByOauthId(oAuth2Attributes.getOauthId());
            log.info("소셜 로그인에 성공했습니다.");
            return generateLoginResponse(user, false);
        } catch (CustomException e){
            // user is saved in repo within generateLoginResponse method
            Users user = oAuth2Attributes.toUserEntity();
            log.info("새 계정 생성에 성공했습니다.");
            return generateLoginResponse(user, true);
        }
    }


    private OAuth2TokenResponse getToken(String code, OAuth2Provider provider) {

        return WebClient.create()
                .post()
                .uri(provider.getTokenUrl())
                .headers(header -> {
                    header.setBasicAuth(provider.getClientId(), provider.getClientSecret());
                    header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    header.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
                    header.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
                })
                .bodyValue(tokenRequest(code, provider))
                .retrieve()
                .bodyToMono(OAuth2TokenResponse.class)
                .block();
    }

    private MultiValueMap<String, String> tokenRequest(String code, OAuth2Provider provider) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("client_id", provider.getClientId());
        formData.add("redirect_uri", provider.getRedirectUri());
        formData.add("code", code);
        formData.add("client_secret", provider.getClientSecret());
        return formData;
    }

    private OAuth2Attributes getOAuthAttributes(OAuth2Provider provider, OAuth2TokenResponse tokenResponse, String providerName) {
        Map<String, Object> attributes = WebClient.create()
                .get()
                .uri(provider.getUserInfoUrl())
                .headers(header -> header.setBearerAuth(tokenResponse.getAccessToken()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

        return OAuth2Attributes.of(Provider.of(providerName), attributes);
    }

    public LoginResponse generateLoginResponse(Users user, boolean newAccount) {
        String accessToken = jwtUtils.generateAccessToken(user.getOauthId());
        String refreshToken = jwtUtils.generateRefreshToken();
        userService.updateRefreshToken(user, refreshToken);

        return LoginResponse.of(user, accessToken, refreshToken, newAccount);
    }

}
