package com.keepyuppy.KeepyUppy.post.domain.entity;

import com.keepyuppy.KeepyUppy.member.domain.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ReadAnnouncement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
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
