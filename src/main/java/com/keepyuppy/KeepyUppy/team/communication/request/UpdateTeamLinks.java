package com.keepyuppy.KeepyUppy.team.communication.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "팀 링크 수정 요청")
public class UpdateTeamLinks {
    private String figma;
    private String github;
    private String discord;
}
