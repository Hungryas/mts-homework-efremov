package org.example.controllers;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.example.errors.CustomException;
import org.example.errors.UserNotFoundException;
import org.example.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@RestController
@Validated
@RequestMapping("/user")
public class UserController {

    @GetMapping("/hello")
    public String greetings() {
        return "Hello, world!";
    }

    @GetMapping("/greet")
    public String greetingsName(@RequestParam(defaultValue = "Guest") String name) {
        return "Hello, %s!".formatted(name);
    }

    @PostMapping("/greet")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public String greetingsUser(@RequestBody @Valid User user) {
        return user.getName();
    }

    @PostMapping("/register")
    public String register(@RequestBody @Valid User user) {
        return "%s registered!".formatted(user.getName());
    }

    @GetMapping("/{name}")
    public String findName(@PathVariable String name, @RequestParam String id) {
        if (id == null || id.isEmpty()) {
            throw new UserNotFoundException();
        }
        return "%s : %s".formatted(name, id);
    }

    @GetMapping("/agent")
    @PreAuthorize("hasAuthority('USER')")
    public String getUserAgent(@RequestHeader("User-Agent") String userAgent) {
        return userAgent;
    }

    @GetMapping("/validate/{date}")
    public ResponseEntity<String> validateDate(@PathVariable String date) {
        String pattern = "\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])";

        if (!date.matches(pattern)) {
            throw new CustomException("%s is invalid. Required format: yyyy-MM-dd".formatted(date));
        }
        return ResponseEntity.ok("%s is valid date".formatted(date));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    private Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CustomException.class)
    private String handleCustomExceptions(CustomException ex) {
        return ex.getMessage();
    }
}