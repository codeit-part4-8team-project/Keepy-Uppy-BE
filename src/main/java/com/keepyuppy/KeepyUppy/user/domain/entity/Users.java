package com.keepyuppy.KeepyUppy.user.domain.entity;

import com.keepyuppy.KeepyUppy.global.domain.BaseTimeEntity;
import com.keepyuppy.KeepyUppy.user.domain.enums.Provider;
import com.keepyuppy.KeepyUppy.user.domain.enums.Role;
import jakarta.persistence.*;
import lombok.*;

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

    private String email;

    private String oauthId;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String bio;

    private String refreshToken;

//    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
//    private Set<Member> members = new HashSet<>();

    @Builder
    public Users(String name,
                 String imageUrl,
                 String email,
                 String oauthId,
                 Provider provider,
                 Role role,
                 String bio) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.email = email;
        this.oauthId = oauthId;
        this.provider = provider;
        this.role = role;
        this.bio = bio;
    }

    public void updateRefreshToken(String token){
        this.refreshToken = token;
    }

}
