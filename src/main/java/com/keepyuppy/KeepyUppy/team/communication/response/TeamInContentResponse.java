package com.keepyuppy.KeepyUppy.team.communication.response;

import com.keepyuppy.KeepyUppy.team.domain.entity.Team;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "팀 스케쥴 팀 정보")
public class TeamInContentResponse {
    private Long id;
    private String name;
    private String description;
    private String color;

    private TeamInContentResponse(Long id, String name, String description, String color) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.color = color;
    }

    public static TeamInContentResponse of(Team team) {
        return new TeamInContentResponse(team.getId(), team.getName(), team.getDescription(), team.getColor());
    }
}
