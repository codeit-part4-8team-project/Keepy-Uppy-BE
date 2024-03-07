package com.keepyuppy.KeepyUppy.global.exception;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String message) {
        super(message);
    }

    public static class UserNotFoundException extends NotFoundException{
        public UserNotFoundException(String message) {
            super(message);
        }
    }
}
