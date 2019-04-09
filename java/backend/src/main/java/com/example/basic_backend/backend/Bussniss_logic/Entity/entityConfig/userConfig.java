package com.example.basic_backend.backend.Bussniss_logic.Entity.entityConfig;


import com.example.basic_backend.backend.Bussniss_logic.Entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class userConfig {
    @Bean(name = "user")                //创建user的bean
    public User inituser(){
        User user = new User();
        return user;
    }
}
