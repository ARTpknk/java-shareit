package shareit.booking.dto;

import lombok.experimental.UtilityClass;
import shareit.booking.model.Booking;

@UtilityClass
public class BookingMapper {
    public BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(booking.getItem())
                .itemId(booking.getItemId())
                .booker(booking.getBooker())
                .status(booking.getStatus())
                .build();
    }

    public Booking toBooking(BookingDto bookingDto, int bookerId) {
        return Booking.builder()
                .id(bookingDto.getId())
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .itemId(bookingDto.getItemId())
                .bookerId(bookerId)
                .status(bookingDto.getStatus())
                .build();
    }
}
