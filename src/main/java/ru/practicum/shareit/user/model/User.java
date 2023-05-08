package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Value;
import lombok.With;

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
