package com.keepyuppy.KeepyUppy.team.repository;

import com.keepyuppy.KeepyUppy.team.domain.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamJpaRepository extends JpaRepository<Team,Long> {
    
}
