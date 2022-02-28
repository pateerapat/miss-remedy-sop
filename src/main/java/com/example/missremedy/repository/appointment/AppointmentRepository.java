package com.example.missremedy.repository.appointment;

import com.example.missremedy.pojo.Appointment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends MongoRepository<Appointment, String> {

    @Query(value = "{id:'?0'}", fields = "{'_id':0}", sort = "{year: 1, month: 1, day: 1}")
    List<Appointment> findAllById(String id);

    @Query(value = "{appId:'?0'}", fields = "{'_id':0}")
    Appointment findAppById(String appId);

    @Query(value = "{appId:'?0'}")
    Appointment findAppWithId(String appId);
}
