package com.keepyuppy.KeepyUppy.post.domain.entity;

import com.keepyuppy.KeepyUppy.post.communication.request.PostRequest;
import com.keepyuppy.KeepyUppy.post.domain.enums.ContentType;
import com.keepyuppy.KeepyUppy.global.domain.BaseTimeEntity;
import com.keepyuppy.KeepyUppy.member.domain.entity.Member;
import com.keepyuppy.KeepyUppy.team.domain.entity.Team;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    private String title;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    private Member author;

    private String content;

    @Enumerated(EnumType.STRING)
    private ContentType type;

    @Builder
    public Post(Team team, String title, Member author, String content, ContentType type) {
        this.team = team;
        this.title = title;
        this.author = author;
        this.content = content;
        this.type = type;
    }

    public void setTeam(Team team){
        this.team = team;
    }

    public void update(PostRequest request){
        if (request.getTitle() != null) this.title = request.getTitle();
        if (request.getContent() != null) this.content = request.getContent();
    }

    public Announcement convertAsAnnouncement(){
        Set<Member> readers = new HashSet<>(Collections.singletonList(author));
        return Announcement.announcementBuilder()
                .title(title)
                .content(content)
                .team(team)
                .author(author)
                .type(ContentType.ANNOUNCEMENT)
                .pinned(false)
                .readers(readers)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post that = (Post) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
