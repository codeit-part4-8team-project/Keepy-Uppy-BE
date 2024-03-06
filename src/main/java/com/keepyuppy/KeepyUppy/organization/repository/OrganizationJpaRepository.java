package com.keepyuppy.KeepyUppy.organization.repository;

import com.keepyuppy.KeepyUppy.organization.domain.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationJpaRepository extends JpaRepository<Organization,Long> {
    
}
