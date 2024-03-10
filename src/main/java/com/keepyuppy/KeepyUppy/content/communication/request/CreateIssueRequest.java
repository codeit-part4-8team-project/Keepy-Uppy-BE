package com.keepyuppy.KeepyUppy.content.communication.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Schema(name = "이슈 생성 요청")
public class CreateIssueRequest extends CreatePostRequest {

    private LocalDateTime dueDate;
    private String status;
    private List<String> assignedMembers;

}
