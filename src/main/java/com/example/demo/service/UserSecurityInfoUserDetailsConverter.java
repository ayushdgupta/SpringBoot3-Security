package com.example.demo.service;

import com.example.demo.entity.UserSecurityInfo;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

// This class will convert the UserSecurityInfo object into userDetails object
public class UserSecurityInfoUserDetailsConverter implements UserDetails {

  private String name;
  private String password;
  private List<GrantedAuthority> roles;

  public UserSecurityInfoUserDetailsConverter(UserSecurityInfo userSecurityInfo) {
    this.name = userSecurityInfo.getUserName();
    this.password = userSecurityInfo.getPassword();
    this.roles =
        Arrays.stream(userSecurityInfo.getRoles().split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.roles;
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return this.name;
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
