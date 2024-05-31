package com.mobiauto.repository;

import com.mobiauto.data.VehicleHistory;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface VehicleHistoryRepository extends JpaRepository<VehicleHistory, Long> {

    @Nullable List<VehicleHistory> findAll();

}
