package com.keepyuppy.KeepyUppy.team.communication.response;

import com.keepyuppy.KeepyUppy.member.communication.response.MemberResponse;
import com.keepyuppy.KeepyUppy.member.domain.enums.Status;
import com.keepyuppy.KeepyUppy.team.domain.entity.Team;
import lombok.Data;

import java.util.List;

@Data
public class TeamResponse {
    private Long id;
    private String name;
    private String description;
    private List<MemberResponse> members;

    public TeamResponse(Team team) {
        this.id = team.getId();
        this.name = team.getName();
        this.description = team.getDescription();
        this.members = team.getMembers().isEmpty() ? null : team.getMembers().stream().filter(member -> member.getStatus().equals(Status.ACCEPTED)).map(MemberResponse::new).toList();
    }
}
