package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import shareit.exceptions.model.OwnerNotFoundException;
import shareit.exceptions.model.ShareItNotFoundException;
import shareit.user.model.User;
import shareit.user.repository.UserRepository;
import shareit.user.service.UserServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @InjectMocks
    UserServiceImpl userService;
    @Mock
    UserRepository repository;

    private User user;
    private final int id = 1;
    private final int wrongId = 99;

    @BeforeEach
    public void makeUserForTests() {
        user = User.builder()
                .id(id)
                .name("user")
                .email("user@email.ru")
                .build();
    }

    @Test
    void createTest() {
        Mockito.when(repository.save(user)).thenReturn(user);
        User newUser = userService.create(user);
        assertThat(newUser.equals(user)).isTrue();
        Mockito.verify(repository, Mockito.times(1))
                .save(user);
        Mockito.verifyNoMoreInteractions(repository);
    }

    @Test
    void getAllUsersTest() {
        Mockito.when(repository.save(user)).thenReturn(user);
        Mockito.when(repository.findAll()).thenReturn(Collections.singletonList(user));
        userService.create(user);
        List<User> users = userService.getAllUsers();
        assertThat(users.contains(user)).isTrue();
        Mockito.verify(repository, Mockito.times(1))
                .save(user);
        Mockito.verify(repository, Mockito.times(1))
                .findAll();
        Mockito.verifyNoMoreInteractions(repository);
    }

    @Test
    void getUserByIdTest() {
        Mockito.when(repository.save(user)).thenReturn(user);
        Mockito.when(repository.findById(id)).thenReturn(Optional.ofNullable(user));
        userService.create(user);
        User newUser = userService.getUserById(id);
        assertThat(newUser.equals(user)).isTrue();
        Mockito.verify(repository, Mockito.times(2))
                .findById(id);
        Mockito.verifyNoMoreInteractions(repository);
    }

    @Test
    void updateTest() {
        Mockito.when(repository.save(user)).thenReturn(user);
        Mockito.when(repository.findById(id)).thenReturn(Optional.ofNullable(user));
        userService.create(user);
        user.setName("Update");
        User newUser = userService.update(user);
        assertThat(newUser.equals(user)).isTrue();
        Mockito.verify(repository, Mockito.times(2))
                .save(user);
        Mockito.verify(repository, Mockito.times(2))
                .findById(id);
        Mockito.verifyNoMoreInteractions(repository);
    }

    @Test
    void deleteUserByIdTest() {
        userService.deleteUserById(id);
        verify(repository, times(1)).deleteById(1);
        Mockito.verifyNoMoreInteractions(repository);
    }

    @Test
    void getUnknownUserByIdTest() {
        assertThrows(
                OwnerNotFoundException.class,
                () -> userService.getUserById(wrongId));
    }

    @Test
    void updateUnknownUserTest() {
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(
                ShareItNotFoundException.class,
                () -> userService.update(user));
        Mockito.verify(repository, Mockito.times(1))
                .findById(id);
        Mockito.verifyNoMoreInteractions(repository);
    }
}
