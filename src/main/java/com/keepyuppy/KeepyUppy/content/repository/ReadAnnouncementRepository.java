package com.keepyuppy.KeepyUppy.content.repository;

import com.keepyuppy.KeepyUppy.content.domain.entity.ReadAnnouncement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadAnnouncementRepository extends JpaRepository<ReadAnnouncement,Long> {
}
