package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@SpringBootTest(
        properties = "db.name=test2",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    RequestService service;

    private Request request1;
    private Request request2;
    private User user;
    int userId = 1;
    int id1 = 1;
    int id2 = 2;
    LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    public void makeRequestsForTests() {
        user = User.builder()
                .id(userId)
                .name("user")
                .email("user@email.ru")
                .build();

        request1 = Request.builder()
                .id(id1)
                .description("description1")
                .requestorId(userId)
                .created(now)
                .build();

        request2 = Request.builder()
                .id(id2)
                .description("description2")
                .requestorId(userId)
                .created(now)
                .build();
    }

    @Test
    void Test() {
        userService.create(user);
        Request newRequest = service.create(request1, userId);
        assertThat(newRequest, equalTo(request1));

        service.create(request2, userId);
        List<Request> requests = new ArrayList<>();
        requests.add(request1);
        requests.add(request2);
        List<Request> newRequests = service.getMyRequests(userId);
        assertThat(newRequests, equalTo(requests));

        Request newRequest2 = service.getRequest(userId, id1);
        assertThat(newRequest2, equalTo(request1));
    }
}
