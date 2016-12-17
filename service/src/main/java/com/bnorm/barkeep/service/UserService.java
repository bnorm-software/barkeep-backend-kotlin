// Copyright 2016 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.service;

import com.bnorm.barkeep.model.User;

public interface UserService {

  User getUser(long userId);

  User createUser(User user);

  User setUser(long userId, User user);
}
