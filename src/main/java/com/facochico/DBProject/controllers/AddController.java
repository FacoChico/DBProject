package com.facochico.DBProject.controllers;

import com.facochico.DBProject.models.AdditionalClientInfo;
import com.facochico.DBProject.models.Client;
import com.facochico.DBProject.repo.AdditionalClientInfoRepository;
import com.facochico.DBProject.repo.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class AddController {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AdditionalClientInfoRepository additionalClientInfoRepository;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Главный экран");
        Iterable<Client> clients = clientRepository.findAll();
        Iterable<AdditionalClientInfo> additionalClientInfos = additionalClientInfoRepository.findAll();
        model.addAttribute("clients", clients);
        model.addAttribute("additionalClientInfos", additionalClientInfos);
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
                            @RequestParam String bDay, @RequestParam String footSize,
                            @RequestParam String clothSize, @RequestParam String lastMsg,
                            @RequestParam String description, Model model) {
        Client client = new Client(name, patronymic, surname, phoneNumber, bDay);
        clientRepository.save(client);

        AdditionalClientInfo additionalClientInfo = new AdditionalClientInfo(client.getId(), clothSize, footSize, lastMsg, description);
        additionalClientInfoRepository.save(additionalClientInfo);

        return "redirect:/";
    }
}
