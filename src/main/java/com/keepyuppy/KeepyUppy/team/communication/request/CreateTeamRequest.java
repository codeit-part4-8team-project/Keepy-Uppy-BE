package com.keepyuppy.KeepyUppy.team.communication.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateTeamRequest {
    private String type;
    private String name;
    private String role;
    private String description;
    private String startDate;
    private String endDate;
    private String figmaLink = "https://www.figma.com/";
    private String githubLink = "https://github.com/";
    private String discordLink = "https://discord.com/";

    public CreateTeamRequest(String type,String name, String role, String description, String startDate, String endDate) {
        this.type = type;
        this.name = name;
        this.role = role;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public CreateTeamRequest(String type, String name, String role, String description, String startDate, String endDate, String figmaLink, String githubLink, String discordLink) {
        this.type = type;
        this.name = name;
        this.role = role;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.figmaLink = figmaLink;
        this.githubLink = githubLink;
        this.discordLink = discordLink;
    }
}
