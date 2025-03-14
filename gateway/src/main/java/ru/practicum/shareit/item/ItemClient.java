package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.Map;

@Slf4j
@Service
public class ItemClient extends BaseClient {

    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(
            @Value("${shareit-server.url}") String serverUrl,
            RestTemplateBuilder builder
    ) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> create(ItemCreateDto dto, Long userId) {
        log.info("Create item {} and user {}", dto, userId);
        return post("", userId, dto);
    }

    public ResponseEntity<Object> update(ItemUpdateDto dto, Long itemId, Long userId) {
        log.info("Update item {} and user {}", dto, userId);
        return patch("/" + itemId, userId, dto);
    }

    public ResponseEntity<Object> findItemById(Long itemId, Long userId) {
        log.info("Find item {} and user {}", itemId, userId);
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> findItemsByOwner(Long ownerId) {
        log.info("Find items by owner {}", ownerId);
        return get("/owner", ownerId);
    }

    public ResponseEntity<Object> findItemsByText(String text) {
        Map<String, Object> params = Map.of("text", text);
        return get("/search", null, params);
    }

    public ResponseEntity<Object> addComment(Long itemId, Long authorId, CommentCreateDto commentDto) {
        return post("/" + itemId + "/comment", authorId, commentDto);
    }

}
