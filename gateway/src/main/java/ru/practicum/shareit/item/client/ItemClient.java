package ru.practicum.shareit.item.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> create(ItemDto itemDto, long ownerId) {
        return post("", ownerId, itemDto);
    }

    public ResponseEntity<Object> update(long id, ItemDto itemDto, long ownerId) {
        return patch("/" + id, ownerId, itemDto);
    }

    public ResponseEntity<Object> findItemById(long id, long ownerId) {
        return get("/" + id, ownerId);
    }

    public ResponseEntity<Object> findMyItems(long ownerId, long from, long size) {
        return get("/", ownerId, Map.of("from", from, "size", size));
    }

    public ResponseEntity<Object> searchItems(String text, long from, long size) {
        return get("/search?text=" + text, null, Map.of("from", from, "size", size));
    }

    public ResponseEntity<Object> createComment(CommentDto commentDto, long authorId, long itemId) {
        return post("/" + itemId + "/comment", authorId, commentDto);
    }
}
