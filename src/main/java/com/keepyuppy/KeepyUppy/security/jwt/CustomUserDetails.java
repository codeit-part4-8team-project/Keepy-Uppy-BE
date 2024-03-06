package com.keepyuppy.KeepyUppy.security.jwt;

import com.keepyuppy.KeepyUppy.user.domain.entity.Users;
import com.keepyuppy.KeepyUppy.user.domain.enums.Provider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@RequiredArgsConstructor
@Getter
public class CustomUserDetails implements UserDetails {

    private long userId;
    private Provider provider;
    private String oauthId;
    private String username;
    private String name;
    private String imageUrl;
    private Collection<? extends GrantedAuthority> authorities;


    public CustomUserDetails(Users user) {
        this.userId = user.getId();
        this.provider = user.getProvider();
        this.oauthId = user.getOauthId();
        this.username = user.getUsername();
        this.name = user.getName();
        this.imageUrl = user.getImageUrl();
        this.authorities = Collections.emptyList();
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

