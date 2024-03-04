package com.keepyuppy.KeepyUppy.user.domain.entity;

import com.keepyuppy.KeepyUppy.global.domain.BaseTimeEntity;
import com.keepyuppy.KeepyUppy.user.domain.enums.Provider;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter(AccessLevel.PROTECTED)
@NoArgsConstructor
@ToString
public class Users extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String imageUrl;

    private String oauthId;

    @Enumerated(EnumType.STRING)
    private Provider provider;

//    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
//    private Set<Member> members = new HashSet<>();

    @Builder
    public Users(String name, String imageUrl, String oauthId, Provider provider) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.oauthId = oauthId;
        this.provider = provider;
    }

}
