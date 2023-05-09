package ru.practicum.shareit.item.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.Item;
import ru.practicum.shareit.item.exception.OwnerNotFoundException;
import ru.practicum.shareit.user.exception.ShareItNotFoundException;
import ru.practicum.shareit.user.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemDtoStorageImpl implements ItemStorage {
    private final Map<Integer, Item> items = new HashMap<>();
    private final Map<Integer, List<Item>> userItemIndex = new LinkedHashMap<>();
    private final UserService userService;


    private Integer id = 1;

    @Override
    public Item create(Item item, int ownerId) {
        if (userService.getUserById(ownerId) == null) {
            throw new OwnerNotFoundException("Owner not found");
        }
        Integer id = getNextId();
        Item newItem = item.withId(id);
        newItem.setOwner(ownerId);
        items.put(id, newItem);
        if (!userItemIndex.containsKey(ownerId)) {
            userItemIndex.put(ownerId, new ArrayList<>());
        }
        userItemIndex.get(ownerId).add(newItem);
        return newItem;
    }

    @Override
    public Item update(Item item, int ownerId) {
        int id = item.getId();
        if (userService.getUserById(ownerId) == null) {
            throw new OwnerNotFoundException("Owner not found");
        }
        if (items.containsKey(id)) {
            if (ownerId != items.get(id).getOwner()) {
                throw new OwnerNotFoundException("owner not found");
            }
            if (item.getName() == null || item.getName().isBlank()) {
                item.setName(items.get(id).getName());
            }
            if (item.getDescription() == null || item.getDescription().isBlank()) {
                item.setDescription(items.get(id).getDescription());
            }
            if (item.getAvailable() == null) {
                item.setAvailable(items.get(id).getAvailable());
            }
            if (item.getOwner() == 0) {
                item.setOwner(items.get(id).getOwner());
            }
            userItemIndex.get(ownerId).remove(items.get(id));
            items.put(id, item);
            userItemIndex.get(ownerId).add(item);
            return item;
        } else {
            throw new ShareItNotFoundException(String.format("Вещь с таким id: %d не найдена.", item.getId()));
        }
    }

    @Override
    public List<Item> getAllItems() {
        return new ArrayList<>(items.values());
    }

    @Override
    public Item getItemById(Integer id) {
        if (items.containsKey(id)) {
            return items.get(id);
        } else {
            throw new ShareItNotFoundException(String.format("Вещь с id: %d не найдена.", id));
        }
    }

    @Override
    public List<Item> getMyItems(int ownerId) {
        return userItemIndex.get(ownerId);
    }

    @Override
    public List<Item> searchItems(String text) {
        List<Item> searched = new ArrayList<>();
        searched.addAll(items.values().stream().filter(Item::getAvailable)
                .filter(ItemDto -> ItemDto.getName().toLowerCase().contains(text.toLowerCase()))
                .collect(Collectors.toList()));
        searched.addAll(items.values().stream().filter(Item::getAvailable)
                .filter(ItemDto -> ItemDto.getDescription().toLowerCase().contains(text.toLowerCase()))
                .collect(Collectors.toList()));
        return searched.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public Integer getNextId() {
        return id++;
    }
}
