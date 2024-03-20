package com.keepyuppy.KeepyUppy.global.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super("Invalid token");
    }
}
