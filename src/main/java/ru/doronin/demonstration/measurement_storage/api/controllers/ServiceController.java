package ru.doronin.demonstration.measurement_storage.api.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.doronin.demonstration.measurement_storage.user.UserService;
import ru.doronin.demonstration.measurement_storage.user.base.User;

/**
 * API общего назначения
 */
@RestController
public class ServiceController {

    private final UserService userService;

    public ServiceController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Обработка получения роли пользователя
     *
     * @return
     */
    @GetMapping(value = "/role", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JsonNode getRole() {
        User current = userService.getCurrent();
        if (current == null) {
            throw new AccessDeniedException("Access denied");
        }
        ObjectNode node = new ObjectNode(JsonNodeFactory.instance);
        node.set("role", new TextNode(current.getRole().name()));
        return node;
    }
}
