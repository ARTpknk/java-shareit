package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {
    User create(User user);

    List<User> getAllUsers();

    User getUserById(Integer id);

    void deleteUserById(Integer id);

    Integer getNextId();

    User update(User user);

    User getUser(int id);


}
