package com.keepyuppy.KeepyUppy.team.domain.enums;

import java.util.Arrays;

public enum Type {
    PROJECT("project"),
    STUDY("study");

    private final String name;

    Type(String name) {
        this.name = name;
    }

    public static Type getInstance(String type) {
        return Arrays.stream(Type.values()).filter(t -> t.name.equals(type)).findFirst().orElseThrow(() ->new IllegalArgumentException(type + " 을 찾을수 없습니다."));
    }
}
