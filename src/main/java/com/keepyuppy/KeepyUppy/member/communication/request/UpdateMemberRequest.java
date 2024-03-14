package com.keepyuppy.KeepyUppy.member.communication.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(name = "멤버 수정 요청")
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMemberRequest {
    private String grade;
    private String role;
}

