package com.keepyuppy.KeepyUppy.post.domain.entity;

import com.keepyuppy.KeepyUppy.member.domain.entity.Member;
import com.keepyuppy.KeepyUppy.post.domain.enums.ContentType;
import com.keepyuppy.KeepyUppy.team.domain.entity.Team;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.joda.time.LocalDateTime;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class Announcement extends Post {

    private boolean pinned = false;

    @ManyToMany
    private Set<Member> readers = new HashSet<>();

    @Builder(builderMethodName = "announcementBuilder")
    public Announcement(Team team, String title, Member author, String content, ContentType type,
                        Post announcement, Set<Member> readers, boolean pinned) {
        super(team, title, author, content, type);
        this.pinned = pinned;
        this.readers = readers;
    }

    public void setPinned(boolean pinned){
        this.pinned = pinned;
    }

    public void addReader(Member reader){
        this.readers.add(reader);
    }

    public Post convertAsPost(){
        return Post.builder()
                .title(super.getTitle())
                .content(super.getContent())
                .team(super.getTeam())
                .author(super.getAuthor())
                .type(ContentType.POST)
                .build();
    }
}
