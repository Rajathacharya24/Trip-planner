package com.tripplanner.backend.model;

public class BookingRequest {
    private String name;
    private String email;
    private String packageName;

    public BookingRequest() {}

    public BookingRequest(String name, String email, String packageName) {
        this.name = name;
        this.email = email;
        this.packageName = packageName;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPackageName() { return packageName; }

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPackageName(String packageName) { this.packageName = packageName; }
}
