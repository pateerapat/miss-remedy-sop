package com.example.missremedy.repository.appointment;

import com.example.missremedy.pojo.Appointment;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {
    @Autowired
    AppointmentRepository appointmentRepository;

    @RabbitListener(queues = "AllAppointmentQueue")
    public List<Appointment> getAllAppointmentById(String id) {
        return appointmentRepository.findAllById(id);
    }

    @RabbitListener(queues = "IdAppointmentQueue")
    public Appointment getAppById(String id) {
        return appointmentRepository.findAppById(id);
    }

    @RabbitListener(queues = "AllWithIdAppointmentQueue")
    public Appointment getAppWithId(String id) {
        return appointmentRepository.findAppWithId(id);
    }

    @RabbitListener(queues = "CancelAppointmentQueue")
    public Boolean cancelAppointment(Appointment a) {
        appointmentRepository.delete(a);
        return true;
    }

    public long countAppointment() {
        return appointmentRepository.count();
    }

    @RabbitListener(queues = "AddAppointmentQueue")
    public Boolean addAppointment(Appointment a) {
        appointmentRepository.save(a);
        return true;
    }
}
