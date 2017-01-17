// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.security

import com.bnorm.barkeep.db.UserEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import javax.persistence.EntityManager

@Component
open class BarkeepUserDetailsService
@Autowired
constructor(private val em: EntityManager) : UserDetailsService {

  @Throws(UsernameNotFoundException::class)
  override fun loadUserByUsername(username: String): UserDetails {
    val query = em.createNamedQuery("UserEntity.findByUsername", UserEntity::class.java)
    val user = query.setParameter("username", username).singleResult
            ?: throw UsernameNotFoundException("Unable to find user $username")
    return UserEntityDetails(user)
  }
}
