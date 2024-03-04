package com.keepyuppy.KeepyUppy.user.domain.entity;

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
public class Users {
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
