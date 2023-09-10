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
import java.util.*;

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

        String[] types = {"Вип","Постоянный клиент", "Новый клиент",
                "Потерянный вип клиент", "Потерянный постоянный клиент", "Давно потерянный клиент"};

        String[] typesforOut = {"Вип клиенты","Постоянные клиенты", "Новые клиенты",
                "Потерянные вип клиенты", "Потерянные постоянные клиенты", "Давно потерянные клиенты"};

        Iterable<Client> clients = clientRepository.findAll();

        Map<String, List> mapTypes = new LinkedHashMap<>(); // Создали словарь с ключом - типом клиента и значением - списком клиентов

        int i = 0;
        for (String type : types) {
            List<AdditionalClientInfo> temp = additionalClientInfoRepository.findByType(type);
            mapTypes.put(typesforOut[i], temp);

            if (temp.isEmpty()) {
                mapTypes.remove(typesforOut[i]);
            }
            i += 1;
        }

        Map<Long, Client> clientsById = new HashMap<>(); // Map с id клиента и объектом client - для вывода на экран имени и тд
        for (Client client : clients) {
            clientsById.put(client.getId(), client);
        }

        model.addAttribute("clientsById", clientsById);
        model.addAttribute("mapTypes", mapTypes);

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
                            @RequestParam("socialStatus") String socialStatus, @RequestParam("type") String type,
                            @RequestParam("clothSize") String clothSize, @RequestParam("footSize") String footSize,
                            @RequestParam("lastMsg") String lastMsg, @RequestParam("lastPurchaseDate") String lastPurchase,
                            @RequestParam("address") String address, @RequestParam("description") String description,
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
                type, clothSize, footSize, lastMsg, lastPurchase, address, description, clientPhoto);
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
        // Приведение даты в формат дд/мм/гггг
        if (!res.get(0).getBDay().isEmpty())
            res.get(0).setBDay(parseDate(res.get(0).getBDay()));

        model.addAttribute("client", res);

        Optional<AdditionalClientInfo> additionalClientInfo = additionalClientInfoRepository.findById(id);
        ArrayList<AdditionalClientInfo> res2 = new ArrayList<>();
        additionalClientInfo.ifPresent(res2::add);
        // Приведение даты в формат дд/мм/гггг
        if (!res2.get(0).getLastMsg().isEmpty()) {
            res2.get(0).setLastMsg(parseDate(res2.get(0).getLastMsg()));
            res2.get(0).setLastPurchase(parseDate(res2.get(0).getLastPurchase()));
        }

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
                               @RequestParam("socialStatus") String socialStatus, @RequestParam("type") String type,
                               @RequestParam("clothSize") String clothSize, @RequestParam("footSize") String footSize,
                               @RequestParam("lastMsg") String lastMsg, @RequestParam("lastPurchaseDate") String lastPurchase,
                               @RequestParam("address") String address, @RequestParam("description") String description,
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
        additionalClientInfo.setType(type);
        additionalClientInfo.setClothSize(clothSize);
        additionalClientInfo.setFootSize(footSize);
        additionalClientInfo.setLastMsg(lastMsg);
        additionalClientInfo.setLastPurchase(lastPurchase);
        additionalClientInfo.setAddress(address);
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

    private String parseDate(String str) {

        String year = str.substring(0, 4);
        String month = str.substring(5, 7);
        String day = str.substring(8);

        return day + "." + month + "." + year;
    }
}