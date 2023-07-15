package com.facochico.DBProject.controllers;

import com.facochico.DBProject.models.Client;
import com.facochico.DBProject.repo.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

@Controller
public class SearchController {
    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/search")
    public String search(@RequestParam(name="query") String query, Model model) {
        if (!query.isEmpty()) {
            Iterable<Client> clients = clientRepository.findAll();
            ArrayList<Client> results = new ArrayList<>();

            for (Client client : clients) {
                if(client.matchesFullName(query) || client.getPhoneNumber().contains(query))
                    results.add(client);
            }

            model.addAttribute("results", results);
            model.addAttribute("query", query);

            return "search";
        }

        return "redirect:/";
    }
}