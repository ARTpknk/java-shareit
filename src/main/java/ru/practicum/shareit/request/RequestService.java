package ru.practicum.shareit.request;

import java.util.List;

public interface RequestService {
    Request create(Request request, int requestorId);
    List<Request> getMyRequests(int userId);
    List<Request> getUserRequests(int userId, int from, int size);
    Request getRequest(int userId, int requestId);
}
