package com.mobiauto.service;

import com.mobiauto.data.VehicleHistory;
import com.mobiauto.repository.VehicleHistoryRepository;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
@Serdeable
public class VehicleHistoryService {

    private final VehicleHistoryRepository vehicleHistoryRepository;

    public VehicleHistoryService(VehicleHistoryRepository vehicleHistoryRepository) {
        this.vehicleHistoryRepository = vehicleHistoryRepository;
    }

    public VehicleHistory saveVehicleHistory(VehicleHistory vehicleHistory) {
        return vehicleHistoryRepository.save(vehicleHistory);
    }

    public VehicleHistory updateVehicleHistory(VehicleHistory vehicleHistory) {
        return vehicleHistoryRepository.update(vehicleHistory);
    }

    public List<VehicleHistory> getAllVehicleHistorys() {
        return vehicleHistoryRepository.findAll();
    }

    public @Nullable VehicleHistory getVehicleHistoryById(Long id) {
        return vehicleHistoryRepository.findById(id).orElse(null);
    }

    public void deleteVehicleHistory(Long id) {
        vehicleHistoryRepository.deleteById(id);
    }
    
}
