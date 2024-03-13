package com.keepyuppy.KeepyUppy.issue.communication.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(name = "이슈 태그 요청")
public class IssueTagRequest {

    private String name;
    private String color;

}
