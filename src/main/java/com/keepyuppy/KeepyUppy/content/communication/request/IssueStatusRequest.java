package com.keepyuppy.KeepyUppy.content.communication.request;

import com.keepyuppy.KeepyUppy.content.domain.enums.IssueStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(name = "이슈 진행상황 수정 요청")
public class IssueStatusRequest {
    private IssueStatus status;
}
