package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.example.dao.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final User user = User.builder()
            .name("tester")
            .email("tester@mail.com")
            .build();

    @Test
    @DisplayName("Позитивный тест GET greetings")
    @SneakyThrows
    void successGetGreetings() {
        var requestBuilder = get("/user/hello");

        mvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, world!"));
    }

    @Test
    @DisplayName("Позитивный тест GET greet с name")
    @SneakyThrows
    void successGetGreetingsName() {
        var requestBuilder = get("/user/greet")
                .param("name", "tester");

        mvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, tester!"));
    }

    @Test
    @DisplayName("Позитивный тест GET greet без name")
    @SneakyThrows
    void successGetGreetingsNameWithDefaultName() {
        var requestBuilder = get("/user/greet");

        mvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, Guest!"));
    }

    @Test
    @DisplayName("Позитивный тест POST greet")
    @SneakyThrows
    void successPostGreetingsUser() {
        var requestBuilder = post("/user/greet")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user));

        mvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(user.getName()));
    }

    @Test
    @DisplayName("Позитивный тест register")
    @SneakyThrows
    void successPostRegister() {
        var requestBuilder = post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user));

        mvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("%s registered!".formatted(user.getName())));
    }

    @ParameterizedTest(name = "{displayName} [{index}] name=[{0}]")
    @DisplayName("Негативный тест register с некорректным name")
    @CsvSource(textBlock = """
            null, must not be null
            ''  , size must be between 3 and 250
            ab  , size must be between 3 and 250
            """, nullValues = "null")
    @SneakyThrows
    void failurePostRegisterWithBadName(String name, String errorMessage) {
        User badUser = user.toBuilder()
                .name(name)
                .build();
        var requestBuilder = post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(badUser));

        mvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("name").value(errorMessage));
    }

    @ParameterizedTest(name = "{displayName} [{index}] email=[{0}]")
    @DisplayName("Негативный тест register с некорректным email")
    @CsvSource({"tester.ru", "tester@", "tester@.ru", "@mail.ru"})
    @SneakyThrows
    void failurePostRegisterWithBadEmail(String email) {
        User badUser = user.toBuilder()
                .email(email)
                .build();
        var requestBuilder = post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(badUser));

        mvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("email").value("must be a well-formed email address"));
    }

    @Test
    @DisplayName("Позитивный тест findName")
    @SneakyThrows
    void successFindName() {
        String id = UUID.randomUUID().toString();
        var requestBuilder = get("/user/{name}", user.getName())
                .param("id", id);

        mvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("%s : %s".formatted(user.getName(), id)));
    }

    @Test
    @DisplayName("Негативный тест findName с пустым id")
    @SneakyThrows
    void failureFindNameWithEmptyId() {
        var requestBuilder = get("/user/{name}", user.getName())
                .param("id", StringUtils.EMPTY);

        mvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("Пользователь не найден!"));
    }

    @Test
    @DisplayName("Позитивный тест getUserAgent")
    @SneakyThrows
    void successGetAgent() {
        String userAgent = "Mozilla/5.0";
        var requestBuilder = get("/user/agent")
                .header("User-Agent", userAgent);

        mvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(userAgent));
    }

    @Test
    @DisplayName("Позитивный тест validateDate")
    @SneakyThrows
    void successValidateDate() {
        String date = "2024-10-06";
        var requestBuilder = get("/user/validate/{date}", date);

        mvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("%s is valid date".formatted(date)));
    }

    @ParameterizedTest(name = "{displayName} [{index}] date=[{0}]")
    @DisplayName("Негативный тест register с некорректным date")
    @CsvSource({"3-12-12", "2003-13-12", "2200-00-12", "2013-12-32", "2200-12-00"})
    @SneakyThrows
    void failureValidateDateWithBadDate(String date) {
        var requestBuilder = get("/user/validate/{date}", date);

        mvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("%s is invalid. Required format: yyyy-MM-dd".formatted(date)));
    }
}