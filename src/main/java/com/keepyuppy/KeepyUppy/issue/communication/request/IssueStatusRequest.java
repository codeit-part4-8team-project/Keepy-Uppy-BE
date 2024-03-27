package com.keepyuppy.KeepyUppy.issue.communication.request;

import com.keepyuppy.KeepyUppy.issue.domain.enums.IssueStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "이슈 진행상황 수정 요청")
public class IssueStatusRequest {
    private IssueStatus status;
}
