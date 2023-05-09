package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Value;
import lombok.With;

import javax.validation.constraints.Email;

@Value
@Builder
public class User { //для Базы Данных
    @With
    int id;
    String name;
    @Email
    String email;
}
