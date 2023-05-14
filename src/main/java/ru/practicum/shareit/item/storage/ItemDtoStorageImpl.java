package ru.practicum.shareit.item.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.model.OwnerNotFoundException;
import ru.practicum.shareit.exceptions.model.ShareItNotFoundException;
import ru.practicum.shareit.item.dto.Item;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemDtoStorageImpl implements ItemStorage {
    private final Map<Integer, Item> items = new HashMap<>();
    private final Map<Integer, List<Item>> userItemIndex = new LinkedHashMap<>();

    private Integer id = 1;

    @Override
    public Item create(Item item, int ownerId) {
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
        if (items.containsKey(id)) {
            if (ownerId != items.get(id).getOwner()) {
                throw new OwnerNotFoundException("owner not found");
            }
            Item updateItem = getItemById(id);
            String name = item.getName();
            String description = item.getDescription();
            Boolean available = item.getAvailable();
            if (name != null && !name.isBlank()) {
                updateItem.setName(name);
            }
            if (description != null && !description.isBlank()) {
                updateItem.setDescription(description);
            }
            if (available != null) {
                updateItem.setAvailable(available);
            }
            return updateItem;
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
        Item item = items.get(id);
        if (item == null) {
            throw new ShareItNotFoundException(String.format("Вещь с id: %d не найдена.", id));
        }
        return item;
    }

    @Override
    public List<Item> getMyItems(int ownerId) {
        return userItemIndex.getOrDefault(ownerId, List.of());
    }

    @Override
    public List<Item> searchItems(String text) {
        return items.values().stream()
                .filter(Item::getAvailable)
                .filter(ItemDto -> ItemDto.getName().toLowerCase().contains(text.toLowerCase())
                        || ItemDto.getDescription().toLowerCase().contains(text.toLowerCase()))
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public Integer getNextId() {
        return id++;
    }
}