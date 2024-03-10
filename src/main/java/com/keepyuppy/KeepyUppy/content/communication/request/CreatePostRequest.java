package com.keepyuppy.KeepyUppy.content.communication.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(name = "게시글, 공지 생성 요청")
public class CreatePostRequest {

    private String title;
    private String content;
    private boolean isAnnouncement;
}
