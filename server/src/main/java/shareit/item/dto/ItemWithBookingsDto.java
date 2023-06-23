package shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import lombok.With;
import shareit.booking.model.Booking;
import shareit.item.comment.CommentDto;

import java.util.List;

@Data
@Builder
public class ItemWithBookingsDto {
    @With
    private int id;
    private String name;
    private String description;
    private Boolean available;
    private Booking lastBooking;
    private Booking nextBooking;
    private List<CommentDto> comments;
}
