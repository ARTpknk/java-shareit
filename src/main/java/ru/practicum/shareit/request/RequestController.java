package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.classes.Create;
import ru.practicum.shareit.exceptions.model.ShareItBadRequest;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestMapper;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
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
                .map((Request request)-> (RequestMapper.toRequestDto(request, Collections.emptyList())))
                .collect(Collectors.toList());
        //ЗАМЕНИТЬ ПУСТОЙ ЛИСТ НА ЛИСТ С ITEMS
    }

    @GetMapping("/all")
    public List<RequestDto> getUserRequests(@RequestHeader("X-Sharer-User-Id") int userId,
                                            @RequestParam(required = false, defaultValue = "0") int from,
                                             @RequestParam(required = false, defaultValue = "1000000000") int size) {
        //не понимаю как совместить запрет на size==0 и разрешение на defaultValue=0

        if(from==0 && size == 1000000000){
            return Collections.emptyList();
        }

        if(from<0 || size <= 0){
           throw new ShareItBadRequest("некорректные значения");
        }

        return requestService.getUserRequests(userId, from, size).stream()
                .map((Request request)-> (RequestMapper.toRequestDto(request, Collections.emptyList())))
                .collect(Collectors.toList());
        //ЗАМЕНИТЬ ПУСТОЙ ЛИСТ НА ЛИСТ С ITEMS
    }


    @GetMapping("/{requestId}")
    public RequestDto getRequest(@RequestHeader("X-Sharer-User-Id") int userId, @PathVariable("requestId") Integer id) {
        return RequestMapper.toRequestDto(requestService.getRequest(userId, id), Collections.emptyList());
        //ЗАМЕНИТЬ ПУСТОЙ ЛИСТ НА ЛИСТ С ITEMS
    }










}
