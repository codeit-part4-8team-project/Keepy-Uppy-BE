package com.keepyuppy.KeepyUppy.user.domain.entity;

import com.keepyuppy.KeepyUppy.global.domain.BaseTimeEntity;
import com.keepyuppy.KeepyUppy.member.domain.entity.Member;
import com.keepyuppy.KeepyUppy.user.communication.request.UpdateUserRequest;
import com.keepyuppy.KeepyUppy.user.domain.enums.Provider;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
@ToString
public class Users extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String imageUrl;

    private String username;

    private String oauthId;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    private String bio;

    private String refreshToken;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Member> members = new HashSet<>();

    @Builder
    public Users(String name,
                 String imageUrl,
                 String username,
                 String oauthId,
                 Provider provider,
                 String bio) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.username = username;
        this.oauthId = oauthId;
        this.provider = provider;
        this.bio = bio;
    }

    public void update(UpdateUserRequest request){
        if (request.getName() != null) this.name = request.getName();
        if (request.getImageUrl() != null) this.imageUrl = request.getImageUrl();
        if (request.getUsername() != null) this.username = request.getUsername();
        if (request.getBio() != null) this.bio = request.getBio();
    }

    public void updateRefreshToken(String token){
        this.refreshToken = token;
    }

    public void addMember(Member member) {
        member.setUsers(this);
        this.members.add(member);
    }


    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

