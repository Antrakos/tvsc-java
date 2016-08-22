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
    override fun getCurrentUser(): User = User().setId(1).setName("Jack") //TODO: Security
}