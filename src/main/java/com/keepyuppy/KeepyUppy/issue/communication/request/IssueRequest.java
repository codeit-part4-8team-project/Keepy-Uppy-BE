package com.keepyuppy.KeepyUppy.issue.communication.request;

import com.keepyuppy.KeepyUppy.issue.domain.enums.IssueStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@Schema(name = "이슈 생성 요청")
public class IssueRequest {

    private String title;
    private String content;
    private LocalDate dueDate;
    private IssueStatus status;
    private List<String> assignedMembersUsernames;

}
