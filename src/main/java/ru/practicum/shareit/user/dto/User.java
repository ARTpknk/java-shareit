package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;
import lombok.With;


@Data
@Builder
public class User { //для Базы Данных
    @With
    int id;
    String name;
    String email;
}
