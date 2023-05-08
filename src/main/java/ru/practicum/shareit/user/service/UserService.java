package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto create(UserDto user);

    List<UserDto> getAllUsers();

    UserDto getUserById(Integer id);

    UserDto update(UserDto user);

    void deleteUserById(Integer id);
}
