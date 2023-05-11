package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Value;
import lombok.With;


@Value
@Builder
public class User { //для Базы Данных
    @With
    int id;
    String name;
    String email;
}
