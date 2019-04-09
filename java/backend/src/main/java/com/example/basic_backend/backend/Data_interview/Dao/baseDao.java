package com.example.basic_backend.backend.Data_interview.Dao;

import com.example.basic_backend.backend.Bussniss_logic.Entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface baseDao<T> extends JpaRepository<T,Long> {
        @Query("select name from User s where s.name = ?1 and s.password = ?2")
        public User findByNameandPassword(String name,String password);


        @Query("select name from User s where s.name = ?1")
        public User findbyName(String name);


}
