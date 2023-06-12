package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.model.OwnerNotFoundException;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RequestServiceImpl implements RequestService {
    private final RequestRepository repository;
    private final UserService userService;

    @Override
    public Request create(Request request, int requestorId) {
        User requestor = userService.getUserById(requestorId);
        if (requestor == null) {
            throw new OwnerNotFoundException("Requestor with Id: " + requestorId + " not found");
        }
        return repository.save(request);
    }

    @Override
    public List<Request> getMyRequests(int userId) {
        if (userService.getUserById(userId) == null) {
            throw new OwnerNotFoundException("Requestor with Id: " + userId + " not found");
        }
        return repository.findAllByRequestorIdOrderByCreatedDesc(userId);
    }

    @Override
    public List<Request> getByUserIdAndRequestId(int userId, int from, int size) {
        if (userService.getUserById(userId) == null) {
            throw new OwnerNotFoundException("Requestor with Id: " + userId + " not found");
        }
        return repository.findRequests(userId, size, from);
    }

    @Override
    public Request getRequest(int userId, int requestId) {
        if (userService.getUserById(userId) == null) {
            throw new OwnerNotFoundException("Requestor with Id: " + userId + " not found");
        }
        if (repository.findById(requestId).isPresent()) {
            return repository.findById(requestId).get();
        } else {
            throw new OwnerNotFoundException("Requestor with Id: " + userId + " not found");
        }
    }
}
