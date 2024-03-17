package com.keepyuppy.KeepyUppy.security.oauth;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuth2Provider {
    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;
    private final String tokenUrl;
    private final String userInfoUrl;
    private final String[] scope;


    @Builder
    public OAuth2Provider(String clientId, String clientSecret, String redirectUri, String tokenUrl, String userInfoUrl, String[] scope) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.tokenUrl = tokenUrl;
        this.userInfoUrl = userInfoUrl;
        this.scope = scope;
    }
}
