package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.model.OwnerNotFoundException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RequestServiceImpl implements RequestService{
    private final RequestRepository repository;
    private final UserService userService;
    private final ItemService itemService;

    @Override
    public Request create(Request request, int requestorId) {
        User requestor = userService.getUserById(requestorId);
        if (requestor == null) {
            throw new OwnerNotFoundException("Owner not found");
        }
        return repository.save(request);
    }

    @Override
    public List<Request> getMyRequests(int userId) {
        if (userService.getUserById(userId) == null) {
            throw new OwnerNotFoundException("Owner not found");
        }
        return repository.findAllByRequestorIdOrderByCreatedDesc(userId);
    }

    @Override
    public List<Request> getUserRequests(int userId, int from, int size) {
        if (userService.getUserById(userId) == null) {
            throw new OwnerNotFoundException("Owner not found");
        }
        int limit = from + size;
        List<Request> requests = repository.findUsersRequests(userId, limit);
        for(int i = from; i>0; i--){
            requests.remove(0);
        }
        return requests;
    }

    @Override
    public Request getRequest(int userId, int requestId) {
        if (userService.getUserById(userId) == null) {
            throw new OwnerNotFoundException("Owner not found");
        }
        if (repository.findById(requestId).isPresent()) {
            return repository.findById(requestId).get();
        }
        else{
            throw new OwnerNotFoundException("Request не найден");
        }
    }

}
