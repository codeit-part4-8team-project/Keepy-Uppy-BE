package com.keepyuppy.KeepyUppy.team.communication.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "팀 정보 수정 요청")
public class UpdateTeam {
    private String name;
    private String description;
    private String color;
    private String startDate;
    private String endDate;
    private String figma;
    private String github;
    private String discord;
}
