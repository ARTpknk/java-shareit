package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.dto.Item;

import java.util.List;

public interface ItemStorage {
    Item create(Item item, int ownerId);

    Item update(Item item, int ownerId);

    List<Item> getAllItems();

    Item getItemById(Integer id);

    List<Item> getMyItems(int ownerId);

    Integer getNextId();

    List<Item> searchItems(String text);
}
