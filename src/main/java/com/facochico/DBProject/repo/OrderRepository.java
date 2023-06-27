package com.facochico.DBProject.repo;

import com.facochico.DBProject.models.ClientOrder;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<ClientOrder, Long> {
    List<ClientOrder> findByClientId(Long client_id);
}