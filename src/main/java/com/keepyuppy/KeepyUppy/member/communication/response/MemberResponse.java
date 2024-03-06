package com.keepyuppy.KeepyUppy.member.communication.response;

import com.keepyuppy.KeepyUppy.member.domain.entity.Member;
import lombok.Data;

@Data
public class MemberResponse {
    private String name;
    private String role;
    private String grade;

    public MemberResponse(Member member) {
        this.name = member.getUser().getName();
        this.role = member.getRole().name();
        this.grade = member.getGrade().name();
    }
}
