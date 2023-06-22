package com.facochico.DBProject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AddController {
    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("title", "Создание карточки");
        return "add";
    }
}
