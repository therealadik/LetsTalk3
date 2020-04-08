package com.example.letstalk;

import java.util.Date;

public class person {
    private String name;
    private String mess;
    private long time;
    public person(){}
    public person(String name, String mess){
        this.name=name;
        this.mess=mess;
        time = new Date().getTime();
    }

    public String getName() {
        return name;
    }

    public String getMess() {
        return mess;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setMess(String mess) {
        this.mess = mess;
    }

    public void setName(String name) {
        this.name = name;
    }
}
