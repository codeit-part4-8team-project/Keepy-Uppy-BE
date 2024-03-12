package com.keepyuppy.KeepyUppy.member.communication.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "멤버 추가 요청")
public class AddMemberRequest {
    private String userName;
}