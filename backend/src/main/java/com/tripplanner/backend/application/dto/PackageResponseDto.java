package com.tripplanner.backend.application.dto;

public class PackageResponseDto {
    private Long id;
    private String name;
    private String destination;
    private String description;
    private int price;

    public PackageResponseDto() {
    }

    public PackageResponseDto(Long id, String name, String destination, String description, int price) {
        this.id = id;
        this.name = name;
        this.destination = destination;
        this.description = description;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
