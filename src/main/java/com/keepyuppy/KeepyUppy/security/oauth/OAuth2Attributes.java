package com.keepyuppy.KeepyUppy.security.oauth;

import com.keepyuppy.KeepyUppy.user.domain.enums.Provider;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Builder(access = AccessLevel.PRIVATE)
@Getter
public class OAuth2Attributes {

    private Map<String, Object> attributes;
    private Provider provider;
    private String oauthId;
    private String email;
    private String name;
    private String imageUrl;

    private final String nameAttributeKey = "oauthId";

    public static OAuth2Attributes of(Provider provider, Map<String, Object> attributes) {
        return switch (provider) {
            case GOOGLE -> ofGoogle(provider, attributes);
            case KAKAO -> ofKakao(provider, attributes);
            case GITHUB -> ofGithub(provider, attributes);
        };
    }

    private static OAuth2Attributes ofGoogle(Provider provider, Map<String, Object> attributes) {
        return OAuth2Attributes.builder()
                .provider(provider)
                .oauthId((String) attributes.get("sub"))
                .email((String) attributes.get("email"))
                .name((String) attributes.get("name"))
                .imageUrl((String) attributes.get("picture"))
                .attributes(attributes)
                .build();
    }

    private static OAuth2Attributes ofKakao(Provider provider, Map<String, Object> attributes) {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        // TODO: Check if image works
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        String altImageUrl = (String) properties.get("profile_image");

        return OAuth2Attributes.builder()
                .provider(provider)
                .oauthId((String) attributes.get("id"))
                .email((String) account.get("email"))
                .name((String) profile.get("nickname"))
                .imageUrl((String) profile.get("thumbnail_image_url"))
                .attributes(account)
                .build();
    }

    private static OAuth2Attributes ofGithub(Provider provider, Map<String, Object> attributes) {
        return OAuth2Attributes.builder()
                .provider(provider)
                .oauthId((String) attributes.get("id"))
                .email((String) attributes.get("email"))
                .name((String) attributes.get("name"))
                .imageUrl((String) attributes.get("avatar_url"))
                .attributes(attributes)
                .build();
    }

    Map<String, Object> convertToMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", nameAttributeKey);
        map.put("key", nameAttributeKey);
        map.put("provider", provider);
        map.put(nameAttributeKey, oauthId);
        map.put("email", email);
        map.put("name", name);
        map.put("imageUrl", imageUrl);

        return map;
    }



}
