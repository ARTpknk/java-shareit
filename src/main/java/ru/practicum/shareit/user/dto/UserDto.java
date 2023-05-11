package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;
import lombok.With;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

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
