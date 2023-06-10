package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.controller.RequestController;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class ItemControllerTest {

    @MockBean
    private ItemService itemService;
    @MockBean
    private UserService userService;

    @InjectMocks
    private ItemController controller;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mvc;

    private Item item;
    private Item item2;
    private ItemDto itemDto;
    private ItemDto itemDto2;
    private User user;
    private Comment comment;
    private CommentDto commentDto;
    int userId = 1;
    int wronguserId = 99;
    int id = 1;
    int wrongId = 99;
    int from = 0;
    int size = 20;
    String text = "item";
    LocalDateTime now = LocalDateTime.now();


    @BeforeEach
    public void makeItemForTests() {
        item = Item.builder()
                .id(1)
                .name("item")
                .description("good")
                .available(true)
                .ownerId(userId)
                .requestId(1)
                .build();

        itemDto = ItemDto.builder()
                .id(1)
                .name("item")
                .description("good")
                .available(true)
                .requestId(1)
                .build();

        itemDto2 = ItemDto.builder()
                .id(2)
                .name("item2")
                .description("good!!!")
                .available(true)
                .requestId(1)
                .build();


        item2 = Item.builder()
                .id(2)
                .name("item2")
                .description("good!!!")
                .available(true)
                .ownerId(userId)
                .requestId(1)
                .build();

        user = User.builder()
                .id(userId)
                .name("user")
                .email("user@email.ru")
                .build();

        comment = Comment.builder()
                .id(1)
                .text(text)
                .itemId(id)
                .authorId(userId)
                .build();

        commentDto = CommentDto.builder()
                .id(1)
                .text(text)
                .build();
    }


    @Test
    void createTest() throws Exception {
        when(itemService.create(any(), anyInt()))
                .thenReturn(item);

        mvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Integer.class))
                .andExpect(jsonPath("$.name").value(itemDto.getName()))
                .andExpect(jsonPath("$.description").value(itemDto.getDescription()))
                .andExpect(jsonPath("$.available").value(itemDto.getAvailable()));
    }

    @Test
    void createTestWithoutRequestTest() throws Exception {
        when(itemService.create(any(), anyInt()))
                .thenReturn(item);
        itemDto.setRequestId(null);
        item.setRequestId(null);
        mvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Integer.class))
                .andExpect(jsonPath("$.name").value(itemDto.getName()))
                .andExpect(jsonPath("$.description").value(itemDto.getDescription()))
                .andExpect(jsonPath("$.available").value(itemDto.getAvailable()));
    }

    @Test
    void updateTest() throws Exception {
        when(itemService.update(any(), anyInt()))
                .thenReturn(item);
        item.setName("Игорь");
        itemDto.setName("Игорь");
        mvc.perform(patch("/items/{id}", 1)
                        .content(objectMapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Integer.class))
                .andExpect(jsonPath("$.name").value(itemDto.getName()))
                .andExpect(jsonPath("$.description").value(itemDto.getDescription()))
                .andExpect(jsonPath("$.available").value(itemDto.getAvailable()));
    }


    @Test
    void findItemByTest() throws Exception {
        when(itemService.getItemById(item.getId()))
                .thenReturn(item);
        when(itemService.getLastBooking(item.getId(), 2))
                .thenReturn(null);
        when(itemService.getNextBooking(item.getId(), 2))
                .thenReturn(null);
        when(itemService.getComments(item.getId()))
                .thenReturn(Collections.emptyList());
        when(itemService.getUserName(item.getId()))
                .thenReturn(user.getName());

        mvc.perform(get("/items/{id}", 1)
                        .content(objectMapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Integer.class))
                .andExpect(jsonPath("$.name").value(itemDto.getName()))
                .andExpect(jsonPath("$.description").value(itemDto.getDescription()))
                .andExpect(jsonPath("$.available").value(itemDto.getAvailable()));
    }

    @Test
    void findMyItemsTest() throws Exception {
        List<Item> items = new ArrayList<>();
        items.add(item);
        items.add(item2);
        when(itemService.getMyItems(item.getOwnerId(), size, from))
                .thenReturn(items);
        when(itemService.getLastBooking(item.getId(), 2))
                .thenReturn(null);
        when(itemService.getNextBooking(item.getId(), 2))
                .thenReturn(null);
        when(itemService.getComments(item.getId()))
                .thenReturn(Collections.emptyList());
        when(itemService.getUserName(item.getId()))
                .thenReturn(user.getName());

        mvc.perform(get("/items", 1)
                        .content(objectMapper.writeValueAsString(items))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemDto.getId())))
                .andExpect(jsonPath("$[1].id").value(itemDto2.getId()))
                .andExpect(jsonPath("$[0].name").value(itemDto.getName()))
                .andExpect(jsonPath("$[1].name").value(itemDto2.getName()))
                .andExpect(jsonPath("$[0].description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$[1].description", is(itemDto2.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemDto2.getAvailable())))
                .andExpect(jsonPath("$[1].available", is(itemDto2.getAvailable())));
    }

    @Test
    void findMyItemsWrongFromTest() throws Exception {
        mvc.perform(get("/items", 1)
                        .param("from", String.valueOf(-1))
                        .param("size", String.valueOf(size))
                        .content(objectMapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void searchItemsWrongFromTest() throws Exception {
        mvc.perform(get("/items/search", 1)
                        .param("from", String.valueOf(-1))
                        .param("size", String.valueOf(size))
                        .content(objectMapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void searchItemsNoTextTest() throws Exception {
        mvc.perform(get("/items/search", 1)
                        .param("text", String.valueOf(""))
                        .content(objectMapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void searchItemsTest() throws Exception {
        List<Item> items = new ArrayList<>();
        items.add(item);
        items.add(item2);

        when(itemService.searchItems(text, size, from))
                .thenReturn(items);
        mvc.perform(get("/items/search", 1)
                        .param("text", String.valueOf(text))
                        .content(objectMapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemDto.getId())))
                .andExpect(jsonPath("$[1].id").value(itemDto2.getId()))
                .andExpect(jsonPath("$[0].name").value(itemDto.getName()))
                .andExpect(jsonPath("$[1].name").value(itemDto2.getName()))
                .andExpect(jsonPath("$[0].description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$[1].description", is(itemDto2.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemDto2.getAvailable())))
                .andExpect(jsonPath("$[1].available", is(itemDto2.getAvailable())));
    }

    @Test
    void createCommentTest() throws Exception {
        when(itemService.createComment(any()))
                .thenReturn(comment);
        when(userService.getUserById(1))
                .thenReturn(user);
        mvc.perform(post("/items/{itemId}/comment", 1)
                        .content(objectMapper.writeValueAsString(commentDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Integer.class))
                .andExpect(jsonPath("$.text").value(commentDto.getText()));
    }



}
