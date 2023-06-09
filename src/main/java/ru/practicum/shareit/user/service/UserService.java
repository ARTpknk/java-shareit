package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    User create(User user);

    List<User> getAllUsers();

    User getUserById(Integer id);

    User update(User user);

    void deleteUserById(Integer id);
}
