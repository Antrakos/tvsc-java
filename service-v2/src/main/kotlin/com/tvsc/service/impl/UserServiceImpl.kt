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
    override fun getCurrentUser(): User = User(id = 1, name = "Jack") //TODO: Security
}