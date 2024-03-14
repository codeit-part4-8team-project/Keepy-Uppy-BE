package com.keepyuppy.KeepyUppy.global.exception;

public class MemberException extends RuntimeException{
    public MemberException(String message) {
        super(message);
    }

    public static class MemberNotFoundException extends MemberException {
        public MemberNotFoundException() {
            super(ExceptionMessage.MEMBER_NOT_FOUND.getMessage());
        }
    }

    public static class MemberAlreadyExistException extends MemberException {

        public MemberAlreadyExistException() {
            super(ExceptionMessage.MEMBER_ALREADY_EXIST.getMessage());
        }
    }

    public static class RemoveMemberFailException extends MemberException {

        public RemoveMemberFailException() {
            super(ExceptionMessage.MEMBER_REMOVE_FAIL.getMessage());
        }
    }
}

