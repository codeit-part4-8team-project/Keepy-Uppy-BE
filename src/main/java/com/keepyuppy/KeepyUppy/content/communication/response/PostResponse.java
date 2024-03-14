package com.keepyuppy.KeepyUppy.content.communication.response;

import com.keepyuppy.KeepyUppy.content.domain.entity.Post;
import com.keepyuppy.KeepyUppy.content.domain.enums.ContentType;
import com.keepyuppy.KeepyUppy.member.communication.response.MemberResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Schema(name = "게시글, 공지 정보 응답")
@AllArgsConstructor
public class PostResponse {

    private Long id;
    private String title;
    private MemberResponse author;
    private String content;
    private boolean isAnnouncement;

    public static PostResponse of(Post post){
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                MemberResponse.of(post.getAuthor()),
                post.getContent(),
                post.getType() == ContentType.ANNOUNCEMENT
        );
    }
}
