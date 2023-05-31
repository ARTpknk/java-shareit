package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.model.OwnerNotFoundException;
import ru.practicum.shareit.exceptions.model.ShareItNotFoundException;
import ru.practicum.shareit.item.dto.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final UserService userService;

    @Override
    public Item create(Item item, int ownerId) {
        if (userService.getUserById(ownerId) == null) {
            throw new OwnerNotFoundException("Owner not found");
        }
        item.setOwnerId(ownerId);
        return repository.save(item);
    }

    @Override
    public Item update(Item item, int ownerId) {
        if (userService.getUserById(ownerId) == null) {
            throw new OwnerNotFoundException("Owner not found");
        }
        int id = item.getId();
        if(repository.findById(id).isPresent()){
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
        }
        else {
            throw new ShareItNotFoundException(String.format("Вещь с таким id: %d не найдена.", id));
        }


    }

    @Override
    public List<Item> getMyItems(int ownerId) {
        return repository.findItemsByOwnerId(ownerId);
    }



    @Override
    public Item getItemById(Integer id) {
        if(repository.findById(id).isPresent()){
            return repository.findById(id).get();
        }
        else{
            throw new OwnerNotFoundException("вещь не найдена");
        }
    }

    @Override
    public List<Item> searchItems(String text) {
        return repository.search(text);
    }



}
