package com.keepyuppy.KeepyUppy.post.communication.response;

import com.keepyuppy.KeepyUppy.member.communication.response.MemberResponse;
import com.keepyuppy.KeepyUppy.post.domain.entity.Announcement;
import com.keepyuppy.KeepyUppy.post.domain.entity.Post;
import com.keepyuppy.KeepyUppy.post.domain.enums.ContentType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(name = "공지글 정보 응답")
@AllArgsConstructor
public class AnnouncementResponse {

    private Long id;
    private String title;
    private MemberResponse author;
    private String content;
    private Boolean isAnnouncement;
    private LocalDateTime createdDate;
    private boolean isEdited;
    private boolean pinned;

    public static AnnouncementResponse of(Announcement announcement){
        return new AnnouncementResponse(
                announcement.getId(),
                announcement.getTitle(),
                MemberResponse.of(announcement.getAuthor()),
                announcement.getContent(),
                announcement.getType() == ContentType.ANNOUNCEMENT,
                announcement.getCreatedDate(),
                announcement.getModifiedDate().isAfter(announcement.getCreatedDate().plusMinutes(1)),
                announcement.isPinned()
        );
    }
}
