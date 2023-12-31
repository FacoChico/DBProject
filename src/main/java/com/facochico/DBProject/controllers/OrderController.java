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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Optional;

@Controller
public class OrderController {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/client{clientId}/new-order")
    public String newOrderMapping(@PathVariable(value = "clientId") Long clientId, Model model) {
        model.addAttribute("title", "Создание заказа");
        model.addAttribute("clientId", clientId);
        return "order-add";
    }

    @PostMapping("/client{clientId}/new-order")
    public String newOrder(@PathVariable(value = "clientId") long clientId,
                           @RequestParam("category") String category, @RequestParam("brand") String brand,
                           @RequestParam("size") String size, @RequestParam("color") String color,
                           @RequestParam("price") String price, @RequestParam("orderDate") String orderDate,
                           @RequestParam("description") String description,
                           @RequestParam(value = "file", required = false) MultipartFile file) {

        byte[] orderPhoto = null;

        if (file != null) {
            try {
                orderPhoto = new byte[file.getBytes().length];
                int i = 0;
                for (byte b : file.getBytes()) {
                    orderPhoto[i++] = b;
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        ClientOrder clientOrder = new ClientOrder(clientId, category, brand, size, color, price, orderDate, description, orderPhoto);
        orderRepository.save(clientOrder);


        return ("redirect:/client{clientId}");
    }

    @GetMapping("/client{clientId}/order{orderId}")
    public String viewOrder(@PathVariable(value = "clientId") Long clientId,
                        @PathVariable(value = "orderId") Long orderId, Model model) {

        if(!orderRepository.existsById(orderId)) {
            return "redirect:/";
        }

        String title = "Карточка заказа";
        model.addAttribute("title", title);

        if (orderRepository.findById(orderId).get().getOrderPhoto() != null) {
            byte[] encodeBase64 = Base64.getEncoder().encode(orderRepository.findById(orderId).get().getOrderPhoto());
            String orderPhoto = new String(encodeBase64, StandardCharsets.UTF_8);
            model.addAttribute("orderPhoto", orderPhoto);
        }

        Optional<Client> client = clientRepository.findById(clientId);
        ArrayList<Client> res = new ArrayList<>();
        client.ifPresent(res::add);
        model.addAttribute("client", res);

        Optional<ClientOrder> order = orderRepository.findById(orderId);
        ArrayList<ClientOrder> res2 = new ArrayList<>();
        order.ifPresent(res2::add);
        if (!res2.get(0).getOrderDate().isEmpty())
            res2.get(0).setOrderDate(parseDate(res2.get(0).getOrderDate()));
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

        if (orderRepository.findById(orderId).get().getOrderPhoto() != null) {
            byte[] encodeBase64 = Base64.getEncoder().encode(orderRepository.findById(orderId).get().getOrderPhoto());
            String orderPhoto = new String(encodeBase64, StandardCharsets.UTF_8);
            model.addAttribute("orderPhoto", orderPhoto);
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
    public String orderUpdate(@PathVariable(value = "clientId") long clientId, @PathVariable(value = "orderId") Long orderId,
                              @RequestParam("category") String category, @RequestParam("brand") String brand,
                              @RequestParam("size") String size, @RequestParam("color") String color,
                              @RequestParam("price") String price, @RequestParam("orderDate") String orderDate,
                              @RequestParam("description") String description,
                              @RequestParam(value = "file", required = false) MultipartFile file) {

        ClientOrder clientOrder = orderRepository.findById(orderId).orElseThrow();

        byte[] orderPhoto = null;

        if (file != null) {
            try {
                orderPhoto = new byte[file.getBytes().length];
                int i = 0;
                for (byte b : file.getBytes()) {
                    orderPhoto[i++] = b;
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        clientOrder.setCategory(category);
        clientOrder.setBrand(brand);
        clientOrder.setSize(size);
        clientOrder.setColor(color);
        clientOrder.setPrice(price);
        clientOrder.setOrderDate(orderDate);
        clientOrder.setDescription(description);

        if (orderPhoto != null)
            clientOrder.setOrderPhoto(orderPhoto);

        orderRepository.save(clientOrder);

        return "redirect:/client{clientId}/order{orderId}";
    }

    @PostMapping("/client{clientId}/order{orderId}/remove")
    public String orderDelete(@PathVariable(value = "clientId") long clientId,
                              @PathVariable(value = "orderId") Long orderId) {

        ClientOrder clientOrder = orderRepository.findById(orderId).orElseThrow();
        orderRepository.delete(clientOrder);

        return "redirect:/";
    }

    @PostMapping("/client{clientId}/order{orderId}/deletePhoto")
    public String deletePhoto(@PathVariable(value = "orderId") Long orderId) {

        ClientOrder clientOrder = orderRepository.findById(orderId).orElseThrow();
        clientOrder.setOrderPhoto(null);
        orderRepository.save(clientOrder);

        return "redirect:/client{clientId}/order{orderId}";
    }

    private String parseDate(String str) {

        String year = str.substring(0, 4);
        String month = str.substring(5, 7);
        String day = str.substring(8);

        return day + "." + month + "." + year;
    }
}