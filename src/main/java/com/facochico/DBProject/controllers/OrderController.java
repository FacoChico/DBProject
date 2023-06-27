package com.facochico.DBProject.controllers;

import com.facochico.DBProject.models.ClientOrder;
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

    @GetMapping("/client{clientId}/new-order")
    public String newOrderMapping(@PathVariable(value = "clientId") Long clientId, Model model) {
        model.addAttribute("title", "Создание заказа");
        return "new-order";
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

        Optional<ClientOrder> order = orderRepository.findById(orderId);
        ArrayList<ClientOrder> res = new ArrayList();
        order.ifPresent(res::add);
        model.addAttribute("order", res);

        return "order-card";
    }
}
