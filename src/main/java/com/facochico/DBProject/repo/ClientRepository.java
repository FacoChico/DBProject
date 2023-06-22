package com.facochico.DBProject.repo;

import com.facochico.DBProject.models.Client;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<Client, Long> {
}
