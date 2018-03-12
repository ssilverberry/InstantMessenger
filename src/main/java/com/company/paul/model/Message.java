package com.company.paul.model;

public class Message {
    private String msg;

    Message (String message) {
        msg = message;
    }

    Message () {}

    public String getMsg () {
        return msg;
    }

    public void setMsg (String msg) {
        this.msg = msg;
    }
}
