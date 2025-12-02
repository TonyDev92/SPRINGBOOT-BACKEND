package com.example.demo.app.aplication.port.out;

import com.example.demo.app.domain.model.User;

public interface UserPersistencePort {
    User save(User user);
}
