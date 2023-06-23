package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.classes.Update;
import ru.practicum.shareit.exceptions.model.ShareItBadRequest;

import java.util.Map;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody BookingDto bookingDto,
                                         @RequestHeader("X-Sharer-User-Id") int bookerId) {
        return bookingClient.bookItem(bookerId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> update(@Validated(Update.class) @PathVariable("bookingId") Integer id,
                                         @RequestHeader("X-Sharer-User-Id") long ownerId,
                                         @RequestParam(required = false, defaultValue = "true") boolean approved) {
        return bookingClient.update(id, ownerId, approved);
    }


    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getMyBooking(@PathVariable("bookingId") Integer id,
                                               @RequestHeader("X-Sharer-User-Id") int userId) {
        return bookingClient.getBooking(userId, Long.valueOf(id));
    }

    @GetMapping
    public ResponseEntity<Object> getMyBookings(@RequestHeader("X-Sharer-User-Id") int userId,
                                                @RequestParam(required = false, defaultValue = "ALL") State state,
                                                @RequestParam(required = false, defaultValue = "0") int from,
                                                @RequestParam(required = false, defaultValue = "20") int size) {


        if (from < 0 || size < 1) {
            throw new ShareItBadRequest("некорректные значения");
        }
        return bookingClient.getBookings(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getOwnerBookings(@RequestHeader("X-Sharer-User-Id") int ownerId,
                                                   @RequestParam(required = false, defaultValue = "ALL") State state,
                                                   @RequestParam(required = false, defaultValue = "0") int from,
                                                   @RequestParam(required = false, defaultValue = "20") int size) {

        if (from < 0 || size < 1) {
            throw new ShareItBadRequest("некорректные значения");
        }
        return bookingClient.getOwnerBookings(ownerId, state, from, size);
    }

    @ExceptionHandler(ConversionFailedException.class)
    public ResponseEntity<Map<String, String>> wrongStateError() {
        return new ResponseEntity<>(
                Map.of("error", "Unknown state: UNSUPPORTED_STATUS"),
                HttpStatus.BAD_REQUEST
        );
    }
}




