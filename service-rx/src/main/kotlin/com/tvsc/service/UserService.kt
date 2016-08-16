package com.tvsc.service

import com.tvsc.core.immutable.User

/**
 *
 * @author Taras Zubrei
 */
interface UserService {
    fun getCurrentUser(): User
}