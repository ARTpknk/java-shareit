package ru.practicum.shareit.request.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.model.Request;

import java.util.List;

@UtilityClass
public class RequestMapper {

    public RequestDto toRequestDto(Request request, List<ItemDto> items) {
        return RequestDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .created(request.getCreated())
                .items(items)
                .build();
    }

    public Request toRequest(RequestDto requestDto, int requestorId) {
        return Request.builder()
                .id(requestDto.getId())
                .description(requestDto.getDescription())
                .requestorId(requestorId)
                .created(requestDto.getCreated())
                .build();
    }
}
