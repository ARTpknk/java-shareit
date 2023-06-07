package ru.practicum.shareit.item.service;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.model.OwnerNotFoundException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item create(Item item, int ownerId) throws OwnerNotFoundException;

    Item update(Item item, int ownerId);

    List<Item> getMyItems(int ownerId, int size, int from);

    Item getItemById(Integer id);

    List<Item> searchItems(String text, int size, int from);

    Booking getLastBooking(int itemId, int userId);

    Booking getNextBooking(int itemId, int userId);

    Comment createComment(Comment comment);

    List<Comment> getComments(int itemId);

    String getUserName(int userId);
    List<Item> getItemsByRequest(int requestId);
}
