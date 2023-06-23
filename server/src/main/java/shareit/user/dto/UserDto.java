package shareit.user.dto;

import lombok.Builder;
import lombok.Data;
import lombok.With;

@Data
@Builder
public class UserDto {
    @With
    private int id;
    private String name;
    private String email;
}
