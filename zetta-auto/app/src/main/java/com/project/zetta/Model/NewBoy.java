package com.project.zetta.Model;

public class NewBoy {
    private String name, phone, password, status;

    public NewBoy() {
    }

    public NewBoy(String name, String phone, String password, String status) {
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
