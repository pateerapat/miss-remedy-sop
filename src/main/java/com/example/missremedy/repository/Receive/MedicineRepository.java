package com.example.missremedy.repository.Receive;

import com.example.missremedy.pojo.Medicine;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicineRepository extends MongoRepository<Medicine, String> {

    @Query(value = "{id:'?0'}", fields = "{'_id':0}")
    List<Medicine> findAllById(String id);

    @Query(value = "{medId:'?0'}", fields = "{'_id':0}")
    Medicine findMedById(String medId);

    @Query(value = "{medId:'?0'}")
    Medicine findMedWithId(String medId);
}
