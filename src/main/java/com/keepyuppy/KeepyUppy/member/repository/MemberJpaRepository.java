package com.keepyuppy.KeepyUppy.member.repository;

import com.keepyuppy.KeepyUppy.member.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<Member,Long> {
}
