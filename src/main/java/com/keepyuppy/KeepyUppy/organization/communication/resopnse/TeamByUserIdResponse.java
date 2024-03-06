package com.keepyuppy.KeepyUppy.organization.communication.resopnse;


import com.keepyuppy.KeepyUppy.organization.domain.entity.Organization;
import lombok.Data;

@Data
public class TeamByUserIdResponse {
    private String name;
    private String description;

    public TeamByUserIdResponse(Organization organization) {
        this.name = organization.getName();
        this.description = organization.getDescription();
    }
}
