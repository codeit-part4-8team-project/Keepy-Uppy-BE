package com.keepyuppy.KeepyUppy.post.communication.request;

import com.keepyuppy.KeepyUppy.issue.communication.request.IssueRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Schema(name = "게시글, 공지 생성 요청")
public class PostRequest {

    private String title;
    private String content;
    private Boolean isAnnouncement;

    public static PostRequest ofIssue(IssueRequest request){
        return new PostRequest(request.getTitle(), request.getContent(), false);
    }
}
