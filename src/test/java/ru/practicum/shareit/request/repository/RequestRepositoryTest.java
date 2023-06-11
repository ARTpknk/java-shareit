package ru.practicum.shareit.request.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class RequestRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private RequestRepository repository;
    @Autowired
    private UserRepository userRepository;
    private Request request;
    private Request request2;

    @BeforeEach
    public void setUp() {
        User user = User.builder()
                .id(1)
                .name("user")
                .email("user@email.ru")
                .build();

        User user2 = User.builder()
                .id(2)
                .name("user2")
                .email("user2@email.ru")
                .build();

        request = Request.builder()
                .id(1)
                .description("description")
                .requestorId(1)
                .created(LocalDateTime.now())
                .build();

        request2 = Request.builder()
                .id(2)
                .description("description2")
                .requestorId(1)
                .created(LocalDateTime.now())
                .build();
        User dbUser = userRepository.save(user);
        User dbUser2 = userRepository.save(user2);
        Request dbRequest = repository.save(request);
        Request dbRequest2 = repository.save(request2);
        entityManager.persist(dbUser);
        entityManager.persist(dbUser2);
        entityManager.persist(dbRequest);
        entityManager.persist(dbRequest2);
        entityManager.getEntityManager().getTransaction().commit();
    }

    @Test
    void findUserRequestsTest() {
        List<Request> requests = new ArrayList<>();
        requests.add(request);
        requests.add(request2);
        List<Request> newRequests = this.repository.findRequests(2, 20, 0);
        assertThat(newRequests).isEqualTo(requests);
    }
}
