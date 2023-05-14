package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;
import lombok.With;
import ru.practicum.shareit.classes.Create;
import ru.practicum.shareit.classes.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class UserDto {
    @With
    int id;
    @NotBlank(groups = Create.class)
    String name;
    @Email(groups = {Create.class, Update.class})
    @NotBlank(groups = Create.class)
    String email;
}
