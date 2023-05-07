package ru.practicum.shareit.user.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
@Service
@RequiredArgsConstructor
public class UserMapper {

    public static UserDto toUserDto(User user) {
        return new UserDto( user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
