package com.keepyuppy.KeepyUppy.content.domain.entity;

import com.keepyuppy.KeepyUppy.content.domain.enums.ContentType;
import com.keepyuppy.KeepyUppy.member.domain.entity.Member;
import com.keepyuppy.KeepyUppy.team.domain.entity.Team;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Schedule extends Content {

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Builder
    public Schedule(Team team, String title, Member author, String content, ContentType type,
                 LocalDateTime startDate, LocalDateTime endDate) {
        super(team, title, author, content, type);
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
