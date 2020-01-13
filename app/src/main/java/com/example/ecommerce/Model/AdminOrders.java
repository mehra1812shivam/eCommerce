package com.example.ecommerce.Model;

public class AdminOrders {
    private String name,date,City,Address,phone,state,time,totalAmount;

    public AdminOrders() {
    }

    public AdminOrders(String name, String date, String city, String address, String phone, String state, String time, String totalAmount) {
        this.name = name;
        this.date = date;
        City = city;
        Address = address;
        this.phone = phone;
        this.state = state;
        this.time = time;
        this.totalAmount = totalAmount;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}

