package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemDtoStorage {
    ItemDto create(ItemDto item, int ownerId);

    ItemDto update(ItemDto item, int ownerId);

    List<ItemDto> getAllItems();

    ItemDto getItemById(Integer id);

    List<ItemDto> getMyItems(int ownerId);

    Integer getNextId();

    List<ItemDto> searchItems(String text);
}
