package com.keepyuppy.KeepyUppy.user.communication.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "회원 정보 업데이트 요청")
public class UpdateUserRequest {
    private String name;
    private String username;
    private String bio;
}
