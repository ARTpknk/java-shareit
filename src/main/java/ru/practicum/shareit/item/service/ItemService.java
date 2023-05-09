package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.Item;
import ru.practicum.shareit.item.exception.OwnerNotFoundException;

import java.util.List;

public interface ItemService {
    Item create(Item item, int ownerId) throws OwnerNotFoundException;

    Item update(Item item, int ownerId);

    List<Item> getMyItems(int ownerId);

    Item getItemById(Integer id);

    List<Item> searchItems(String text);
}
