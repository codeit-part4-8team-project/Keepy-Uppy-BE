package com.keepyuppy.KeepyUppy.post.repository;

import com.keepyuppy.KeepyUppy.post.domain.entity.Announcement;
import com.keepyuppy.KeepyUppy.team.domain.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface AnnouncementJPARepository extends JpaRepository<Announcement,Long> {

    Set<Announcement> findByTeamAndPinned(Team team, boolean pinned);
}
