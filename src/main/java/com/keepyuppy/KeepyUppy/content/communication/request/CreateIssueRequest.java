package com.keepyuppy.KeepyUppy.content.communication.request;

import com.keepyuppy.KeepyUppy.content.domain.enums.IssueStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Schema(name = "이슈 생성 요청")
public class CreateIssueRequest {

    private String title;
    private String content;
    private LocalDateTime dueDate;
    private IssueStatus status;
    private List<String> assignedMembersUsernames;

}