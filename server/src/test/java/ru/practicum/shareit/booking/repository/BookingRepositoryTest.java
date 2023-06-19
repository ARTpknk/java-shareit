package shareit.booking.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import shareit.booking.model.Booking;
import shareit.booking.model.BookingStatus;
import shareit.booking.repository.BookingRepository;
import shareit.item.model.Item;
import shareit.item.repository.ItemRepository;
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
public class BookingRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private BookingRepository repository;
    private Booking booking;
    private Booking booking2;
    private final LocalDateTime now = LocalDateTime.now();
    private final LocalDateTime start = now.plusDays(8);
    private final LocalDateTime end = now.plusDays(10);

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

        Request request = Request.builder()
                .id(1)
                .description("description")
                .requestorId(2)
                .created(LocalDateTime.now())
                .build();

        Request request2 = Request.builder()
                .id(2)
                .description("description2")
                .requestorId(2)
                .created(LocalDateTime.now())
                .build();

        Item item = Item.builder()
                .id(1)
                .name("item")
                .description("good")
                .available(true)
                .ownerId(1)
                .requestId(1)
                .build();

        Item item2 = Item.builder()
                .id(2)
                .name("item2")
                .description("very good")
                .available(true)
                .ownerId(1)
                .requestId(1)
                .build();

        booking = Booking.builder()
                .id(1)
                .start(start)
                .end(end)
                .itemId(1)
                .bookerId(2)
                .status(BookingStatus.WAITING)
                .build();

        booking2 = Booking.builder()
                .id(2)
                .start(start)
                .end(end)
                .itemId(2)
                .bookerId(2)
                .status(BookingStatus.WAITING)
                .build();

        entityManager.persist(userRepository.save(user));
        entityManager.persist(userRepository.save(user2));
        entityManager.persist(requestRepository.save(request));
        entityManager.persist(requestRepository.save(request2));
        entityManager.persist(itemRepository.save(item));
        entityManager.persist(itemRepository.save(item2));
        entityManager.persist(repository.save(booking));
        entityManager.persist(repository.save(booking2));
        entityManager.getEntityManager().getTransaction().commit();
    }

    @AfterEach
    public void cleanDb() {
        userRepository.deleteAll();
        repository.deleteAll();
        itemRepository.deleteAll();
        requestRepository.deleteAll();
    }

    @Test
    void findMyBookingsTest() {
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        bookings.add(booking2);
        List<Booking> newBookings = this.repository.findMyBookings(2, 20, 0);
        assertThat(newBookings).isEqualTo(bookings);
    }

    @Test
    void findOwnerBookingsTest() {
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        bookings.add(booking2);
        List<Booking> newBookings = this.repository.findAllByOwnerId(1, 0, 20);
        assertThat(newBookings).isEqualTo(bookings);
    }
}
