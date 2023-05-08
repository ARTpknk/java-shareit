package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.storage.ItemDtoStorageImpl;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemDtoStorageImpl storage;

    @Override
    public ItemDto create(ItemDto item, int ownerId) {
        return storage.create(item, ownerId);
    }

    @Override
    public ItemDto update(ItemDto item, int ownerId) {
        return storage.update(item, ownerId);
    }

    @Override
    public List<ItemDto> getMyItems(int ownerId) {
        return storage.getMyItems(ownerId);
    }

    @Override
    public ItemDto getItemById(Integer id) {
        return storage.getItemById(id);
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        return storage.searchItems(text);
    }

}
