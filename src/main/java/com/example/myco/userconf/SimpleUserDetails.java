package com.example.myco.userconf;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimpleUserDetails implements UserDetails {

    private String username;
    private String password;
    private boolean enabled = true;
    private Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();

    public SimpleUserDetails(String username, String password, String... authorities) {
        this.username = username;
        this.password = password;
        this.authorities.addAll(role(authorities)
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.enabled;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.enabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.enabled;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    private List<String> role(String... roles) {
        return Stream.of(roles)
                .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                .collect(Collectors.toList());
    }
}
