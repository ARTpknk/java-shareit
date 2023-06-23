package shareit.request.service;

import shareit.request.model.Request;

import java.util.List;

public interface RequestService {
    Request create(Request request, int requestorId);

    List<Request> getMyRequests(int userId);

    List<Request> getByUserIdAndRequestId(int userId, int from, int size);

    Request getRequest(int userId, int requestId);
}
