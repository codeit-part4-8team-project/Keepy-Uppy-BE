package com.keepyuppy.KeepyUppy.content.repository;

import com.keepyuppy.KeepyUppy.content.domain.entity.Issue;
import com.keepyuppy.KeepyUppy.content.domain.enums.IssueStatus;
import com.keepyuppy.KeepyUppy.team.domain.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueJpaRepository extends JpaRepository<Issue,Long>{

    List<Issue> findByTeamAndStatusOrderByModifiedDateAsc(Team team, IssueStatus status);

}