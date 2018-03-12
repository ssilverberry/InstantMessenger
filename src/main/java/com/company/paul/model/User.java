package com.company.paul.model;

public class User {
    private String name;
    private String password;

    User (String user_name, String pswrd) {
        name = user_name;
        password = pswrd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
