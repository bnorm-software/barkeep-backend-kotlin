// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.db;

import org.springframework.beans.factory.annotation.Autowired;
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
    return new UserEntityDetails(user);
  }
}
