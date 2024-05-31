package com.mobiauto.repository;

import com.mobiauto.data.Vehicle;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
}
