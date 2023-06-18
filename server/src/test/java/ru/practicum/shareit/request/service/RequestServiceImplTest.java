package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import shareit.exceptions.model.OwnerNotFoundException;
import shareit.request.model.Request;
import shareit.request.repository.RequestRepository;
import shareit.request.service.RequestServiceImpl;
import shareit.user.model.User;
import shareit.user.service.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class RequestServiceImplTest {
    @InjectMocks
    RequestServiceImpl requestService;
    @Mock
    UserServiceImpl userService;
    @Mock
    RequestRepository repository;

    private Request request;
    private User user;
    private final int userId = 1;
    private final int id = 1;
    private final int from = 0;
    private final int size = 20;
    private final LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    public void makeItemForTests() {
        user = User.builder()
                .id(userId)
                .name("user")
                .email("user@email.ru")
                .build();

        request = Request.builder()
                .id(1)
                .description("description1")
                .requestor(user)
                .requestorId(userId)
                .created(now)
                .build();
    }

    @Test
    void createTest() {
        Mockito.when(userService.getUserById(userId)).thenReturn(user);
        Mockito.when(repository.save(request)).thenReturn(request);
        Request newRequest = requestService.create(request, userId);
        assertThat(newRequest.equals(request)).isTrue();
        Mockito.verify(repository, Mockito.times(1))
                .save(request);
        Mockito.verify(userService, Mockito.times(1))
                .getUserById(userId);
        Mockito.verifyNoMoreInteractions(userService, repository);
    }

    @Test
    void createWithWrongUserTest() {
        Mockito.when(userService.getUserById(userId)).thenReturn(null);
        assertThrows(
                OwnerNotFoundException.class,
                () -> requestService.create(request, userId));
        Mockito.verify(userService, Mockito.times(1))
                .getUserById(userId);
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    void getMyRequestsTest() {
        List<Request> requests = new ArrayList<>();
        requests.add(request);
        Mockito.when(userService.getUserById(userId)).thenReturn(user);
        Mockito.when(repository.findAllByRequestorIdOrderByCreatedDesc(userId)).thenReturn(requests);
        List<Request> newRequests = requestService.getMyRequests(userId);
        assertThat(newRequests.equals(requests)).isTrue();
        Mockito.verify(userService, Mockito.times(1))
                .getUserById(userId);
        Mockito.verify(repository, Mockito.times(1))
                .findAllByRequestorIdOrderByCreatedDesc(userId);
        Mockito.verifyNoMoreInteractions(userService, repository);
    }

    @Test
    void getMyRequestsWithWrongUserTest() {
        Mockito.when(userService.getUserById(userId)).thenReturn(null);
        assertThrows(
                OwnerNotFoundException.class,
                () -> requestService.getMyRequests(userId));
        Mockito.verify(userService, Mockito.times(1))
                .getUserById(userId);
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    void getUserRequestsWithWrongUserTest() {
        Mockito.when(userService.getUserById(userId)).thenReturn(null);
        assertThrows(
                OwnerNotFoundException.class,
                () -> requestService.getByUserIdAndRequestId(userId, size, from));
        Mockito.verify(userService, Mockito.times(1))
                .getUserById(userId);
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    void getUserRequestsTest() {
        List<Request> requests = new ArrayList<>();
        requests.add(request);
        Mockito.when(userService.getUserById(userId)).thenReturn(user);
        Mockito.when(repository.findRequests(userId, size, from)).thenReturn(requests);
        List<Request> newRequests = requestService.getByUserIdAndRequestId(userId, from, size);
        assertThat(newRequests.equals(requests)).isTrue();
        Mockito.verify(userService, Mockito.times(1))
                .getUserById(userId);
        Mockito.verify(repository, Mockito.times(1))
                .findRequests(userId, size, from);
        Mockito.verifyNoMoreInteractions(userService, repository);
    }

    @Test
    void getRequestUnknownUserTest() {
        Mockito.when(userService.getUserById(userId)).thenReturn(null);
        assertThrows(
                OwnerNotFoundException.class,
                () -> requestService.getRequest(userId, id));
        Mockito.verify(userService, Mockito.times(1))
                .getUserById(userId);
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    void getRequestUnknownRequestTest() {
        Mockito.when(userService.getUserById(userId)).thenReturn(user);
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(
                OwnerNotFoundException.class,
                () -> requestService.getRequest(userId, id));
        Mockito.verify(userService, Mockito.times(1))
                .getUserById(userId);
        Mockito.verify(repository, Mockito.times(1))
                .findById(id);
        Mockito.verifyNoMoreInteractions(userService, repository);
    }

    @Test
    void getByUserIdAndReuestIdTest() {
        Mockito.when(userService.getUserById(userId)).thenReturn(user);
        Mockito.when(repository.findById(id)).thenReturn(Optional.ofNullable(request));
        Request newRequest = requestService.getRequest(userId, id);
        assertThat(newRequest.equals(request)).isTrue();
        Mockito.verify(userService, Mockito.times(1))
                .getUserById(userId);
        Mockito.verify(repository, Mockito.times(2))
                .findById(id);


        Mockito.verifyNoMoreInteractions(userService, repository);
    }
}
