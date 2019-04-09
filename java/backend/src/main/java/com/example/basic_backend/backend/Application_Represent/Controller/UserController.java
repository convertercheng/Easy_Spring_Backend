package com.example.basic_backend.backend.Application_Represent.Controller;


import com.example.basic_backend.backend.Bussniss_logic.Entity.User;
import com.example.basic_backend.backend.Data_interview.Dao.baseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

//User的controller，用于实现业务应用的响应

@RestController
@RequestMapping("/user")
public class UserController {

    @Value("${MAIN_WEB}")
    String main_web;

    @Value("${LOGIN_WEB}")
    String login_web;


    @Autowired
    private baseDao baseDao;
    private User user= new User();
    @RequestMapping("index")
    public HashMap<String, String> userIndexHashmap(HttpSession httpSession){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("message","请求用户列表");
        httpSession.getAttribute("userLogin");
        return hashMap;
    }

    @RequestMapping(value = "visitor")
    public String visitor(){
        if(user!=null){
            user=baseDao.findbyName("Visitor");
        }

        return "游客,欢迎访问"; }

    @RequestMapping(value = "login")
    public String login(){
        User loginuser = new User();
        String str = "";
        String username = loginuser.getName();
        if(username!=null){
            str = "index";
        }
        else{
            str="login";
        }
        return str;
    }

    @RequestMapping(value = "uvisitor")
    public String visitor(HttpServletRequest request){
        User username = baseDao.findbyName("visitor");
        return "visitor_mode";
    }

    @RequestMapping(value = "ulogin")
    public String login(HttpServletRequest request,HttpSession httpSession){
        User uloginuser = new User();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println(username+" " +password);
        uloginuser = baseDao.findByNameandPassword(username,password);
        String str = "";
        if(uloginuser!=null){
            httpSession.setAttribute("userLogin",uloginuser);
            System.out.println("login success");
            System.out.println(main_web);
            return "login success";
        }
        else{
            System.out.println("login failed");
            return login_web;
        }
    }


}
