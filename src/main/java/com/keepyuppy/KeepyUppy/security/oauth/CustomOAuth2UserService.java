package com.keepyuppy.KeepyUppy.security.oauth;

import com.keepyuppy.KeepyUppy.user.domain.enums.Provider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        Provider provider = Provider.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());
        OAuth2Attributes attributes = OAuth2Attributes.of(provider, oAuth2User.getAttributes());
        Map<String, Object> attributesMap = attributes.convertToMap();

        return new DefaultOAuth2User(
                Collections.emptyList(),
                attributesMap, attributes.getNameAttributeKey());
    }
}
