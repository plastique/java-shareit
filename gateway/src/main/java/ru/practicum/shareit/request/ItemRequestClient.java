package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;

@Slf4j
@Service
public class ItemRequestClient extends BaseClient {

    private static final String API_PREFIX = "/requests";

    @Autowired
    public ItemRequestClient(
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

    public ResponseEntity<Object> create(Long userId, ItemRequestCreateDto itemRequestCreateDto) {
        log.info("Create itemRequest for userId={}, dto: {}", userId, itemRequestCreateDto);
        return post("", userId,  itemRequestCreateDto);
    }

    public ResponseEntity<Object> getListByUser(Long userId) {
        log.info("Get itemRequests for userId={}", userId);
        return get("", userId);
    }

    public ResponseEntity<Object> getList(Long userId) {
        log.info("Get all itemRequests for userId={}", userId);
        return get("/all", userId);
    }

    public ResponseEntity<Object> getById(Long requestId) {
        log.info("Get itemRequest for requestId={}", requestId);
        return get("/" + requestId);
    }

}
