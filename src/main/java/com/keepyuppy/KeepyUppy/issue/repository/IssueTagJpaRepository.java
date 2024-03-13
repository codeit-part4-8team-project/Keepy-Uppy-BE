package com.keepyuppy.KeepyUppy.issue.repository;

import com.keepyuppy.KeepyUppy.issue.domain.entity.IssueTag;
import com.keepyuppy.KeepyUppy.team.domain.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IssueTagJpaRepository extends JpaRepository<IssueTag,Long>{

    public List<IssueTag> findByTeam(Team team);

    public Optional<IssueTag> findByName(String string);

}