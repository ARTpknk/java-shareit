package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.classes.Create;
import ru.practicum.shareit.classes.Update;
import ru.practicum.shareit.exceptions.model.OwnerNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto create(@Validated(Create.class) @RequestBody UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        log.info(String.format("UserController: create User request. Data: %s", user));
        return UserMapper.toUserDto(userService.create(user));
    }

    @PatchMapping("/{id}")
    public UserDto update(@Validated(Update.class) @PathVariable("id") Integer id, @RequestBody UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        log.info(String.format("UserController: update User request. Data: %s", user));
        user.setId(id);
        return UserMapper.toUserDto(userService.update(user));
    }

    @GetMapping
    public List<UserDto> findAll() {
        return userService.getAllUsers().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserDto findUserBy(@PathVariable("id") Integer id) {
        return UserMapper.toUserDto(userService.getUserById(id));
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable("userId") Integer userId) {
        if (userService.getUserById(userId) == null) {
            throw new OwnerNotFoundException("User with Id: " + userId + " not found");
        }
        userService.deleteUserById(userId);
        log.info(String.format("UserController: Remove user with id: %d.", userId));
    }
}
