package com.keepyuppy.KeepyUppy.global.exception;

import lombok.Getter;

@Getter
public enum ExceptionMessage {
    MEMBER_NOT_FOUND("멤버를 찾을수 없습니다."),
    MEMBER_ALREADY_EXIST("이미 팀에 존재하는 회원입니다."),
    TEAM_NOT_FOUND("팀을 찾을수 없습니다."),
    USER_NOT_FOUND("유저를 찾을수 없습니다."),
    AUTHORIZED("잘못된 권한 입니다."),
    MEMBER_REMOVE_FAIL("팀 멤버 삭제에 실패하였습니다");

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }
}

