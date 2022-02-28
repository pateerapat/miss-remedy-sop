package com.example.missremedy.controller;

import com.example.missremedy.pojo.Medicine;
import com.example.missremedy.repository.Receive.MedicineService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MedicineController {
    @Autowired
    MedicineService medicineService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @RequestMapping(value = "service/rec/getAll/{id}", method = RequestMethod.GET)
    public List<Medicine> getAllById(@PathVariable("id") String id) {
        Object obj = rabbitTemplate.convertSendAndReceive("MedicineExchange", "getallmed", id);
        return ((List<Medicine>) obj);
    }

    @RequestMapping(value = "service/rec/getMed/{id}", method = RequestMethod.GET)
    public Medicine getMedById(@PathVariable("id") String id) {
        Object obj = rabbitTemplate.convertSendAndReceive("MedicineExchange", "getmed", id);
        return ((Medicine) obj);
    }


    @RequestMapping(value = "/service/rec/delMed/{id}", method = RequestMethod.GET)
    public Boolean serviceDelMed(@PathVariable("id") String id) {
        Medicine med = (Medicine) rabbitTemplate.convertSendAndReceive("MedicineExchange", "getmedwithid", id);
        System.out.println(med);
        Object obj = rabbitTemplate.convertSendAndReceive("MedicineExchange", "conmed", med);
        return true;
    }
}
