package com.example.missremedy.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Document("Appointment")
public class Appointment implements Serializable {
    @Id
    private String _id;
    private String id;
    private String appId;
    private String year;
    private String month;
    private String day;
    private String time;
    private String doctor;
    private String disease;

    public Appointment() {}

    public Appointment(String _id, String id, String appId, String year, String month, String day, String time, String doctor, String disease) {
        this._id = _id;
        this.id = id;
        this.appId = appId;
        this.year = year;
        this.month = month;
        this.day = day;
        this.time = time;
        this.doctor = doctor;
        this.disease = disease;
    }
}
