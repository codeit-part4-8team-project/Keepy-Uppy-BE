package com.keepyuppy.KeepyUppy.member.domain.enums;

import lombok.Getter;

import java.util.Arrays;


@Getter
public enum Grade {
    OWNER("소유자"),
    MANAGER("매니저"),
    TEAM_MEMBER("팀원");

    private final String name;

    Grade(String name) {
        this.name = name;
    }

    public static Grade getInstance(String grade) {
        return Arrays.stream(Grade.values()).filter(g -> g.getName().equals(grade))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("등급이 존재하지 않습니다."));
    }
}
