package com.keepyuppy.KeepyUppy.team.communication.resopnse;

import com.keepyuppy.KeepyUppy.member.communication.resopnse.MemberResponse;
import com.keepyuppy.KeepyUppy.team.domain.entity.Team;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

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
        this.members = team.getMembers().isEmpty() ? null : team.getMembers().stream().map(MemberResponse::new).collect(Collectors.toList());
    }
}
