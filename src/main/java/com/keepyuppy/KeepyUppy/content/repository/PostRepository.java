package com.keepyuppy.KeepyUppy.content.repository;

import com.keepyuppy.KeepyUppy.content.domain.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Content,Long>{
}