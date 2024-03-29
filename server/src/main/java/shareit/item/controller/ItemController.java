package shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shareit.classes.Create;
import shareit.classes.Update;
import shareit.exceptions.model.ShareItBadRequest;
import shareit.item.comment.Comment;
import shareit.item.comment.CommentDto;
import shareit.item.comment.CommentMapper;
import shareit.item.dto.ItemDto;
import shareit.item.dto.ItemMapper;
import shareit.item.dto.ItemWithBookingsDto;
import shareit.item.model.Item;
import shareit.item.service.ItemService;
import shareit.user.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final UserService userService;

    @PostMapping
    public ItemDto create(@Validated(Create.class) @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") int ownerId) {
        Item item;
        if (itemDto.getRequestId() == null) {
            item = ItemMapper.toItem(itemDto, ownerId);
            log.info(String.format("ItemController: create Item request. Data: %s", item));
            return ItemMapper.toItemDto(itemService.create(item, ownerId));
        } else {
            item = ItemMapper.toItem(itemDto, ownerId, itemDto.getRequestId());
            log.info(String.format("ItemController: create Item request. Data: %s", item));
            return ItemMapper.toItemDto(itemService.create(item, ownerId), item.getRequestId());
        }
    }

    @PatchMapping("/{id}")
    public ItemDto update(@Validated(Update.class) @PathVariable("id") Integer id, @RequestBody ItemDto itemDto,
                          @RequestHeader("X-Sharer-User-Id") int ownerId) {

        Item item = ItemMapper.toItem(itemDto, ownerId);

        log.info(String.format("ItemController: update Item request. Data: %s", item));
        item.setId(id);
        return ItemMapper.toItemDto(itemService.update(item, ownerId));
    }

    @GetMapping("/{id}")
    public ItemWithBookingsDto findItemById(@PathVariable("id") Integer id,
                                          @RequestHeader("X-Sharer-User-Id") int userId) {
        return ItemMapper.toItemWithBookingsDto(itemService.getItemById(id), itemService.getLastBooking(id, userId),
                itemService.getNextBooking(id, userId), itemService.getComments(id)
                        .stream().map((Comment comment) -> (CommentMapper.toCommentDto(comment, itemService.getUserName(comment.getAuthorId())))).collect(Collectors.toList()));
    }

    @GetMapping
    public List<ItemWithBookingsDto> findMyItems(@RequestHeader("X-Sharer-User-Id") int ownerId,
                                                 @RequestParam(required = false, defaultValue = "0") int from,
                                                 @RequestParam(required = false, defaultValue = "20") int size) {
        if (from < 0 || size < 1) {
            throw new ShareItBadRequest("некорректные значения");
        }
        return itemService.getMyItems(ownerId, size, from).stream()
                .map((Item item) -> ItemMapper.toItemWithBookingsDto(item, itemService.getLastBooking(item.getId(), ownerId),
                        itemService.getNextBooking(item.getId(), ownerId), itemService.getComments(item.getId())
                                .stream().map((Comment comment) -> (CommentMapper.toCommentDto(comment, itemService.getUserName(comment.getAuthorId())))).collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text,
                                     @RequestParam(required = false, defaultValue = "0") int from,
                                     @RequestParam(required = false, defaultValue = "20") int size) {
        if (from < 0 || size < 1) {
            throw new ShareItBadRequest("некорректные значения");
        }
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemService.searchItems(text, size, from).stream()
                .map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @PostMapping("{itemId}/comment")
    public CommentDto createComment(@Validated(Create.class) @RequestBody CommentDto commentDto,
                                    @RequestHeader("X-Sharer-User-Id") int authorId,
                                    @PathVariable("itemId") Integer itemId) {
        Comment comment = CommentMapper.toComment(commentDto, itemId, authorId);
        comment.setAuthorId(authorId);
        comment.setItemId(itemId);
        return CommentMapper.toCommentDto(itemService.createComment(comment),
                userService.getUserById(authorId).getName());
    }

}
