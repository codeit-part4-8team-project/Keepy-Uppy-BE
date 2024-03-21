package com.keepyuppy.KeepyUppy.team.communication.response;

import com.keepyuppy.KeepyUppy.team.domain.entity.Team;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "멤버정보 제외한 팀 정보")
public class TeamWithoutMemberResponse {
    private Long id;
    private String name;
    private String description;
    private String color;

    private TeamWithoutMemberResponse(Team team) {
        this.id = team.getId();
        this.name = team.getName();
        this.description = team.getDescription();
        this.color = team.getColor();
    }

    public static TeamWithoutMemberResponse of(Team team) {
        return new TeamWithoutMemberResponse(team);
    }
}
