// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
@EnableWebSecurity
open class SecurityConfig
@Autowired
constructor(private val userDetailsService: BarkeepUserDetailsService) : WebSecurityConfigurerAdapter() {

  @Autowired
  @Throws(Exception::class)
  fun configAuthentication(auth: AuthenticationManagerBuilder) {
    auth.userDetailsService(userDetailsService).passwordEncoder(BCryptPasswordEncoder())
  }

  @Throws(Exception::class)
  override fun configure(http: HttpSecurity) {
    http.authorizeRequests() //
            .antMatchers("/signup").permitAll() //
            .anyRequest().authenticated() //
            .and().httpBasic() //
            .and().csrf().disable()
  }
}