package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.Item;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.exception.EmptyOwnerFieldException;
import ru.practicum.shareit.item.exception.ShareItBadRequest;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto create(@RequestBody @Valid ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") int ownerId) {
        if (ownerId == 0) {
            throw new EmptyOwnerFieldException("Empty owner field");
        }
        Item item = ItemMapper.toItem(itemDto, ownerId);
        log.info(String.format("ItemController: create Item request. Data: %s", item));
        return ItemMapper.toItemDto(itemService.create(item, ownerId));
    }

    @PatchMapping("/{id}")
    public ItemDto update(@PathVariable("id") Integer id, @RequestBody ItemDto itemDto,
                          @RequestHeader("X-Sharer-User-Id") int ownerId) {
        if (ownerId == 0) {
            throw new EmptyOwnerFieldException("Empty owner field");
        }
        Item item = ItemMapper.toItem(itemDto, ownerId);

        log.info(String.format("ItemController: update Item request. Data: %s", item));
        return ItemMapper.toItemDto(itemService.update(item.withId(id), ownerId));
    }

    @GetMapping("/{id}")
    public ItemDto findItemBy(@PathVariable("id") Integer id) {
        return ItemMapper.toItemDto(itemService.getItemById(id));
    }

    @GetMapping
    public List<ItemDto> findMyItems(@RequestHeader("X-Sharer-User-Id") int ownerId) {
        return itemService.getMyItems(ownerId).stream()
                .map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemService.searchItems(text).stream()
                .map(ItemMapper::toItemDto).collect(Collectors.toList());
    }
}
