// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.db;

import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class BarkeepUserDetailsService implements UserDetailsService {

  private final BarkeepRepository repository;

  @Autowired
  public BarkeepUserDetailsService(BarkeepRepository repository) {
    this.repository = repository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserEntity user = repository.findUserByUsername(username);
    if (user == null) {
      throw new UsernameNotFoundException("Unable to find user " + username);
    }

    return new UserDetails() {
      @Override
      public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
      }

      @Override
      public String getPassword() {
        return user.getPassword();
      }

      @Override
      public String getUsername() {
        return user.getUsername();
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
    };
  }
}
