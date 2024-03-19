package com.keepyuppy.KeepyUppy.post.repository;

import com.keepyuppy.KeepyUppy.member.domain.entity.QMember;
import com.keepyuppy.KeepyUppy.post.domain.entity.Announcement;
import com.keepyuppy.KeepyUppy.user.domain.entity.QUsers;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.keepyuppy.KeepyUppy.member.domain.entity.QMember.member;
import static com.keepyuppy.KeepyUppy.post.domain.entity.QAnnouncement.announcement;
import static com.keepyuppy.KeepyUppy.team.domain.entity.QTeam.team;
import static com.keepyuppy.KeepyUppy.user.domain.entity.QUsers.users;

@Repository
@RequiredArgsConstructor
public class AnnouncementRepositoryImpl {

    private final JPAQueryFactory jpaQueryFactory;

    public List<Announcement> findUnreadByUserId(Long userId) {
        return jpaQueryFactory.selectFrom(announcement)
                .where(announcement.team.members.any().user.id.eq(userId)
                    .and((announcement.readers.any().user.id.eq(userId)).not()))
                .orderBy(announcement.createdDate.desc())
                .fetch();
    }

    public Page<Announcement> findByTeam(Long teamId, Pageable pageable) {
        List<Announcement> content = jpaQueryFactory.selectFrom(announcement)
                .where(announcement.team.id.eq(teamId))
                .orderBy(announcement.pinned.desc(), announcement.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory.select(announcement.count())
                .from(announcement)
                .where(announcement.team.id.eq(teamId))
                .fetchOne();
        total = total == null ? 0 : total;

        return new PageImpl<>(content, pageable, total);
    }

}
