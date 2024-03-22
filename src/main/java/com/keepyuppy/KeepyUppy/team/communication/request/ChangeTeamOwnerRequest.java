package com.keepyuppy.KeepyUppy.team.communication.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(name = "팀 소유자 변경 요청")
public class ChangeTeamOwnerRequest {
    private String username;
}
