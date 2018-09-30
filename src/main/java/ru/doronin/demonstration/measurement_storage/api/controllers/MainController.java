package ru.doronin.demonstration.measurement_storage.api.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Контроллер для отображений
 */
@Controller
public class MainController {

    /**
     * Отображение странички логина
     *
     * @return
     */
    @GetMapping("/login")
    public ModelAndView requestLoginPage() {
        return new ModelAndView("login");
    }
}
