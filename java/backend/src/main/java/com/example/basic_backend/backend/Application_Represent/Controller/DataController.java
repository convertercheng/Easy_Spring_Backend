package com.example.basic_backend.backend.Application_Represent.Controller;

import com.example.basic_backend.backend.Bussniss_logic.Entity.StandardResp;
import com.example.basic_backend.backend.Bussniss_logic.Entity.data;
import com.example.basic_backend.backend.Bussniss_logic.Service.DataService;
import com.example.basic_backend.backend.Data_interview.Dao.SonDao.DataDao;
import org.hibernate.validator.constraints.pl.REGON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.HashMap;

/**
 * @程志豪 时间：16:53
 * 描述：{desp}
 **/

@RestController
@RequestMapping(value = "/data")
public class DataController {

    @Autowired
    public DataDao dataDao;

    private data data = new data();
    DataService dataService;



    @GetMapping(value = "index")
    public HashMap<String,data> showDataIndex(HttpServletRequest request, HttpServletResponse resp){

    }
}
