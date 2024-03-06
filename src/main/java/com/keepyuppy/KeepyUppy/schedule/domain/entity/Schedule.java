package com.keepyuppy.KeepyUppy.schedule.domain.entity;

import com.keepyuppy.KeepyUppy.organization.domain.entity.Organization;
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
    private Organization organization;

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
}
