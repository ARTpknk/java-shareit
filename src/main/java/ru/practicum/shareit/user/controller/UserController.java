package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto user) {
        log.info(String.format("UserController: create User request. Data: %s", user));
        return userService.create(user);
    }

    @PatchMapping("/{id}")
    public UserDto update(@PathVariable("id") Integer id, @RequestBody UserDto user) {
        log.info(String.format("UserController: update User request. Data: %s", user));
        return userService.update(user.withId(id));
    }

    @GetMapping
    public List<UserDto> findAll() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDto findUserBy(@PathVariable("id") Integer id) {
        return userService.getUserById(id);
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable("userId") Integer userId) {
        userService.deleteUserById(userId);
        log.info(String.format("UserController: Remove user with id: %d.", userId));
    }
}
