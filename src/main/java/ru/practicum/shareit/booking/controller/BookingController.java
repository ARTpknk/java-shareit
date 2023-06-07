package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.classes.Create;
import ru.practicum.shareit.classes.Update;
import ru.practicum.shareit.exceptions.model.ShareItBadRequest;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@Slf4j
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto create(@Validated(Create.class) @RequestBody BookingDto bookingDto,
                             @RequestHeader("X-Sharer-User-Id") int bookerId) {
        bookingDto.setStatus(BookingStatus.WAITING);
        Booking booking = BookingMapper.toBooking(bookingDto, bookerId);
        log.info(String.format("BookingController: create Booking request. Data: %s", booking));
        return BookingMapper.toBookingDto(bookingService.create(booking, bookerId));
    }

    @PatchMapping("/{bookingId}")
    public BookingDto update(@Validated(Update.class) @PathVariable("bookingId") Integer id,
                             @RequestHeader("X-Sharer-User-Id") int ownerId,
                             @RequestParam(required = false, defaultValue = "true") boolean approved) {
        return BookingMapper.toBookingDto(bookingService.update(id, ownerId, approved));
    }

    @GetMapping("/{bookingId}")
    public BookingDto getMyBooking(@PathVariable("bookingId") Integer id,
                                   @RequestHeader("X-Sharer-User-Id") int userId) {
        return BookingMapper.toBookingDto(bookingService.getMyBooking(id, userId));
    }

    @GetMapping
    public List<BookingDto> getMyBookings(@RequestHeader("X-Sharer-User-Id") int userId,
                                          @RequestParam(required = false, defaultValue = "ALL") State state,
                                          @RequestParam(required = false, defaultValue = "0") int from,
                                          @RequestParam(required = false, defaultValue = "20") int size) {

        System.out.println(from + size);
        if(from<0 || size <= 0){
            throw new ShareItBadRequest("некорректные значения");
        }
        else{
            return bookingService.getMyBookings(userId, state, from, size)
                    .stream().map(BookingMapper::toBookingDto)
                    .collect(Collectors.toList());
        }
    }

    @GetMapping("/owner")
    public List<BookingDto> getOwnerBookings(@RequestHeader("X-Sharer-User-Id") int ownerId,
                                             @RequestParam(required = false, defaultValue = "ALL") State state,
                                             @RequestParam(required = false, defaultValue = "0") int from,
                                             @RequestParam(required = false, defaultValue = "20") int size) {

        if(from<0 || size <= 0){
            throw new ShareItBadRequest("некорректные значения");
        }
        else{
            return bookingService.getOwnerBookings(ownerId, state, from, size)
                    .stream().map(BookingMapper::toBookingDto)
                    .collect(Collectors.toList());
        }
    }

    @ExceptionHandler(ConversionFailedException.class)
    public ResponseEntity<Map<String, String>> wrongStateError() {
        return new ResponseEntity<>(
                Map.of("error", "Unknown state: UNSUPPORTED_STATUS"),
                HttpStatus.BAD_REQUEST
        );
    }
}
