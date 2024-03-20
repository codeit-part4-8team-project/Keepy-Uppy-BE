package com.keepyuppy.KeepyUppy.global.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final ExceptionType type;

    public CustomException(ExceptionType message){
        super(message.getMessage());
        this.type = message;
    }
}
