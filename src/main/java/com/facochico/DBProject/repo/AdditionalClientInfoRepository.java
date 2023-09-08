package com.facochico.DBProject.repo;

import com.facochico.DBProject.models.AdditionalClientInfo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AdditionalClientInfoRepository extends CrudRepository<AdditionalClientInfo, Long> {
    List<AdditionalClientInfo> findByType(String type);
}