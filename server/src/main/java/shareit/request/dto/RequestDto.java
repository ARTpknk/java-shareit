package shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import lombok.With;
import shareit.classes.Create;
import shareit.item.dto.ItemDto;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@Builder
public class RequestDto {
    @With
    private int id;
    @NotBlank(groups = Create.class)
    private String description;
    private LocalDateTime created;
    private List<ItemDto> items;
}
