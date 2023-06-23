package shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import lombok.With;
import shareit.booking.model.BookingStatus;
import shareit.item.model.Item;
import shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
@Builder
public class BookingDto {
    @With
    private int id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Item item;
    private Integer itemId;
    private User booker;
    private BookingStatus status;
}
