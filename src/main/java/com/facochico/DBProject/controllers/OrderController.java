package com.facochico.DBProject.controllers;


import com.facochico.DBProject.models.Client;
import com.facochico.DBProject.models.ClientOrder;
import com.facochico.DBProject.repo.ClientRepository;
import com.facochico.DBProject.repo.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;

@Controller
public class OrderController {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ClientRepository clientRepository;
    public static volatile boolean isReady = false;

    @GetMapping("/client{clientId}/new-order")
    public String newOrderMapping(@PathVariable(value = "clientId") Long clientId, Model model) {
        model.addAttribute("title", "Создание заказа");
        model.addAttribute("clientId", clientId);
        return "order-add";
    }

    @PostMapping("/client{clientId}/new-order")
    public String newOrder(@PathVariable(value = "clientId") long clientId,
                           @RequestParam String category, @RequestParam String brand,
                           @RequestParam String size, @RequestParam String orderDate,
                           @RequestParam String description) {

        ClientOrder clientOrder = new ClientOrder(clientId, category, brand, size, orderDate, description);
        orderRepository.save(clientOrder);

        while (!isReady) Thread.onSpinWait();

        String resourcePath = Paths.get("target" + File.separator + "classes" + File.separator
                + "static" + File.separator + "uploads").toAbsolutePath() + File.separator;
        File file = new File(resourcePath + "newImage");
        File newFile = new File(resourcePath + "order" + clientOrder.getId() + ".jpeg");

        file.renameTo(newFile);

        System.out.println("Order. Изменение состояния на FALSE в OrderController при СОЗДАНИИ");
        isReady = false;

        return "redirect:/client{clientId}";
    }

    @GetMapping("/client{clientId}/order{orderId}")
    public String viewOrder(@PathVariable(value = "clientId") Long clientId,
                        @PathVariable(value = "orderId") Long orderId, Model model) {

        if(!orderRepository.existsById(orderId)) {
            return "redirect:/";
        }

        String title = "Карточка заказа";
        model.addAttribute("title", title);

        String photoPath = File.separator + Paths.get("uploads") + File.separator + "order" + orderId + ".jpeg";
        model.addAttribute("photoPath", photoPath);

        Optional<Client> client = clientRepository.findById(clientId);
        ArrayList<Client> res = new ArrayList<>();
        client.ifPresent(res::add);
        model.addAttribute("client", res);

        Optional<ClientOrder> order = orderRepository.findById(orderId);
        ArrayList<ClientOrder> res2 = new ArrayList<>();
        order.ifPresent(res2::add);
        model.addAttribute("order", res2);

        return "order-card";
    }

    @GetMapping("/client{clientId}/order{orderId}/edit")
    public String orderEdit(@PathVariable(value = "orderId") Long orderId,
                            @PathVariable(value = "clientId") Long clientId,
                            Model model) {

        if(!orderRepository.existsById(orderId)) {
            return "redirect:/";
        }

        String title = "Редактирование карточки клиента";
        model.addAttribute("title", title);

        String photoPath = File.separator + Paths.get("uploads") + File.separator + "order" + orderId + ".jpeg";
        model.addAttribute("photoPath", photoPath);

        Optional<ClientOrder> order = orderRepository.findById(orderId);
        ArrayList<ClientOrder> res = new ArrayList<>();
        order.ifPresent(res::add);
        model.addAttribute("order", res);

        model.addAttribute("clientId", clientId);

        return "order-card-edit";
    }

    @PostMapping("/client{clientId}/order{orderId}/edit")
    public String orderUpdate(@PathVariable(value = "orderId") Long orderId,
                              @RequestParam String category, @RequestParam String brand,
                              @RequestParam String size, @RequestParam String orderDate,
                              @RequestParam String description) {

        ClientOrder clientOrder = orderRepository.findById(orderId).orElseThrow();

        clientOrder.setCategory(category);
        clientOrder.setBrand(brand);
        clientOrder.setSize(size);
        clientOrder.setOrderDate(orderDate);
        clientOrder.setDescription(description);

        orderRepository.save(clientOrder);

        while (!isReady) Thread.onSpinWait();

        String resourcePath = Paths.get("target" + File.separator + "classes" + File.separator
                + "static" + File.separator + "uploads").toAbsolutePath() + File.separator;
        File file = new File(resourcePath + "newImage");
        File newFile = new File(resourcePath + "order" + orderId + ".jpeg");
        file.renameTo(newFile);

        System.out.println("Order. Изменение состояния на FALSE в OrderController при ИЗМЕНЕНИИ");
        isReady = false;

        return "redirect:/client{clientId}/order{orderId}";
    }

    @PostMapping("/client{clientId}/order{orderId}/remove")
    public String orderDelete(@PathVariable(value = "orderId") Long orderId) {

        ClientOrder clientOrder = orderRepository.findById(orderId).orElseThrow();
        orderRepository.delete(clientOrder);

        String photoPath = Paths.get("target" + File.separator + "classes" + File.separator
                + "static" + File.separator + "uploads").toAbsolutePath() + File.separator
                + "order" + orderId + ".jpeg";
        File userPhoto = new File(photoPath);
        userPhoto.delete();

        return "redirect:/";
    }

    @PostMapping("/client{clientId}/order{orderId}/deletePhoto")
    public String deletePhoto(@PathVariable(value = "clientId") Long clientId,
                              @PathVariable(value = "orderId") Long orderId,
                              @RequestParam("orderPhotoPath") String orderPhotoPath) {

        File userPhoto = new File(orderPhotoPath);
        userPhoto.delete();

        return "redirect:/client{clientId}/order{orderId}";
    }
}