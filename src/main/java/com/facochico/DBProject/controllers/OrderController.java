package com.facochico.DBProject.controllers;

import com.facochico.DBProject.models.AdditionalClientInfo;
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

import java.util.ArrayList;
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
        return "order-add";
    }

    @PostMapping("/client{clientId}/new-order")
    public String newOrder(@PathVariable(value = "clientId") long clientId,
                           @RequestParam String category, @RequestParam String brand,
                           @RequestParam String size, @RequestParam String orderDate,
                           @RequestParam String description) {

        ClientOrder clientOrder = new ClientOrder(clientId, category, brand, size, orderDate, description);
        orderRepository.save(clientOrder);

        return "redirect:/client{clientId}";
    }

    @GetMapping("/client{clientId}/order{orderId}")
    public String viewOrder(@PathVariable(value = "clientId") Long clientId,
                        @PathVariable(value = "orderId") Long orderId, Model model) {

        if(!orderRepository.existsById(orderId)) {
            return "redirect:/";
        }

        Optional<Client> client = clientRepository.findById(clientId);
        ArrayList<Client> res = new ArrayList<>();
        client.ifPresent(res::add);
        model.addAttribute("client", res);

        Optional<ClientOrder> order = orderRepository.findById(orderId);
        ArrayList<ClientOrder> res2 = new ArrayList();
        order.ifPresent(res2::add);
        model.addAttribute("order", res2);

        return "order-card";
    }

    @GetMapping("/client{clientId}/order{orderId}/edit")
    public String orderEdit(@PathVariable(value = "orderId") Long orderId, Model model) {

        if(!orderRepository.existsById(orderId)) {
            return "redirect:/";
        }

        Optional<ClientOrder> order = orderRepository.findById(orderId);
        ArrayList<ClientOrder> res = new ArrayList();
        order.ifPresent(res::add);
        model.addAttribute("order", res);

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

        return "redirect:/client{clientId}/order{orderId}";
    }

    @PostMapping("/client{clientId}/order{orderId}/remove")
    public String orderDelete(@PathVariable(value = "orderId") Long orderId) {

        ClientOrder clientOrder = orderRepository.findById(orderId).orElseThrow();
        orderRepository.delete(clientOrder);

        return "redirect:/";
    }
}
