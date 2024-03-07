package com.keepyuppy.KeepyUppy.schedule.domain.entity;

import com.keepyuppy.KeepyUppy.team.domain.entity.Team;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Team team;

    public void setOrganization(Team team) {
        this.team = team;
    }
}
