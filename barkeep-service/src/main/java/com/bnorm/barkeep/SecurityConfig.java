// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.bnorm.barkeep.db.BarkeepUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final BarkeepUserDetailsService userDetailsService;

  @Autowired
  public SecurityConfig(BarkeepUserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  @Autowired
  public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService);
    //        .passwordEncoder(new BCryptPasswordEncoder());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests().antMatchers("/**").authenticated() //
        .and().httpBasic();
  }
}