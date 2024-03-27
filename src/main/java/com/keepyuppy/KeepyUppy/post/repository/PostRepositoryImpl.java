package com.keepyuppy.KeepyUppy.post.repository;

import com.keepyuppy.KeepyUppy.post.domain.entity.Post;
import com.keepyuppy.KeepyUppy.post.domain.enums.ContentType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.keepyuppy.KeepyUppy.post.domain.entity.QPost.post;
import static com.keepyuppy.KeepyUppy.team.domain.entity.QTeam.team;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl {

    private final JPAQueryFactory jpaQueryFactory;

    public Page<Post> findByUserId(Long userId, ContentType type, Pageable pageable) {
        List<Post> content = jpaQueryFactory.selectFrom(post)
                .innerJoin(post.team, team).fetchJoin()
                .where(post.team.members.any().user.id.eq(userId)
                        .and(post.type.eq(type)))
                .orderBy(post.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory.select(post.count())
                .from(post)
                .where(post.team.members.any().user.id.eq(userId))
                .fetchOne();
        total = total == null ? 0 : total;

        return new PageImpl<>(content, pageable, total);
    }

    public Page<Post> findByUserIdAndTeams(Long userId, List<Long> teamIds, ContentType type, Pageable pageable) {
        List<Post> content = jpaQueryFactory.selectFrom(post)
                .innerJoin(post.team, team).fetchJoin()
                .where(post.team.members.any().user.id.eq(userId)
                        .and(post.type.eq(type))
                        .and(post.team.id.in(teamIds)))
                .orderBy(post.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory.select(post.count())
                .from(post)
                .where(post.team.members.any().user.id.eq(userId))
                .fetchOne();
        total = total == null ? 0 : total;

        return new PageImpl<>(content, pageable, total);
    }

}
