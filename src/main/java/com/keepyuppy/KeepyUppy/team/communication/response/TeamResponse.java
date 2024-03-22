package com.keepyuppy.KeepyUppy.team.communication.response;

import com.keepyuppy.KeepyUppy.member.communication.response.MemberResponse;
import com.keepyuppy.KeepyUppy.member.domain.enums.Status;
import com.keepyuppy.KeepyUppy.team.domain.entity.Team;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(name = "팀 정보 응답")
public class TeamResponse {
    private Long id;
    private String name;
    private String description;
    private String color;
    private List<MemberResponse> members;

    public TeamResponse(Team team) {
        this.id = team.getId();
        this.name = team.getName();
        this.description = team.getDescription();
        this.color = team.getColor();
        this.members = team.getMembers() == null ? null : team.getMembers().stream().filter(member -> member.getStatus().equals(Status.ACCEPTED)).map(MemberResponse::new).toList();
    }

    public static TeamResponse of(Team team) {
        return new TeamResponse(team);
    }
}


