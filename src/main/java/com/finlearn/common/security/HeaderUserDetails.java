package com.finlearn.common.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/** Gateway가 주입한 X-User-* 헤더로 구성하는 UserDetails 구현체.*/
@Getter
public class HeaderUserDetails implements UserDetails {

    private final UUID userId;
    private final String email;
    private final String role;

    public HeaderUserDetails(UUID userId, String email, String role) {
        this.userId = userId;
        this.email = email;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override public String getPassword() { return null; }
    @Override public String getUsername() { return email; }
}
