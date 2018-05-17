package com.github.hasanmirzae.demo.models;

public class Profile {
    private String address;
    private String phone;
    private String email;

    public Profile() {
    }

    public Profile(String address, String phone, String email) {
        this.address = address;
        this.phone = phone;
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override public String toString() {
        return "Profile{" + "address='" + address + '\'' + ", phone='" + phone + '\'' + ", email='"
                + email + '\'' + '}';
    }
}
