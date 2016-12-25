// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.security;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.bnorm.barkeep.db.UserEntity;

@Component
public class BarkeepUserDetailsService implements UserDetailsService {

  private final EntityManager em;

  @Autowired
  public BarkeepUserDetailsService(EntityManager em) {
    this.em = em;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    TypedQuery<UserEntity> query = em.createNamedQuery("UserEntity.findByUsername", UserEntity.class);
    UserEntity user = query.setParameter("username", username).getSingleResult();
    if (user == null) {
      throw new UsernameNotFoundException("Unable to find user " + username);
    }
    return new UserEntityDetails(user);
  }
}
