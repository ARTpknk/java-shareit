package shareit.item.dto;

import lombok.experimental.UtilityClass;
import shareit.booking.model.Booking;
import shareit.item.comment.CommentDto;
import shareit.item.model.Item;

import java.util.List;

@UtilityClass
public class ItemMapper {


    public ItemDto toItemDto(Item item, Integer requestId) {
        return new ItemDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                requestId
        );
    }

    public ItemDto toItemDto(Item item) {
        return new ItemDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable()
        );
    }

    public Item toItem(ItemDto itemDto, int ownerId, int requestId) {
        return new Item(itemDto.getId(), itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable(), ownerId,
                requestId);
    }

    public Item toItem(ItemDto itemDto, int ownerId) {
        return new Item(itemDto.getId(), itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable(), ownerId);
    }

    public ItemWithBookingsDto toItemWithBookingsDto(Item item, Booking lastBooking, Booking nextBooking,
                                                     List<CommentDto> comments) {
        return new ItemWithBookingsDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                lastBooking,
                nextBooking,
                comments
        );
    }
}
