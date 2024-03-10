package com.keepyuppy.KeepyUppy.content.domain.entity;

import com.keepyuppy.KeepyUppy.content.domain.enums.ContentType;
import com.keepyuppy.KeepyUppy.member.domain.entity.Member;
import com.keepyuppy.KeepyUppy.team.domain.entity.Team;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class Announcement extends Post {

    @ElementCollection
    Set<Long> readByMemberId;

    @Builder(builderMethodName = "announcementBuilder")
    public Announcement(Team team, String title, Member author, String content, ContentType type) {
        super(team, title, author, content, type);
        this.readByMemberId = new HashSet<>();
    }

    public void markAsRead(Member member){
        this.readByMemberId.add(member.getId());
    }
}
