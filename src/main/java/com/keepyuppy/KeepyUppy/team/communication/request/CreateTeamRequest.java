package com.keepyuppy.KeepyUppy.team.communication.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Schema(name = "팀 생성 요청")
@AllArgsConstructor
public class CreateTeamRequest {
    private String name;
    private String description;
    private String color;
    private String startDate;
    private String endDate;
    private List<String> members;
    private String figmaLink;
    private String githubLink;
    private String discordLink;
}

