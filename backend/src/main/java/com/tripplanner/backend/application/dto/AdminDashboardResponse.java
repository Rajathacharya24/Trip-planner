package com.tripplanner.backend.application.dto;

public class AdminDashboardResponse {
    private long totalUsers;
    private long totalPackages;
    private long totalBookings;
    private long totalRevenue;

    public AdminDashboardResponse(long totalUsers, long totalPackages, long totalBookings, long totalRevenue) {
        this.totalUsers = totalUsers;
        this.totalPackages = totalPackages;
        this.totalBookings = totalBookings;
        this.totalRevenue = totalRevenue;
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public long getTotalPackages() {
        return totalPackages;
    }

    public void setTotalPackages(long totalPackages) {
        this.totalPackages = totalPackages;
    }

    public long getTotalBookings() {
        return totalBookings;
    }

    public void setTotalBookings(long totalBookings) {
        this.totalBookings = totalBookings;
    }

    public long getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(long totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}