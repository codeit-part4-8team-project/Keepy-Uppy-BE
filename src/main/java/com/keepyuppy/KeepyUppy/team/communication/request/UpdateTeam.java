package com.keepyuppy.KeepyUppy.team.communication.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(name = "팀 정보 수정 요청")
public class UpdateTeam {
    private String name;
    private String description;
    private String color;
    private LocalDate startDate;
    private LocalDate endDate;
    private String figma;
    private String github;
    private String discord;
}
