package com.keepyuppy.KeepyUppy.content.domain.entity;

import com.keepyuppy.KeepyUppy.content.domain.enums.ContentType;
import com.keepyuppy.KeepyUppy.global.domain.BaseTimeEntity;
import com.keepyuppy.KeepyUppy.member.domain.entity.Member;
import com.keepyuppy.KeepyUppy.team.domain.entity.Team;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    private String title;

    @ManyToOne
    private Member author;

    // Class 이름과 같아서 변경하는게 좋겠다고 알림이 옵니다!
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
}