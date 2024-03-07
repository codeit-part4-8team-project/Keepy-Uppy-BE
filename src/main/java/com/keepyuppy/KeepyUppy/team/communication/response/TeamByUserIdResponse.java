package com.keepyuppy.KeepyUppy.team.communication.response;


import com.keepyuppy.KeepyUppy.team.domain.entity.Team;
import lombok.Data;

@Data
public class TeamByUserIdResponse {
    private String name;
    private String description;

    public TeamByUserIdResponse(Team team) {
        this.name = team.getName();
        this.description = team.getDescription();
    }
}
