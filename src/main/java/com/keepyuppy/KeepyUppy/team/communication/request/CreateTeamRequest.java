package com.keepyuppy.KeepyUppy.team.communication.request;

import lombok.Data;

@Data
public class CreateTeamRequest {
    private String name;
    private String description;
    private String startDate;
    private String endDate;
    private String figmaLink = "https://www.figma.com/";
    private String githubLink = "https://github.com/";
    private String discordLink = "https://discord.com/";

    public CreateTeamRequest(String name, String description, String startDate, String endDate) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public CreateTeamRequest(String name, String description, String startDate, String endDate, String figmaLink, String githubLink, String discordLink) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.figmaLink = figmaLink;
        this.githubLink = githubLink;
        this.discordLink = discordLink;
    }
}