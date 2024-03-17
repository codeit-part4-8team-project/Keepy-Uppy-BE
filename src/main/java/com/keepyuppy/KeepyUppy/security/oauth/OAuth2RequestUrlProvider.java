package com.keepyuppy.KeepyUppy.security.oauth;

import com.keepyuppy.KeepyUppy.global.config.OAuth2ProviderConfig;
import com.keepyuppy.KeepyUppy.user.domain.enums.Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class OAuth2RequestUrlProvider {

    private final OAuth2ProviderConfig providerConfig;

    public String provide(Provider provider) {
        return switch (provider) {
            case GOOGLE -> ofGoogle();
            case KAKAO -> ofKakao();
            case GITHUB -> ofGithub();
        };
    }

    public String ofGoogle() {
        OAuth2Provider google = providerConfig.getProviders().get("google");
        return UriComponentsBuilder
                .fromUriString("https://accounts.google.com/o/oauth2/v2/auth")
                .queryParam("response_type", "code")
                .queryParam("client_id", google.getClientId())
                .queryParam("redirect_uri", google.getRedirectUri())
                .queryParam("scope", String.join(" ", google.getScope()))
                .toUriString();
    }

    public String ofKakao() {
        OAuth2Provider kakao = providerConfig.getProviders().get("kakao");
        return UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com/oauth/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", kakao.getClientId())
                .queryParam("redirect_uri", kakao.getRedirectUri())
                .queryParam("scope", String.join(",", kakao.getScope()))
                .toUriString();
    }

    public String ofGithub() {
        OAuth2Provider github = providerConfig.getProviders().get("github");
        return UriComponentsBuilder
                .fromUriString("https://github.com/login/oauth/authorize")
                .queryParam("client_id", github.getClientId())
                .queryParam("redirect_uri", github.getRedirectUri())
                .queryParam("scope", String.join(" ", github.getScope()))
                .toUriString();
    }
}
