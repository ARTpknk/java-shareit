package shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import shareit.request.model.Request;
import shareit.request.service.RequestService;
import shareit.user.model.User;
import shareit.user.service.UserService;

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
    private final int userId = 1;
    private final int id1 = 1;
    private final LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    public void makeRequestsForTests() {
        int id2 = 2;
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
    void test() {
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
