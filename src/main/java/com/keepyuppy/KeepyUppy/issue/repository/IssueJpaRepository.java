package com.keepyuppy.KeepyUppy.issue.repository;

import com.keepyuppy.KeepyUppy.issue.domain.entity.Issue;
import com.keepyuppy.KeepyUppy.issue.domain.enums.IssueStatus;
import com.keepyuppy.KeepyUppy.team.domain.entity.Team;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface IssueJpaRepository extends JpaRepository<Issue,Long>{
}