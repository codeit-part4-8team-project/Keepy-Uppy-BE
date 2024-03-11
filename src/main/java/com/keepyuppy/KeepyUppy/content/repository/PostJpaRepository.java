package com.keepyuppy.KeepyUppy.content.repository;

import com.keepyuppy.KeepyUppy.content.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostJpaRepository extends JpaRepository<Post,Long>{
}