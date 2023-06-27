package com.facochico.DBProject.controllers;

import com.facochico.DBProject.models.AdditionalClientInfo;
import com.facochico.DBProject.models.Client;
import com.facochico.DBProject.models.ClientOrder;
import com.facochico.DBProject.repo.AdditionalClientInfoRepository;
import com.facochico.DBProject.repo.ClientRepository;
import com.facochico.DBProject.repo.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Optional;


@Controller
public class ClientController {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AdditionalClientInfoRepository additionalClientInfoRepository;
    @Autowired
    private OrderRepository orderRepository;

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
    public String addClientMapping(Model model) {
        model.addAttribute("title", "Создание карточки");
        return "client-add";
    }

    @PostMapping("/add")
    public String addClient(@RequestParam String name, @RequestParam String patronymic,
                            @RequestParam String surname, @RequestParam String phoneNumber,
                            @RequestParam String bDay, @RequestParam String clothSize,
                            @RequestParam String footSize,  @RequestParam String lastMsg,
                            @RequestParam String description) {
        Client client = new Client(name, patronymic, surname, phoneNumber, bDay);
        clientRepository.save(client);

        AdditionalClientInfo additionalClientInfo = new AdditionalClientInfo(client.getId(), clothSize, footSize, lastMsg, description);
        additionalClientInfoRepository.save(additionalClientInfo);

        return "redirect:/";
    }

    @GetMapping("/client{id}")
    public String viewCard(@PathVariable(value = "id") long id, Model model) {

        // Проверка наличия в базе клиента и информации с данным id
        if(!clientRepository.existsById(id) || !additionalClientInfoRepository.existsById(id)) {
            return "redirect:/";
        }

        Optional<Client> client = clientRepository.findById(id);
        ArrayList<Client> res = new ArrayList<>();
        client.ifPresent(res::add);
        model.addAttribute("client", res);

        Optional<AdditionalClientInfo> additionalClientInfo = additionalClientInfoRepository.findById(id);
        ArrayList<AdditionalClientInfo> res2 = new ArrayList<>();
        additionalClientInfo.ifPresent(res2::add);
        model.addAttribute("additionalClientInfo", res2);

        Iterable<ClientOrder> orders = orderRepository.findByClientId(id);
        model.addAttribute("clientOrder", orders);

        return "client-card";
    }

    @GetMapping("/client{id}/edit")
    public String clientEdit(@PathVariable(value = "id") long id, Model model) {
        if (!clientRepository.existsById(id) || !additionalClientInfoRepository.existsById(id)) {
            return "redirect:/";
        }

        Optional<Client> client = clientRepository.findById(id);
        ArrayList<Client> res = new ArrayList<>();
        client.ifPresent(res::add);
        model.addAttribute("client", res);

        Optional<AdditionalClientInfo> additionalClientInfo = additionalClientInfoRepository.findById(id);
        ArrayList<AdditionalClientInfo> res2 = new ArrayList<>();
        additionalClientInfo.ifPresent(res2::add);
        model.addAttribute("additionalClientInfo", res2);

        return "client-card-edit";
    }

    @PostMapping("/client{id}/edit")
    public String clientUpdate(@PathVariable(value = "id") long id, @RequestParam String name, @RequestParam String patronymic,
                               @RequestParam String surname, @RequestParam String phoneNumber,
                               @RequestParam String bDay, @RequestParam String clothSize,
                               @RequestParam String footSize,  @RequestParam String lastMsg,
                               @RequestParam String description) {

        Client client = clientRepository.findById(id).orElseThrow(); // orElseThrow() выбрасывает исключение в случае, если запись была не найдена

        client.setName(name);
        client.setSurname(surname);
        client.setPatronymic(patronymic);
        client.setPhoneNumber(phoneNumber);
        client.setBDay(bDay);
        clientRepository.save(client);

        AdditionalClientInfo additionalClientInfo = additionalClientInfoRepository.findById(id).orElseThrow();

        additionalClientInfo.setClothSize(clothSize);
        additionalClientInfo.setFootSize(footSize);
        additionalClientInfo.setLastMsg(lastMsg);
        additionalClientInfo.setDescription(description);
        additionalClientInfoRepository.save(additionalClientInfo);

        return "redirect:/client{id}";
    }

    @PostMapping("/client{id}/remove")
    public String remove(@PathVariable(value = "id") long id) {

        Client client = clientRepository.findById(id).orElseThrow();
        clientRepository.delete(client);

        AdditionalClientInfo additionalClientInfo = additionalClientInfoRepository.findById(id).orElseThrow();
        additionalClientInfoRepository.delete(additionalClientInfo);

        return "redirect:/";
    }
}
