package com.keepyuppy.KeepyUppy.post.communication.response;

import com.keepyuppy.KeepyUppy.post.domain.entity.Post;
import com.keepyuppy.KeepyUppy.post.domain.enums.ContentType;
import com.keepyuppy.KeepyUppy.member.communication.response.MemberResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(name = "게시글, 공지 정보 응답")
@AllArgsConstructor
public class PostResponse {

    private Long id;
    private String title;
    private MemberResponse author;
    private String content;
    private Boolean isAnnouncement;
    private LocalDateTime createdDate;
    private boolean isEdited;

    public static PostResponse of(Post post){
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                new MemberResponse(post.getAuthor()),
                post.getContent(),
                post.getType() == ContentType.ANNOUNCEMENT,
                post.getCreatedDate(),
                post.getModifiedDate().isAfter(post.getCreatedDate().plusMinutes(1))
        );
    }
}
