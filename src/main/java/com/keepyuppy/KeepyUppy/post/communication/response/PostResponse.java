package com.keepyuppy.KeepyUppy.post.communication.response;

import com.keepyuppy.KeepyUppy.member.communication.response.MemberResponse;
import com.keepyuppy.KeepyUppy.post.domain.entity.Post;
import com.keepyuppy.KeepyUppy.post.domain.entity.PostLike;
import com.keepyuppy.KeepyUppy.post.domain.enums.ContentType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(name = "게시글 정보 응답")
@AllArgsConstructor
public class PostResponse {

    private Long id;
    private String title;
    private MemberResponse author;
    private String content;
    private LocalDateTime createdDate;
    private boolean isEdited;
    private int likeCount;
    private boolean isLiked;


    public static PostResponse of(Post post){
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                MemberResponse.of(post.getAuthor()),
                post.getContent(),
                post.getCreatedDate(),
                post.getModifiedDate().isAfter(post.getCreatedDate().plusMinutes(1)),
                post.getLikes().size(),
                post.getLikes().stream().map(PostLike::getMember).toList().contains(post.getAuthor())
        );
    }

}
