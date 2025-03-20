package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class RequestResponseDtoTests {

    private final JacksonTester<ItemRequestResponseDto> json;

    @Test
    void itemRequestResponseDto() throws Exception {
        ItemRequestResponseDto itemRequestResponseDto = new ItemRequestResponseDto(
                null,
                "description",
                LocalDateTime.now(),
                List.of(
                        new ItemRequestResponseDto.ItemDto(null, "item name")
                )
        );

        JsonContent<ItemRequestResponseDto> result = json.write(itemRequestResponseDto);

        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(itemRequestResponseDto.getDescription());
    }

}
