package shareit.user.dto;

import lombok.Builder;
import lombok.Data;
import lombok.With;
import shareit.classes.Create;
import shareit.classes.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class UserDto {
    @With
    private int id;
    @NotBlank(groups = Create.class)
    private String name;
    @Email(groups = {Create.class, Update.class})
    @NotBlank(groups = Create.class)
    private String email;
}
