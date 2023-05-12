package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserStorage storage;

    @Override
    public User create(User user) {
        return storage.create(user);
    }

    @Override
    public List<User> getAllUsers() {
        return storage.getAllUsers();
    }

    @Override
    public User getUserById(Integer id) {
        return storage.getUserById(id);
    }

    @Override
    public User update(User user) {
        return storage.update(user);
    }

    @Override
    public void deleteUserById(Integer id) {
        storage.deleteUserById(id);
    }
}
