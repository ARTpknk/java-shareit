package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.OwnerNotFoundException;

import java.util.List;

public interface ItemService {
    ItemDto create(ItemDto item, int ownerId) throws OwnerNotFoundException;

    ItemDto update(ItemDto item, int ownerId);

    List<ItemDto> getMyItems(int ownerId);

    ItemDto getItemById(Integer id);

    List<ItemDto> searchItems(String text);
}
