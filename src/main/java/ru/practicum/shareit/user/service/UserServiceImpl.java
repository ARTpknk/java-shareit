package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.storage.UserDtoStorage;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserDtoStorage storage;

    @Override
    public UserDto create(UserDto user) {
        return storage.create(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return storage.getAllUsers();
    }

    @Override
    public UserDto getUserById(Integer id) {
        return storage.getUserById(id);
    }

    @Override
    public UserDto update(UserDto user) {
        return storage.update(user);
    }

    @Override
    public void deleteUserById(Integer id) {
        storage.deleteUserById(id);
    }
}
