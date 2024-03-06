package com.keepyuppy.KeepyUppy.organization.communication.response;

import com.keepyuppy.KeepyUppy.member.communication.response.MemberResponse;
import com.keepyuppy.KeepyUppy.member.domain.enums.Status;
import com.keepyuppy.KeepyUppy.organization.domain.entity.Organization;
import lombok.Data;

import java.util.List;

@Data
public class TeamResponse {
    private Long id;
    private String name;
    private String description;
    private List<MemberResponse> members;

    public TeamResponse(Organization organization) {
        this.id = organization.getId();
        this.name = organization.getName();
        this.description = organization.getDescription();
        this.members = organization.getMembers().isEmpty() ? null : organization.getMembers().stream().filter(member -> member.getStatus().equals(Status.ACCEPTED)).map(MemberResponse::new).toList();
    }
}
