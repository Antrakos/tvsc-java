package com.tvsc.service.services;

import com.tvsc.core.model.User;
import org.springframework.stereotype.Service;

/**
 * @author Taras Zubrei
 */
@Service
public class UserService {
    public User getCurrentUser() {
        return new User(1, "Jack"); //TODO: Security
    }
}
