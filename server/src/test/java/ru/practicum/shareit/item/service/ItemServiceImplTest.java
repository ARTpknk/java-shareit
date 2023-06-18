package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import shareit.exceptions.model.OwnerNotFoundException;
import shareit.exceptions.model.ShareItNotFoundException;
import shareit.item.comment.Comment;
import shareit.item.comment.CommentRepository;
import shareit.item.model.Item;
import shareit.item.repository.ItemRepository;
import shareit.item.service.ItemServiceImpl;
import shareit.user.model.User;
import shareit.user.service.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(MockitoExtension.class)
public class ItemServiceImplTest {
    @InjectMocks
    ItemServiceImpl itemService;
    @Mock
    UserServiceImpl userService;
    @Mock
    ItemRepository repository;
    @Mock
    CommentRepository commentRepository;

    private Item item;
    private User user;
    private Comment comment;
    private final int userId = 1;
    private final int wrongUserId = 99;
    private final int id = 1;
    private final int wrongId = 99;
    private final int from = 0;
    private final int size = 20;
    private final String text = "text";

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
    }

    @Test
    void createTest() {
        Mockito.when(repository.save(item)).thenReturn(item);
        Mockito.when(userService.getUserById(userId)).thenReturn(user);
        Item newItem = itemService.create(item, userId);
        assertThat(newItem.equals(item)).isTrue();
        Mockito.verify(userService, Mockito.times(1))
                .getUserById(userId);
        Mockito.verify(repository, Mockito.times(1))
                .save(item);
        Mockito.verifyNoMoreInteractions(userService, repository);
    }

    @Test
    void createItemWithoutUser() {
        Mockito.when(userService.getUserById(wrongUserId)).thenReturn(null);
        assertThrows(
                OwnerNotFoundException.class,
                () -> itemService.create(item, wrongUserId));
        Mockito.verify(userService, Mockito.times(1))
                .getUserById(wrongUserId);
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    void updateTest() {
        Mockito.when(userService.getUserById(userId)).thenReturn(user);
        Mockito.when(repository.findById(id)).thenReturn(Optional.ofNullable(item));
        Mockito.when(repository.save(item)).thenReturn(item);
        item.setName("Игорь");
        Item newItem = itemService.update(item, userId);
        assertThat(newItem.equals(item)).isTrue();
        Mockito.verify(userService, Mockito.times(1))
                .getUserById(userId);
        Mockito.verify(repository, Mockito.times(1))
                .save(item);
        Mockito.verify(repository, Mockito.times(2))
                .findById(id);
        Mockito.verifyNoMoreInteractions(userService, repository);
    }

    @Test
    void updateItemWithUnknownUserTest() {
        Mockito.when(userService.getUserById(wrongUserId)).thenReturn(null);
        assertThrows(
                OwnerNotFoundException.class,
                () -> itemService.update(item, wrongUserId));
        Mockito.verify(userService, Mockito.times(1))
                .getUserById(wrongUserId);
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    void updateUnknownItemTest() {
        Mockito.when(userService.getUserById(userId)).thenReturn(user);
        Mockito.when(repository.findById(wrongId)).thenReturn(Optional.empty());
        item.setId(wrongId);
        assertThrows(
                ShareItNotFoundException.class,
                () -> itemService.update(item, userId));
        Mockito.verify(userService, Mockito.times(1))
                .getUserById(userId);
        Mockito.verify(repository, Mockito.times(1))
                .findById(wrongId);
        Mockito.verifyNoMoreInteractions(userService, repository);
    }

    @Test
    void getMyItemsTest() {
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        Page<Item> itemPage = new PageImpl<>(itemList);
        Mockito.when(repository.findByOwnerId(userId, PageRequest.of(from, size))).thenReturn(itemPage);
        List<Item> newItemList = itemService.getMyItems(userId, size, from);
        assertThat(newItemList.equals(itemList)).isTrue();
        Mockito.verify(repository, Mockito.times(1))
                .findByOwnerId(userId, PageRequest.of(from, size));
        Mockito.verifyNoMoreInteractions(userService, repository);
    }

    @Test
    void getItemByIdTest() {
        Mockito.when(repository.findById(id)).thenReturn(Optional.ofNullable(item));
        Item newItem = itemService.getItemById(id);
        assertThat(newItem.equals(item)).isTrue();
        Mockito.verify(repository, Mockito.times(2))
                .findById(id);
        Mockito.verifyNoMoreInteractions(userService, repository);
    }

    @Test
    void getUnknownItemByIdTest() {
        Mockito.when(repository.findById(wrongId)).thenReturn(Optional.empty());
        assertThrows(
                OwnerNotFoundException.class,
                () -> itemService.getItemById(wrongId));
        Mockito.verify(repository, Mockito.times(1))
                .findById(wrongId);
        Mockito.verifyNoMoreInteractions(repository);
    }

    @Test
    void searchItemsTest() {
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        Page<Item> itemPage = new PageImpl<>(itemList);
        Mockito.when(repository.search(text, PageRequest.of(from, size))).thenReturn(itemPage);
        List<Item> newItemList = itemService.searchItems(text, size, from);
        assertThat(newItemList.equals(itemList)).isTrue();
        Mockito.verify(repository, Mockito.times(1))
                .search(text, PageRequest.of(from, size));
        Mockito.verifyNoMoreInteractions(repository);
    }

    @Test
    void getCommentsTest() {
        List<Comment> comments = new ArrayList<>();
        comments.add(comment);
        Mockito.when(commentRepository.findAllByItemId(id)).thenReturn(comments);
        List<Comment> newCommentList = itemService.getComments(id);
        assertThat(newCommentList.equals(comments)).isTrue();
        Mockito.verify(commentRepository, Mockito.times(1))
                .findAllByItemId(id);
        Mockito.verifyNoMoreInteractions(commentRepository);
    }

    @Test
    void getUserNameTest() {
        Mockito.when(userService.getUserById(userId)).thenReturn(user);
        String name = user.getName();
        assertThat(name.equals(itemService.getUserName(userId))).isTrue();
        Mockito.verify(userService, Mockito.times(1))
                .getUserById(userId);
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    void getItemsByRequestTest() {
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        Mockito.when(repository.findItemsByRequestId(1)).thenReturn(itemList);
        List<Item> newItemList = itemService.getItemsByRequest(1);
        assertThat(itemList.equals(newItemList)).isTrue();
        Mockito.verify(repository, Mockito.times(1))
                .findItemsByRequestId(1);
        Mockito.verifyNoMoreInteractions(repository);
    }
}
