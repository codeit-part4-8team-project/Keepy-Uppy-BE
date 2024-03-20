package com.keepyuppy.KeepyUppy.global.exception;

public class AccessDeniedException extends RuntimeException{
    public AccessDeniedException(String message) {
        super(message);
    }

    public static class TeamAccessDeniedException extends  AccessDeniedException {
        public TeamAccessDeniedException() {
            super("존재하지 않거나 속하지 않은 팀입니다.");
        }
    }

    public static class ActionAccessDeniedException extends  AccessDeniedException {
        public ActionAccessDeniedException() {
            super("이 작업을 수행할 수 있는 권한이 없습니다.");
        }
    }
}
