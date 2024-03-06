package com.keepyuppy.KeepyUppy.security.oauth;

import com.keepyuppy.KeepyUppy.user.domain.enums.Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOidcUserService extends OidcUserService {

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);

        Provider provider = Provider.valueOf(userRequest.getClientRegistration().getClientName().toUpperCase());
        OAuth2Attributes attributes = OAuth2Attributes.of(provider, oidcUser.getAttributes());
        Map<String, Object> attributesMap = attributes.convertToMap();

        return new DefaultOidcUser(Collections.emptyList(), userRequest.getIdToken(),
                new OidcUserInfo(attributesMap), attributes.getNameAttributeKey());
    }
}
