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
    @NotBlank
    String name;
    @Email
    @NotBlank
    String email;
}
