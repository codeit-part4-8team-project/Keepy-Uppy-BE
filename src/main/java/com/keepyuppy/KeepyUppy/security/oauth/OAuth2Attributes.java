package com.keepyuppy.KeepyUppy.security.oauth;

import com.keepyuppy.KeepyUppy.user.domain.entity.Users;
import com.keepyuppy.KeepyUppy.user.domain.enums.Provider;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;

@Builder(access = AccessLevel.PRIVATE)
@Getter
public class OAuth2Attributes {

    private Map<String, Object> attributes;
    private Provider provider;
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
                .provider(Provider.GOOGLE)
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
                .provider(Provider.KAKAO)
                .oauthId(String.valueOf(attributes.get("id")))
                .name((String) profile.get("nickname"))
                .imageUrl((String) properties.get("profile_image"))
                .attributes(account)
                .build();
    }

    private static OAuth2Attributes ofGithub(Map<String, Object> attributes) {
        return OAuth2Attributes.builder()
                .provider(Provider.GITHUB)
                .oauthId(String.valueOf(attributes.get("id")))
                .name((String) attributes.get("name"))
                .imageUrl((String) attributes.get("avatar_url"))
                .attributes(attributes)
                .build();
    }

    public Users toUserEntity(){
        return Users.builder()
                .oauthId(this.oauthId)
                .username(generateUsername())
                .provider(this.provider)
                .name(this.name)
                .imageUrl(this.imageUrl)
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
