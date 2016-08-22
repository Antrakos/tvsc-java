package com.tvsc.service

import com.tvsc.core.model.User

/**
 *
 * @author Taras Zubrei
 */
interface UserService {
    fun getCurrentUser(): User
}