package ru.practicum.shareit.request.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.RequestDto;

import java.util.Map;

@Service
public class RequestClient extends BaseClient {

    private static final String API_PREFIX = "/requests";

    @Autowired
    public RequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> create(RequestDto requestDto, long requestorId) {
        return post("", requestorId, requestDto);
    }

    public ResponseEntity<Object> getMyRequests(long requestorId) {
        return get("", requestorId);
    }

    public ResponseEntity<Object> getRequests(long requestorId, long from, long size) {
        return get("/all", requestorId, Map.of("from", from, "size", size));
    }

    public ResponseEntity<Object> getRequest(long requestorId, long requestId) {
        return get("/" + requestId, requestorId);
    }

}
