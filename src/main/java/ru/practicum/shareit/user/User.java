package ru.practicum.shareit.user;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Value
@Builder
public class User { //для Базы Данных
    @With
    int id;
    @NotNull
    @NotBlank
    String name;
    @Email
    @NotNull
    @NotBlank
    String email;
}
