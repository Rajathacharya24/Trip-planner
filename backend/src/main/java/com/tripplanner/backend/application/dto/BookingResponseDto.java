package com.tripplanner.backend.application.dto;

public class BookingResponseDto {
    private Long id;
    private String name;
    private String email;
    private String packageName;
    private String status;

    public BookingResponseDto() {}

    public BookingResponseDto(Long id, String name, String email, String packageName) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.packageName = packageName;
        this.status = "PENDING";
    }

    public BookingResponseDto(Long id, String name, String email, String packageName, String status) {
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
