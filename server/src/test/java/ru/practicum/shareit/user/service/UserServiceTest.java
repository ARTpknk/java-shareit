package shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import shareit.user.model.User;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTest {
    @Autowired
    UserService service;
    private User user1;
    private User user2;
    private final int id1 = 1;
    private final int id2 = 2;

    @BeforeEach
    public void makeUserForTests() {
        user1 = User.builder()
                .id(id1)
                .name("user")
                .email("user@email.ru")
                .build();

        user2 = User.builder()
                .id(id2)
                .name("user2")
                .email("user2@email.ru")
                .build();
    }

    @Test
    void test() {
        User newUser = service.create(user1);
        assertThat(newUser, equalTo(user1));

        service.create(user2);
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        List<User> newUsers = service.getAllUsers();
        assertThat(newUsers, equalTo(users));

        User newUser2 = service.getUserById(id1);
        assertThat(newUser2, equalTo(user1));

        user1.setName("updateName");
        User newUser3 = service.update(user1);
        assertThat(newUser3, equalTo(user1));

        service.deleteUserById(id1);
        service.deleteUserById(id2);
        assertThat(service.getAllUsers(), equalTo(Collections.emptyList()));
    }
}
