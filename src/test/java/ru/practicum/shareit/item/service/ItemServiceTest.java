package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@SpringBootTest(
        properties = "db.name=test3",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceTest {

    @Autowired
    ItemService service;

    @Autowired
    UserService userService;
    @Autowired
    RequestService requestService;

    @Autowired
    BookingService bookingService;


    private Request request1;
    private Request request2;


    private Item item1;
    private Item item2;
    private User user;
    private User user2;
    private Comment comment;
    int userId = 1;
    int id1 = 1;
    int id2 = 2;
    int from = 0;
    int size = 20;
    String text = "veryGood";
    LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    public void makeItemForTests() {


        item1 = Item.builder()
                .id(1)
                .name("item")
                .description("good")
                .available(true)
                .requestId(1)
                .ownerId(userId)
                .build();

        item2 = Item.builder()
                .id(2)
                .name("item2")
                .description("veryGood")
                .available(true)
                .requestId(2)
                .ownerId(userId)
                .build();

        user = User.builder()
                .id(userId)
                .name("user")
                .email("user@email.ru")
                .build();

        user2 = User.builder()
                .id(2)
                .name("user2")
                .email("user2@email.ru")
                .build();

        comment = Comment.builder()
                .id(1)
                .text(text)
                .itemId(id1)
                .authorId(userId)
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
        requestService.create(request1, userId);
        requestService.create(request2, userId);
        Item newItem = service.create(item1, userId);
        assertThat(newItem, equalTo(item1));

        item1.setName("update");
        Item newItem2 = service.update(item1, userId);
        assertThat(newItem2, equalTo(item1));

        service.create(item2, userId);
        List<Item> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);
        List<Item> newItems = service.getMyItems(userId, size, from);
        assertThat(newItems, equalTo(items));

        Item newItem3 = service.getItemById(id1);
        assertThat(newItem3, equalTo(item1));

        items.remove(item1);
        List<Item> newItems2 = service.searchItems(text, size, from);
        assertThat(newItems2, equalTo(items));

        String name1 = user.getName();
        String name2 = service.getUserName(userId);
        assertThat(name1, equalTo(name2));

        List<Item> newItems3 = service.getItemsByRequest(2);
        assertThat(newItems3, equalTo(items));

    }
}
