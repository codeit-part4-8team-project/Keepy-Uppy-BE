package com.keepyuppy.KeepyUppy.user.domain.entity;

import com.keepyuppy.KeepyUppy.global.domain.BaseTimeEntity;
import com.keepyuppy.KeepyUppy.user.domain.enums.Provider;
import jakarta.persistence.*;
import lombok.*;

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

//    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
//    private Set<Member> members = new HashSet<>();

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

    public void updateRefreshToken(String token){
        this.refreshToken = token;
    }

}
