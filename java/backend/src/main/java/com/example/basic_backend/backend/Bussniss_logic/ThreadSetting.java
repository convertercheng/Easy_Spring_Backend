package com.example.basic_backend.backend.Bussniss_logic;

/**
 * @程志豪 时间：15:45
 * 描述：{设置进程的数量和初始化}
 **/
public class ThreadSetting extends Thread{
    public Integer taskid;
    public void ThreadSetting (Integer tid){
        this.taskid = tid;
    }
    public void run(){

    }
}
