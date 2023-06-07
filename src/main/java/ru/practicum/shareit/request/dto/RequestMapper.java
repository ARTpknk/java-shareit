package ru.practicum.shareit.request.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.Request;

import java.time.LocalDateTime;
import java.util.List;

@UtilityClass
public class RequestMapper {

    public RequestDto toRequestDto(Request request, List<ItemDto> items){
        return new RequestDto(request.getId(), request.getDescription(), request.getCreated(), items);
    }

    public Request toRequest(RequestDto requestDto, int requestorId){
        return new Request(requestDto.getId(), requestDto.getDescription(), requestorId, requestDto.getCreated());
    }
}
