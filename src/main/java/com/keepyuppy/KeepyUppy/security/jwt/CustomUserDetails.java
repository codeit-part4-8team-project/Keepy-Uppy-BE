package com.keepyuppy.KeepyUppy.security.jwt;

import com.keepyuppy.KeepyUppy.user.domain.entity.Users;
import com.keepyuppy.KeepyUppy.user.domain.enums.Provider;
import com.keepyuppy.KeepyUppy.user.domain.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@RequiredArgsConstructor
@Getter
@Builder
public class CustomUserDetails implements UserDetails {

    private Map<String, Object> attributes;
    private Provider provider;
    private String oauthId;
    private String email;
    private String name;
    private String imageUrl;
    private Collection<? extends GrantedAuthority> authorities;


    public CustomUserDetails(Users user) {
        this.provider = user.getProvider();
        this.oauthId = user.getOauthId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.imageUrl = user.getImageUrl();
        this.authorities = Collections.singleton(new SimpleGrantedAuthority(user.getRole().getName()));
    }

    public Users toEntity(){
        // TODO: Check emails & profile pics?
        return Users.builder()
                .provider(provider)
                .oauthId(oauthId)
                .email(email)
                .name(name)
                .role(Role.USER)
                .imageUrl(imageUrl)
                .build();
    }

    @Override
    public String getUsername() {
        return oauthId;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}

