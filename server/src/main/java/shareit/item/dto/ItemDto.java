package shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import lombok.With;

@Data
@Builder
public class ItemDto {
    @With
    private int id;
    private String name;
    private String description;
    private Boolean available;
    private Integer requestId;

    public ItemDto() {
    }

    public ItemDto(int id, String name, String description, Boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
    }

    public ItemDto(int id, String name, String description, Boolean available, Integer requestId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.requestId = requestId;
    }
}