package com.example.basic_backend.backend.Bussniss_logic.Entity;

import lombok.Data;
import sun.java2d.pipe.SpanClipRenderer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @程志豪 时间：20:49
 * 描述：{论文的主体}
 **/
@Data
@Entity
public class paper {

    @Id
    @GeneratedValue
    public Integer id;

    @Column
    public String topic;        //题目

    @Column
    public String direction;        //方向

    @Column
    public String date;         //日期

    @Column
    public String text;         //正文

    public paper(){

    }           //构造器

    public paper(String topic, String direction, String date, String text){
        this.date = topic;
        this.direction = direction;
        this.text = text;
        this.date = date;
    }
}