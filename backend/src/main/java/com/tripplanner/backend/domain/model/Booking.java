package com.tripplanner.backend.domain.model;

public class Booking {
    private Long id;
    private String name;
    private String email;
    private String packageName;

    public Booking() {}

    public Booking(Long id, String name, String email, String packageName) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.packageName = packageName;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPackageName() { return packageName; }
    public void setPackageName(String packageName) { this.packageName = packageName; }
}
