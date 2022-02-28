package com.example.missremedy.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Document("Medicine")
public class Medicine implements Serializable {
    @Id
    private String _id;
    private String medId;
    private String medicine;
    private String pharmacy;
    private String location;
    private String date;
    private String year;
    private String month;
    private String day;
    private String price;

    public Medicine() {}

    public Medicine(String _id, String medId, String medicine, String pharmacy, String location, String date, String year, String month, String day, String price) {
        this._id = _id;
        this.medId = medId;
        this.medicine = medicine;
        this.pharmacy = pharmacy;
        this.location = location;
        this.date = date;
        this.year = year;
        this.month = month;
        this.day = day;
        this.price = price;
    }
}
