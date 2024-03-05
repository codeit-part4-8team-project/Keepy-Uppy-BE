package com.keepyuppy.KeepyUppy.member.domain.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Role {
    BACKEND("백엔드"),
    FRONTEND("프론트엔드"),
    DESIGNER("디자이너");


    private final String name;

    Role(String name) {
        this.name = name;
    }

    public static Role getInstance(String role) {
        return Arrays.stream(Role.values()).filter(r -> r.getName().equals(role)).findFirst().orElseThrow(() -> new IllegalArgumentException("역할이 존재하지 않습니다."));
    }
}
