package com.keepyuppy.KeepyUppy.member.communication.response;

import com.keepyuppy.KeepyUppy.member.domain.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "멤버 정보 응답")
public class MemberResponse {
    private String name;
    private String imageUrl;
    private String role;
    private String grade;
    private String username;

    public MemberResponse(Member member) {
        this.name = member.getUser() == null ? null : member.getUser().getName();
        this.imageUrl = member.getUser() == null ? null : member.getUser().getImageUrl();
        this.role = member.getRole() == null ? null : member.getRole().name();
        this.grade = member.getGrade().name();
        this.username = member.getUser() == null ? null : member.getUser().getUsername();
    }

    public static MemberResponse of(Member member) {
        return new MemberResponse(member);
    }
}

