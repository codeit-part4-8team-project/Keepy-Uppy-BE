package com.keepyuppy.KeepyUppy.post.domain.entity;

import com.keepyuppy.KeepyUppy.post.domain.enums.ContentType;
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
public class Schedule extends Post {

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    @Builder(builderMethodName = "scheduleBuilder")
    public Schedule(Team team, String title, Member author, String content, ContentType type,
                    LocalDateTime startDateTime, LocalDateTime endDateTime) {
        super(team, title, author, content, type);
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

}
