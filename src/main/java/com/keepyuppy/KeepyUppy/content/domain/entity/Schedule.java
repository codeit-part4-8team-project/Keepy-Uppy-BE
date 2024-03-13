package com.keepyuppy.KeepyUppy.content.domain.entity;

import com.keepyuppy.KeepyUppy.content.domain.enums.ContentType;
import com.keepyuppy.KeepyUppy.member.domain.entity.Member;
import com.keepyuppy.KeepyUppy.team.domain.entity.Team;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class Schedule extends Post {

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    @OneToMany(mappedBy = "schedule")
    private Set<UserSchedule> userSchedules = new HashSet<>();

    @Builder(builderMethodName = "scheduleBuilder")
    public Schedule(Team team, String title, Member author, String content, ContentType type,
                    LocalDateTime startDateTime, LocalDateTime endDateTime) {
        super(team, title, author, content, type);
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

}

