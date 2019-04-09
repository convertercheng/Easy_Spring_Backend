package com.example.basic_backend.backend.Bussniss_logic.Entity;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
@Entity
@Data

///定义User主题，主要就是本人和游客
public class User {
    @Id
    @GeneratedValue
    private Integer ID;

    @Column
    private String name;

    @Column
    private String password;

    @Column
    private String permit;

    public User(Integer ID,String name, String password, String permit){

    }
    public User(){

    }

}
