// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.security

import com.bnorm.barkeep.model.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

internal class UserEntityDetails(val user: User) : UserDetails {

  override fun getAuthorities(): Collection<GrantedAuthority> {
    return emptyList()
  }

  override fun getPassword(): String? {
    return user.password
  }

  override fun getUsername(): String? {
    return user.username
  }

  override fun isAccountNonExpired(): Boolean {
    return true
  }

  override fun isAccountNonLocked(): Boolean {
    return true
  }

  override fun isCredentialsNonExpired(): Boolean {
    return true
  }

  override fun isEnabled(): Boolean {
    return true
  }
}
