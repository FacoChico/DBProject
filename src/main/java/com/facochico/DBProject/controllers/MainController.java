package com.facochico.DBProject.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "О приложении");
        return "about";
    }

    @GetMapping("/info")
    public String info(Model model) {
        model.addAttribute("title", "Информация");
        return "info";
    }
}