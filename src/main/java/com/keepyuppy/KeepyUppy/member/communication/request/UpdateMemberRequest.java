package com.keepyuppy.KeepyUppy.member.communication.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "멤버 수정 요청")
public class UpdateMemberRequest {
    private Long memberId;
    private String grade;
    private String role;
}

