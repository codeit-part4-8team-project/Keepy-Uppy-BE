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
    private String provider;
    private String oauthId;
    private String name;
    private String imageUrl;

    private final String nameAttributeKey = "oauthId";

    public static OAuth2Attributes of(Provider provider, Map<String, Object> attributes) {
        return switch (provider) {
            case GOOGLE -> ofGoogle(attributes);
            case KAKAO -> ofKakao(attributes);
            case GITHUB -> ofGithub(attributes);
        };
    }

    private static OAuth2Attributes ofGoogle(Map<String, Object> attributes) {
        return OAuth2Attributes.builder()
                .provider("GOOGLE")
                .oauthId(String.valueOf(attributes.get("sub")))
                .name((String) attributes.get("name"))
                .imageUrl((String) attributes.get("picture"))
                .attributes(attributes)
                .build();
    }

    private static OAuth2Attributes ofKakao(Map<String, Object> attributes) {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");

        return OAuth2Attributes.builder()
                .provider("KAKAO")
                .oauthId(String.valueOf(attributes.get("id")))
                .name((String) profile.get("nickname"))
                .imageUrl((String) properties.get("profile_image"))
                .attributes(account)
                .build();
    }

    private static OAuth2Attributes ofGithub(Map<String, Object> attributes) {
        return OAuth2Attributes.builder()
                .provider("GITHUB")
                .oauthId(String.valueOf(attributes.get("id")))
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
        map.put("name", name);
        map.put("imageUrl", imageUrl);

        return map;
    }

}
