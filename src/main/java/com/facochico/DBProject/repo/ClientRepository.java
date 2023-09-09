package com.facochico.DBProject.repo;

import com.facochico.DBProject.models.AdditionalClientInfo;
import com.facochico.DBProject.models.Client;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ClientRepository extends CrudRepository<Client, Long> {
}
