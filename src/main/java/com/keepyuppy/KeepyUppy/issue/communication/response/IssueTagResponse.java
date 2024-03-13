package com.keepyuppy.KeepyUppy.issue.communication.response;

import com.keepyuppy.KeepyUppy.issue.domain.entity.IssueTag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Schema(name = "이슈 태그 응답")
@AllArgsConstructor
public class IssueTagResponse {
    private String name;
    private String color;

    public static IssueTagResponse of(IssueTag tag){
        return new IssueTagResponse(tag.getName(), tag.getColor());
    }
}
