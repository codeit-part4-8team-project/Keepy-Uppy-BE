package com.keepyuppy.KeepyUppy.member.communication.request;

import lombok.Data;

@Data
public class AddMemberRequest {
    private Long userId;
    private String grade;
    private String role;
}