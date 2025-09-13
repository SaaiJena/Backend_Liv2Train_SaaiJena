package com.liv2train.liv2train.repository;

import com.liv2train.liv2train.model.TrainingCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TrainingCenterRepository
        extends JpaRepository<TrainingCenter, Long>, JpaSpecificationExecutor<TrainingCenter> {

    boolean existsByCenterCode(String centerCode);
}
