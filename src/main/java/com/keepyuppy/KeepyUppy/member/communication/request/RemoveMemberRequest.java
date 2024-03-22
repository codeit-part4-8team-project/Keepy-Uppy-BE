package com.keepyuppy.KeepyUppy.member.communication.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "멤버 삭제 요청")
public class RemoveMemberRequest {
    private String membername;
}
