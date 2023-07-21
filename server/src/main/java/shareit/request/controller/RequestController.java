package shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shareit.classes.Create;
import shareit.exceptions.model.ShareItBadRequest;
import shareit.item.dto.ItemDto;
import shareit.item.dto.ItemMapper;
import shareit.item.model.Item;
import shareit.item.service.ItemService;
import shareit.request.dto.RequestDto;
import shareit.request.dto.RequestMapper;
import shareit.request.model.Request;
import shareit.request.service.RequestService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@Slf4j
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;
    private final ItemService itemService;

    @PostMapping
    public RequestDto create(@Validated(Create.class) @RequestBody RequestDto requestDto,
                             @RequestHeader("X-Sharer-User-Id") int requestorId) {
        requestDto.setCreated(LocalDateTime.now());
        Request request = RequestMapper.toRequest(requestDto, requestorId);
        log.info(String.format("RequestController: create Request request. Data: %s", request));
        return RequestMapper.toRequestDto(requestService.create(request, requestorId), Collections.emptyList());
    }

    @GetMapping
    public List<RequestDto> getMyRequests(@RequestHeader("X-Sharer-User-Id") int userId) {
        return requestService.getMyRequests(userId).stream()
                .map((Request request) -> (RequestMapper.toRequestDto(request, itemService.getItemsByRequest(request.getId()).stream().map((Item item) -> (ItemMapper.toItemDto(item, request.getId())))
                        .collect(Collectors.toList()))))
                .collect(Collectors.toList());
    }

    @GetMapping("/all")
    public List<RequestDto> getRequests(@RequestHeader("X-Sharer-User-Id") int userId,
                                        @RequestParam(required = false, defaultValue = "0") int from,
                                        @RequestParam(required = false, defaultValue = "20") int size) {
        if (from < 0 || size < 1) {
            throw new ShareItBadRequest("некорректные значения");
        }

        return requestService.getByUserIdAndRequestId(userId, from, size).stream()
                .map((Request request) -> (RequestMapper.toRequestDto(request,
                        itemService.getItemsByRequest(request.getId())
                                .stream().map((Item item) -> (ItemMapper.toItemDto(item, request.getId())))
                                .collect(Collectors.toList()))))
                .collect(Collectors.toList());
    }

    @GetMapping("/{requestId}")
    public RequestDto getRequest(@RequestHeader("X-Sharer-User-Id") int userId, @PathVariable("requestId") Integer id) {
        Request request = requestService.getRequest(userId, id);
        List<ItemDto> items = itemService.getItemsByRequest(request.getId())
                .stream().map((Item item) -> (ItemMapper.toItemDto(item, request.getId())))
                .collect(Collectors.toList());
        return RequestMapper.toRequestDto(request, items);
    }

}
