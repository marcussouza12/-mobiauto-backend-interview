package com.mobiauto.data;


import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Serdeable
public class TestDrive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    private Date testDriveDate;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Date getTestDriveDate() {
        return testDriveDate;
    }

    public void setTestDriveDate(Date testDriveDate) {
        this.testDriveDate = testDriveDate;
    }

}
