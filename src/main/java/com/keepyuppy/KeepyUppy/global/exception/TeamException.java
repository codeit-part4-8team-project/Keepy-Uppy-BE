package com.keepyuppy.KeepyUppy.global.exception;

public class TeamException extends RuntimeException{
    public TeamException(String message) {
        super(message);
    }

    public static class TeamNotFoundException extends TeamException {

        public TeamNotFoundException() {
            super(ExceptionMessage.TEAM_NOT_FOUND.getMessage());
        }
    }


}

