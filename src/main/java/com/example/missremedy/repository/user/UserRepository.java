package com.example.missremedy.repository.user;

import com.example.missremedy.pojo.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    @Query(value = "{username:'?0'}", fields = "{'_id':0}")
    User findByUsername(String username);

    @Query(value = "{username:'?0'}")
    User findByUsernameWithId(String username);
}
