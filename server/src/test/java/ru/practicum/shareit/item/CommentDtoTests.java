package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.CommentDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class CommentDtoTests {

    private final JacksonTester<CommentDto> json;

    @Test
    void commentDto() throws Exception {
        LocalDateTime dateTime = LocalDateTime.now();

        CommentDto commentDto = new CommentDto(
                null,
                "text",
                "authorName",
                dateTime
        );

        JsonContent<CommentDto> result = json.write(commentDto);

        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo(commentDto.getText());
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo(commentDto.getAuthorName());
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(commentDto.getCreated().toString());
    }

}
