package com.keepyuppy.KeepyUppy.content.domain.entity;

import com.keepyuppy.KeepyUppy.content.domain.enums.ContentType;
import com.keepyuppy.KeepyUppy.member.domain.entity.Member;
import com.keepyuppy.KeepyUppy.team.domain.entity.Team;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class ReadAnnouncement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "announcement_id")
    private Post announcement;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public ReadAnnouncement(Post announcement, Member member) {
        this.announcement = announcement;
        this.member = member;
    }
}
