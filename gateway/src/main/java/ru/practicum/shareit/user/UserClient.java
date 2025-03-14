package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

@Slf4j
@Service
public class UserClient extends BaseClient {

    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(
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

    public ResponseEntity<Object> create(UserCreateDto dto) {
        log.info("Create request for user={}", dto);
        return post("", dto);
    }

    public ResponseEntity<Object> update(Long userId, UserUpdateDto dto) {
        log.info("Update request for user={}, dto: {}", userId,  dto);
        return patch("/" + userId, dto);
    }

    public ResponseEntity<Object> deleteById(Long userId) {
        log.info("Delete request for user={}", userId);
        return delete("/" + userId);
    }

    public ResponseEntity<Object> findById(Long userId) {
        log.info("Find request for user={}", userId);
        return get("/" + userId);
    }

}
