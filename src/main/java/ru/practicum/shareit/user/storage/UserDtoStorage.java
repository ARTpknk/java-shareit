package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserDtoStorage {
    UserDto create(UserDto user);

    List<UserDto> getAllUsers();

    UserDto getUserById(Integer id);

    void deleteUserById(Integer id);

    Integer getNextId();

    UserDto update(UserDto user);

    UserDto getUser(int id);


}
