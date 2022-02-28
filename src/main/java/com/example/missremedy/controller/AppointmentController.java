package com.example.missremedy.controller;

import com.example.missremedy.pojo.Appointment;
import com.example.missremedy.repository.appointment.AppointmentService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
public class AppointmentController {
    @Autowired
    AppointmentService appointmentService;
    @Autowired
    RabbitTemplate rabbitTemplate;

    @RequestMapping(value = "/service/app/getAll/{id}", method = RequestMethod.GET)
    public List<Appointment> serviceGetAppointment(@PathVariable("id") String id) {
        Object obj = rabbitTemplate.convertSendAndReceive("AppointmentExchange", "allappointment", id);
        return ((List<Appointment>) obj);
    }

    @RequestMapping(value = "/service/app/getApp/{id}", method = RequestMethod.GET)
    public Appointment serviceAppById(@PathVariable("id") String id) {
        Object obj = rabbitTemplate.convertSendAndReceive("AppointmentExchange", "idappointment", id);
        return ((Appointment) obj);
    }

    @RequestMapping(value = "/service/app/delApp/{id}", method = RequestMethod.GET)
    public Boolean serviceDelApp(@PathVariable("id") String id) {
        Appointment app = (Appointment) rabbitTemplate.convertSendAndReceive("AppointmentExchange", "allidappointment", id);
        Object obj = rabbitTemplate.convertSendAndReceive("AppointmentExchange", "cancelappointment", app);
        return true;
    }

    @RequestMapping(value = "/service/app/addApp/", method = RequestMethod.POST)
    public Appointment serviceAddApp(@RequestBody MultiValueMap<String, String> map) {

        Random rand = new Random();
        ArrayList<String> docList = new ArrayList<>();

        docList.add("Poorin Rpt");
        docList.add("Thitiwut Sukarom");

        Map<String, String> d = map.toSingleValueMap();

        String id = d.get("1");
        Long temp = appointmentService.countAppointment();
        String appId = (temp+1000)+"";
        String year = d.get("2");
        String month = d.get("3");
        String day = d.get("4");
        String time = d.get("5");
        String doctor = docList.get(rand.nextInt(2));
        String disease = d.get("6");

        System.out.println(id + " " + appId + " " + year + " " + month + " " + day + " " + doctor + " " + disease);

        Appointment app = new Appointment(null, id, appId, year, month, day, time, doctor, disease);

        Object obj = rabbitTemplate.convertSendAndReceive("AppointmentExchange", "addappointment", app);
        return app;
    }
}
