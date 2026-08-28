package com.tripplanner.backend.infrastructure.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "bookings")
public class BookingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String packageName;
    private String status = "PENDING";

    public BookingEntity() {}

    public BookingEntity(Long id, String name, String email, String packageName) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.packageName = packageName;
        this.status = "PENDING";
    }

    public BookingEntity(Long id, String name, String email, String packageName, String status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.packageName = packageName;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPackageName() { return packageName; }
    public void setPackageName(String packageName) { this.packageName = packageName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
