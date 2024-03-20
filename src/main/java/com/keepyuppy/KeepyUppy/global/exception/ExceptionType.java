package com.keepyuppy.KeepyUppy.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExceptionType {

    //401 UNAUTHORIZED
    UNAUTHORIZED(401, "잘못된 토큰입니다."),

    //403 FORBIDDEN
    ACTION_ACCESS_DENIED(403, "이 작업을 수행할 수 있는 권한이 없습니다."),
    TEAM_ACCESS_DENIED(403, "존재하지 않는 팀이거나 속하지 않은 팀입니다."),

    //404 NOT_FOUND
    USER_NOT_FOUND(404, "유저를 찾을 수 없습니다."),
    MEMBER_NOT_FOUND(404, "멤버를 찾을 수 없습니다."),
    TEAM_NOT_FOUND(404, "팀을 찾을 수 없습니다."),
    ISSUE_NOT_FOUND(404, "이슈를 찾을 수 없습니다."),
    POST_NOT_FOUND(404, "게시글을 찾을 수 없습니다."),
    SCHEDULE_NOT_FOUND(404, "일정을 찾을 수 없습니다."),
    ANNOUNCEMENT_NOT_FOUND(404, "공지글을 찾을 수 없습니다."),

    //409 CONFLICT
    MEMBER_ALREADY_EXISTS(409, "이미 팀에 속해있는 유저입니다."),

    //500 INTERNAL SERVER ERROR
    PICTURE_UPLOAD_FAIL(500, "사진 업로드에 실패하였습니다."),
    MEMBER_DELETE_FAIL(500, "팀 멤버 삭제에 실패하였습니다");


    private final int status;
    private final String message;
}
