package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    public UserDto create(UserDto user);
    public List<UserDto> getAllUsers() ;

    public UserDto getUserBy(Integer id);

    public UserDto update(UserDto user);

    public void deleteUserBy(Integer id);
}
