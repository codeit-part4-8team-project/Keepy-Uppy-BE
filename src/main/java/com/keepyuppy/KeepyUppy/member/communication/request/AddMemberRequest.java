package com.keepyuppy.KeepyUppy.member.communication.request;

import lombok.Data;

@Data
public class AddMemberRequest {
    private Long usersId;
    private String grade;
}
