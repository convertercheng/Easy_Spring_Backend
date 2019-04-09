package com.example.basic_backend.backend.Bussniss_logic.Entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @程志豪 时间：20:45
 * 描述：{日志的主体}
 **/
@Data
@Entity
public class data {
    @Id
    @GeneratedValue
    public Integer id;

    @Column
    public String topic;        //题目

    @Column
    public String date;         //日期

    @Column
    public String text;         //正文

    public data(){

    }           //构造器
    public data(String topic, String direction, String date, String text){
        this.date = topic;
        this.text = text;
        this.date = date;
    }
}
