package com.example.basic_backend.backend.Bussniss_logic.Entity.entityConfig;

import com.example.basic_backend.backend.Bussniss_logic.Entity.data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



/**
 * @程志豪 时间：15:37
 * 描述：{创建Data的Bean}
 **/


@Configuration
public class DataConfig {
    @Bean(name = "data")
    public data datainit(){
        data Data = new data();
        return Data;
    }

}
