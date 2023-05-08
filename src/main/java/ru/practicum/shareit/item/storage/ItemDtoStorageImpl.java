package ru.practicum.shareit.item.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.OwnerNotFoundException;
import ru.practicum.shareit.user.exception.ShareItNotFoundException;
import ru.practicum.shareit.user.storage.UserDtoStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemDtoStorageImpl implements ItemDtoStorage {
    private final HashMap<Integer, ItemDto> items = new HashMap<>();
    private final UserDtoStorage userDtoStorage;

    private Integer id = 1;

    public ItemDto create(ItemDto item, int ownerId) {
        if (userDtoStorage.getUser(ownerId) == null) {
            throw new OwnerNotFoundException("Owner not found");
        }
        Integer id = getNextId();
        ItemDto newItem = item.withId(id);
        newItem.setOwner(ownerId);
        items.put(id, newItem);
        return newItem;
    }

    public ItemDto update(ItemDto item, int ownerId) {
        int id = item.getId();
        if (userDtoStorage.getUser(ownerId) == null) {
            throw new OwnerNotFoundException("Owner not found");
        }
        if (items.containsKey(id)) {
            if (ownerId != items.get(id).getOwner()) {
                throw new OwnerNotFoundException("owner not found");
            }
        }

        if (items.containsKey(id)) {
            if (item.getName() == null) {
                item.setName(items.get(id).getName());
            }
            if (item.getDescription() == null) {
                item.setDescription(items.get(id).getDescription());
            }
            if (item.getAvailable() == null) {
                item.setAvailable(items.get(id).getAvailable());
            }
            if (item.getOwner() == 0) {
                item.setOwner(items.get(id).getOwner());
            }
            if (item.getRequest() == null) {
                item.setRequest(items.get(id).getRequest());
            }
            items.put(id, item);
            return item;
        } else {
            throw new ShareItNotFoundException(String.format("Вещь с таким id: %d не найдена.", item.getId()));
        }
    }

    public List<ItemDto> getAllItems() {
        return new ArrayList<>(items.values());
    }

    public ItemDto getItemById(Integer id) {
        if (items.containsKey(id)) {
            return items.get(id);
        } else {
            throw new ShareItNotFoundException(String.format("Вещь с id: %d не найдена.", id));
        }
    }

    public List<ItemDto> getMyItems(int ownerId) {
        return items.values().stream().filter(itemDto -> itemDto.getOwner() == ownerId).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text == null || text.isEmpty() || text.isBlank()) {
            return new ArrayList<>();
        }
        List<ItemDto> searched = new ArrayList<>();
        searched.addAll(items.values().stream().filter(ItemDto::getAvailable)
                .filter(ItemDto -> ItemDto.getName().toLowerCase().contains(text.toLowerCase()))
                .collect(Collectors.toList()));
        searched.addAll(items.values().stream().filter(ItemDto::getAvailable)
                .filter(ItemDto -> ItemDto.getDescription().toLowerCase().contains(text.toLowerCase()))
                .collect(Collectors.toList()));
        return searched.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public Integer getNextId() {
        return id++;
    }
}
