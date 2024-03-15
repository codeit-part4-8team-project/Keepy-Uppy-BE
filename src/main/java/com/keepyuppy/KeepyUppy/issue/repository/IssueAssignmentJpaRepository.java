package com.keepyuppy.KeepyUppy.issue.repository;

import com.keepyuppy.KeepyUppy.issue.domain.entity.IssueAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueAssignmentJpaRepository extends JpaRepository<IssueAssignment,Long>{
}