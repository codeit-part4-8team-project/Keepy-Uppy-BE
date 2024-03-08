package com.keepyuppy.KeepyUppy.member.communication.request;

import lombok.Data;

@Data
public class UpdateMemberRequest {
    private Long memberId;
    private String grade;
    private String role;
}

