package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.EmptyOwnerFieldException;
import ru.practicum.shareit.item.exception.ShareItBadRequest;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto create(@RequestBody ItemDto item, @RequestHeader("X-Sharer-User-Id") int ownerId) {
        if (ownerId == 0) {
            throw new EmptyOwnerFieldException("Empty owner field");
        }
        try {
            if (item.getAvailable() == null || item.getName().isBlank() || item.getName().isEmpty() ||
                    item.getName() == null || item.getDescription().isBlank()
                    || item.getDescription().isEmpty() || item.getDescription() == null) {
                throw new ShareItBadRequest("empty available status");
            }
        } catch (Exception e) {
            throw new ShareItBadRequest("empty available status");
        }
        log.info(String.format("ItemController: create Item request. Data: %s", item));
        return itemService.create(item, ownerId);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@PathVariable("id") Integer id, @RequestBody ItemDto item,
                          @RequestHeader("X-Sharer-User-Id") int ownerId) {
        if (ownerId == 0) {
            throw new EmptyOwnerFieldException("Empty owner field");
        }
        log.info(String.format("ItemController: update Item request. Data: %s", item));
        return itemService.update(item.withId(id), ownerId);
    }

    @GetMapping("/{id}")
    public ItemDto findItemBy(@PathVariable("id") Integer id) {
        return itemService.getItemById(id);
    }

    @GetMapping
    public List<ItemDto> findMyItems(@RequestHeader("X-Sharer-User-Id") int ownerId) {
        return itemService.getMyItems(ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        return itemService.searchItems(text);
    }
}
