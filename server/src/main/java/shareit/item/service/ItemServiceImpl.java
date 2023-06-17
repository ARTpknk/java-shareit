package shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shareit.booking.model.Booking;
import shareit.booking.repository.BookingRepository;
import shareit.exceptions.model.OwnerNotFoundException;
import shareit.exceptions.model.ShareItBadRequest;
import shareit.exceptions.model.ShareItNotFoundException;
import shareit.item.comment.Comment;
import shareit.item.comment.CommentRepository;
import shareit.item.model.Item;
import shareit.item.repository.ItemRepository;
import shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Transactional
    @Override
    public Item create(Item item, int ownerId) {
        if (userService.getUserById(ownerId) == null) {
            throw new OwnerNotFoundException("Owner with Id: " + ownerId + " not found");
        }
        item.setOwnerId(ownerId);
        return repository.save(item);
    }

    @Override
    public Item update(Item item, int ownerId) {
        if (userService.getUserById(ownerId) == null) {
            throw new OwnerNotFoundException("Owner with Id: " + ownerId + " not found");
        }
        int id = item.getId();
        if (repository.findById(id).isPresent()) {
            item.setOwnerId(ownerId);
            Item updateItem = repository.findById(id).get();
            String name = item.getName();
            String description = item.getDescription();
            Boolean available = item.getAvailable();
            if (name != null && !name.isBlank()) {
                updateItem.setName(name);
            }
            if (description != null && !description.isBlank()) {
                updateItem.setDescription(description);
            }
            if (available != null) {
                updateItem.setAvailable(available);
            }
            return repository.save(updateItem);
        } else {
            throw new ShareItNotFoundException(String.format("Вещь с таким id: %d не найдена.", id));
        }
    }

    @Override
    public List<Item> getMyItems(int ownerId, int size, int from) {
        return repository.findByOwnerId(ownerId, PageRequest.of(from, size)).toList();
    }

    @Override
    public Item getItemById(Integer id) {
        if (repository.findById(id).isPresent()) {
            return repository.findById(id).get();
        } else {
            throw new OwnerNotFoundException(String.format("Вещь с таким id: %d не найдена.", id));
        }
    }

    @Override
    public List<Item> searchItems(String text, int size, int from) {
        return repository.search(text, PageRequest.of(from, size)).toList();
    }

    @Override
    public Booking getLastBooking(int itemId, int userId) {
        return bookingRepository.findLastBooking(itemId, LocalDateTime.now(), userId);
    }

    @Override
    public Booking getNextBooking(int itemId, int userId) {
        return bookingRepository.findNextBooking(itemId, LocalDateTime.now(), userId);
    }

    @Override
    public Comment createComment(Comment comment) {
        if (comment.getText().isBlank()) {
            throw new ShareItBadRequest("пустой комментарий");
        }
        if (bookingRepository.findUsedBookings(comment.getItemId(), comment.getAuthorId(), LocalDateTime.now()).isEmpty()) {
            throw new ShareItBadRequest("Вы не пользовались");
        }
        comment.setCreated(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> getComments(int itemId) {
        return commentRepository.findAllByItemId(itemId);
    }

    @Override
    public String getUserName(int userId) {
        return userService.getUserById(userId).getName();
    }

    @Override
    public List<Item> getItemsByRequest(int requestId) {
        return repository.findItemsByRequestId(requestId);
    }
}
