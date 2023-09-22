package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.classes.Create;
import ru.practicum.shareit.classes.Update;
import ru.practicum.shareit.exceptions.model.ShareItBadRequest;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> create(@Validated(Create.class) @RequestBody ItemDto itemDto,
                                         @RequestHeader("X-Sharer-User-Id") int ownerId) {
        return itemClient.create(itemDto, ownerId);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@Validated(Update.class) @PathVariable("id") Integer id,
                                         @RequestBody ItemDto itemDto,
                                         @RequestHeader("X-Sharer-User-Id") int ownerId) {

        return itemClient.update(id, itemDto, ownerId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findItemById(@PathVariable("id") Integer id,
                                               @RequestHeader("X-Sharer-User-Id") int userId) {
        return itemClient.findItemById(id, userId);
    }

    @GetMapping
    public ResponseEntity<Object> findMyItems(@RequestHeader("X-Sharer-User-Id") int ownerId,
                                              @RequestParam(required = false, defaultValue = "0") int from,
                                              @RequestParam(required = false, defaultValue = "20") int size) {
        if (from < 0 || size < 1) {
            throw new ShareItBadRequest("некорректные значения");
        }
        return itemClient.findMyItems(ownerId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestParam String text,
                                              @RequestParam(required = false, defaultValue = "0") int from,
                                              @RequestParam(required = false, defaultValue = "20") int size) {
        if (from < 0 || size < 1) {
            throw new ShareItBadRequest("некорректные значения");
        }

        return itemClient.searchItems(text, from, size);
    }

    @PostMapping("{itemId}/comment")
    public ResponseEntity<Object> createComment(@Validated(Create.class) @RequestBody CommentDto commentDto,
                                                @RequestHeader("X-Sharer-User-Id") int authorId,
                                                @PathVariable("itemId") Integer itemId) {

        return itemClient.createComment(commentDto, authorId, itemId);
    }
}
