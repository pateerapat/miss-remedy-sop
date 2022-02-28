package com.example.missremedy.repository.Receive;

import com.example.missremedy.pojo.Medicine;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicineService {
    @Autowired
    MedicineRepository receiveRepository;

    @RabbitListener(queues = "GetAllMedicineQueue")
    public List<Medicine> getAllById(String id) {
        return receiveRepository.findAllById(id);
    }

    @RabbitListener(queues = "GetMedicineQueue")
    public Medicine getMedById(String id) {
        return receiveRepository.findMedById(id);
    }

    @RabbitListener(queues = "AllWithIdMedicineQueue")
    public Medicine getMedWithId(String id) {
        return receiveRepository.findMedWithId(id);
    }

    @RabbitListener(queues = "ConfirmMedicineQueue")
    public Boolean confirmedMedicine(Medicine m) {
        receiveRepository.delete(m);
        return true;
    }
}
