package com.tvsc.service.impl

import com.tvsc.core.immutable.User
import com.tvsc.service.UserService
import org.springframework.stereotype.Component

/**
 *
 * @author Taras Zubrei
 */
@Component
open class UserServiceImpl : UserService {
    override fun getCurrentUser(): User = User.Builder().id(1).name("Jack").build() //TODO: Security
}