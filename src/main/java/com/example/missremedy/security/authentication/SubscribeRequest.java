package com.example.missremedy.security.authentication;

public class SubscribeRequest {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String hnId;
    private String hospital;

    public SubscribeRequest() {}

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getHnId() {
        return hnId;
    }

    public void setHnId(String hnId) {
        this.hnId = hnId;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }
}
