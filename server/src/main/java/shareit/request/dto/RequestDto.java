package shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import lombok.With;
import shareit.item.dto.ItemDto;

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
    private String description;
    private LocalDateTime created;
    private List<ItemDto> items;
}
