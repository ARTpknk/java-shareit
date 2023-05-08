package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;
import lombok.With;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class UserDto {
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
