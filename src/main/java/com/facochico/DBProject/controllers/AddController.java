package com.facochico.DBProject.controllers;

import com.facochico.DBProject.models.Client;
import com.facochico.DBProject.repo.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.ref.Cleaner;

@Controller
public class AddController {

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Главный экран");
        Iterable<Client> clients = clientRepository.findAll();
        model.addAttribute("clients", clients);
        return "home";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("title", "Создание карточки");
        return "add";
    }

    @PostMapping("/add")
    public String addClient(@RequestParam String name, @RequestParam String patronymic,
                            @RequestParam String surname, @RequestParam String phoneNumber,
                            @RequestParam String bDay, Model model) {
        Client client = new Client(name, patronymic, surname, phoneNumber, bDay);
        System.out.println(client.getPhoneNumber());
        clientRepository.save(client);

        return "redirect:/";
    }
}
