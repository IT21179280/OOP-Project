package com.eventplanner.model;

/**
 * An administrator account. Administrators log in through a separate
 * admin login page and can manage users, events and advertisements.
 */
public class Admin {

    private int adminId;
    private String username;
    private String password;

    public Admin() {
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
