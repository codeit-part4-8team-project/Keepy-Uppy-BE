package com.keepyuppy.KeepyUppy.content.communication.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(name = "이슈 생성 요청")
public class CreateIssueRequest extends CreatePostRequest {
    
    private String dueDate;
    private String status;
}
