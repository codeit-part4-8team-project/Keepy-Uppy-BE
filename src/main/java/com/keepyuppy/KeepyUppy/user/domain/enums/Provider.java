package com.keepyuppy.KeepyUppy.user.domain.enums;

public enum Provider {
    GOOGLE,
    KAKAO,
    GITHUB;

    public static Provider of(String type) {
        return Provider.valueOf(type.toUpperCase());
    }
}