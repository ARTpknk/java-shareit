package shareit.item.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import shareit.item.model.Item;
import shareit.request.model.Request;
import shareit.request.repository.RequestRepository;
import shareit.user.model.User;
import shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class ItemRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository repository;
    @Autowired
    private RequestRepository requestRepository;
    private Item item;
    private Item item2;

    @BeforeEach
    public void setUp() {
        User user = User.builder()
                .id(1)
                .name("user")
                .email("user@email.ru")
                .build();

        Request request = Request.builder()
                .id(1)
                .description("description")
                .requestorId(1)
                .created(LocalDateTime.now())
                .build();

        item = Item.builder()
                .id(1)
                .name("item")
                .description("good")
                .available(true)
                .ownerId(1)
                .requestId(1)
                .build();

        item2 = Item.builder()
                .id(2)
                .name("item2")
                .description("very good")
                .available(true)
                .ownerId(1)
                .requestId(1)
                .build();

        Item item3 = Item.builder()
                .id(3)
                .name("item3")
                .description("very very good, literally the best")
                .available(false)
                .ownerId(1)
                .requestId(1)
                .build();

        entityManager.persist(userRepository.save(user));
        entityManager.persist(requestRepository.save(request));
        entityManager.persist(repository.save(item));
        entityManager.persist(repository.save(item2));
        entityManager.persist(repository.save(item3));
        entityManager.getEntityManager().getTransaction().commit();
    }

    @Test
    void searchTest() {
        List<Item> items = new ArrayList<>();
        items.add(item);
        items.add(item2);
        List<Item> newItems = this.repository.search("good", PageRequest.of(0, 20)).toList();
        assertThat(newItems).isEqualTo(items);
    }
}
