package com.tvsc.service.impl

import com.tvsc.core.model.User
import com.tvsc.service.UserService
import org.springframework.stereotype.Component

/**
 *
 * @author Taras Zubrei
 */
@Component
open class UserServiceImpl : UserService {
    override fun getCurrentUser(): User = User(1, "Jack") //TODO: Security
}