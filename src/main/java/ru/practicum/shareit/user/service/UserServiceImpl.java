package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoStorage;
import ru.practicum.shareit.user.service.UserService;

import java.util.*;

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
    public UserDto getUserBy(Integer id) {
        return storage.getUserBy(id);
    }

    @Override
    public UserDto update(UserDto user) {
        return storage.update(user);
    }

    @Override
    public void deleteUserBy(Integer id) {
        storage.deleteUserBy(id);
    }
}
