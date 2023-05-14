package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.model.OwnerNotFoundException;
import ru.practicum.shareit.item.dto.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemStorage storage;
    private final UserService userService;

    @Override
    public Item create(Item item, int ownerId) {
        if (userService.getUserById(ownerId) == null) {
            throw new OwnerNotFoundException("Owner not found");
        }
        return storage.create(item, ownerId);
    }

    @Override
    public Item update(Item item, int ownerId) {
        if (userService.getUserById(ownerId) == null) {
            throw new OwnerNotFoundException("Owner not found");
        }
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
