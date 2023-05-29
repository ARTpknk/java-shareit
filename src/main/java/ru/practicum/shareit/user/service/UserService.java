package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User create(User user);

    List<User> getAllUsers();

    User getUserById(Integer id);

    User update(User user);

    void deleteUserById(Integer id);
}
