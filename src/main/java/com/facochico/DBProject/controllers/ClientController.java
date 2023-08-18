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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
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
    public String addClient(@RequestParam("firstName") String name, @RequestParam("patronymic") String patronymic,
                            @RequestParam("lastName") String surname, @RequestParam("sex") String sex,
                            @RequestParam("phoneNumber") String phoneNumber, @RequestParam("birthDay") String bDay,
                            @RequestParam("socialStatus") String socialStatus, @RequestParam("clothSize") String clothSize,
                            @RequestParam("footSize") String footSize, @RequestParam("lastMsg") String lastMsg,
                            @RequestParam("lastPurchaseDate") String lastPurchase, @RequestParam("description") String description,
                            @RequestParam(value = "file", required = false) MultipartFile file) {

        Client client = new Client(name, patronymic, surname, sex, phoneNumber, bDay);
        clientRepository.save(client);

        byte[] clientPhoto = null;

        if (file != null) {
            try {
                clientPhoto = new byte[file.getBytes().length];
                int i = 0;
                for (byte b : file.getBytes()) {
                    clientPhoto[i++] = b;
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        AdditionalClientInfo additionalClientInfo = new AdditionalClientInfo(client.getId(), socialStatus,
                clothSize, footSize, lastMsg, lastPurchase, description, clientPhoto);
        additionalClientInfoRepository.save(additionalClientInfo);


        return "redirect:/";
    }

    @GetMapping("/client{id}")
    public String viewCard(@PathVariable(value = "id") long id, Model model) {

        if(!clientRepository.existsById(id) || !additionalClientInfoRepository.existsById(id)) {
            return "redirect:/";
        }

        model.addAttribute("title", "Карточка клиента");

        if(additionalClientInfoRepository.findById(id).get().getClientPhoto() != null) {
            byte[] encodeBase64 = Base64.getEncoder().encode(additionalClientInfoRepository.findById(id).get().getClientPhoto());
            String clientPhoto = new String(encodeBase64, StandardCharsets.UTF_8);
            model.addAttribute("clientPhoto", clientPhoto);
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

        model.addAttribute("title", "Редактирование карточки");

        if(additionalClientInfoRepository.findById(id).get().getClientPhoto() != null) {
            byte[] encodeBase64 = Base64.getEncoder().encode(additionalClientInfoRepository.findById(id).get().getClientPhoto());
            String clientPhoto = new String(encodeBase64, StandardCharsets.UTF_8);
            model.addAttribute("clientPhoto", clientPhoto);
        }

        Optional<Client> client = clientRepository.findById(id);
        ArrayList<Client> res = new ArrayList<>();
        client.ifPresent(res::add);
        model.addAttribute("client", res);

        Optional<AdditionalClientInfo> additionalClientInfo = additionalClientInfoRepository.findById(id);
        ArrayList<AdditionalClientInfo> res2 = new ArrayList<>();
        additionalClientInfo.ifPresent(res2::add);
        model.addAttribute("additionalClientInfo", res2);

        model.addAttribute("clientId", id);

        return "client-card-edit";
    }

    @PostMapping("/client{id}/edit")
    public String clientUpdate(@PathVariable("id") long id,
                               @RequestParam("firstName") String name, @RequestParam("patronymic") String patronymic,
                               @RequestParam("lastName") String surname, @RequestParam("sex") String sex,
                               @RequestParam("phoneNumber") String phoneNumber, @RequestParam("birthDay") String bDay,
                               @RequestParam("socialStatus") String socialStatus, @RequestParam("clothSize") String clothSize,
                               @RequestParam("footSize") String footSize, @RequestParam("lastMsg") String lastMsg,
                               @RequestParam("lastPurchaseDate") String lastPurchase, @RequestParam("description") String description,
                               @RequestParam(value = "file", required = false) MultipartFile file) {

        Client client = clientRepository.findById(id).orElseThrow(); // orElseThrow() выбрасывает исключение в случае, если запись была не найдена

        client.setName(name);
        client.setSurname(surname);
        client.setPatronymic(patronymic);
        client.setSex(sex);
        client.setPhoneNumber(phoneNumber);
        client.setBDay(bDay);
        clientRepository.save(client);

        AdditionalClientInfo additionalClientInfo = additionalClientInfoRepository.findById(id).orElseThrow();

        byte[] clientPhoto = null;

        if (file != null) {
            try {
                clientPhoto = new byte[file.getBytes().length];
                int i = 0;
                for (byte b : file.getBytes()) {
                    clientPhoto[i++] = b;
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        additionalClientInfo.setSocialStatus(socialStatus);
        additionalClientInfo.setClothSize(clothSize);
        additionalClientInfo.setFootSize(footSize);
        additionalClientInfo.setLastMsg(lastMsg);
        additionalClientInfo.setLastPurchase(lastPurchase);
        additionalClientInfo.setDescription(description);

        if (clientPhoto != null)
            additionalClientInfo.setClientPhoto(clientPhoto);

        additionalClientInfoRepository.save(additionalClientInfo);

        return "redirect:/client{id}";
    }

    @PostMapping("/client{id}/remove")
    public String clientDelete(@PathVariable(value = "id") long id) {

        Client client = clientRepository.findById(id).orElseThrow();
        clientRepository.delete(client);

        AdditionalClientInfo additionalClientInfo = additionalClientInfoRepository.findById(id).orElseThrow();
        additionalClientInfoRepository.delete(additionalClientInfo);

        List<ClientOrder> clientOrders = orderRepository.findByClientId(id);
        orderRepository.deleteAll(clientOrders);

        return "redirect:/";
    }

    @PostMapping("/client{id}/deletePhoto")
    public String deletePhoto(@PathVariable(value = "id") long id) {

        AdditionalClientInfo additionalClientInfo = additionalClientInfoRepository.findById(id).orElseThrow();
        additionalClientInfo.setClientPhoto(null);
        additionalClientInfoRepository.save(additionalClientInfo);

        return "redirect:/client{id}";
    }
}