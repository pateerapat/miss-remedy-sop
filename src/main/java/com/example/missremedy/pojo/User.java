package com.example.missremedy.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Document("User")
public class User implements Serializable {
    @Id
    private String _id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String hnId;
    private String hospital;

    public User() {}

    public User(String _id, String username, String password, String firstName, String lastName, String hnId, String hospital) {
        this._id = _id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.hnId = hnId;
        this.hospital = hospital;
    }
}
