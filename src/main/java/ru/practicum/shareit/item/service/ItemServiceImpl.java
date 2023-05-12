package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.Item;
import ru.practicum.shareit.item.storage.ItemStorage;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemStorage storage;

    @Override
    public Item create(Item item, int ownerId) {
        return storage.create(item, ownerId);
    }

    @Override
    public Item update(Item item, int ownerId) {
        return storage.update(item, ownerId);
    }

    @Override
    public List<Item> getMyItems(int ownerId) {
        return storage.getMyItems(ownerId);
    }

    @Override
    public Item getItemById(Integer id) {
        return storage.getItemById(id);
    }

    @Override
    public List<Item> searchItems(String text) {
        return storage.searchItems(text);
    }

}
