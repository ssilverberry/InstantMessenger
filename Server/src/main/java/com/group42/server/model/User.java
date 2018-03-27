package com.group42.server.model;

/**
 * This Class is using for creating new accounts
 *
 * Here we use different constructors because of
 * different action steps between client and server
 */
public class User {
    private String name;
    private String password;
    private String email;
    private Integer id;
    private Integer user_status;

    public User(Integer id, String user_name, String pswrd, String email, Integer user_status) {
        name = user_name;
        password = pswrd;
        this.id = id;
        this.email = email;
        this.user_status = user_status;
    }
    public User (String user_name, String pswrd, String email) {
        name = user_name;
        password = pswrd;
        this.email = email;
    }
    public User (String user_name) {
        name = user_name;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId (Integer id) { this.id = id; };

    public Integer getId () { return id; }

    public Integer getUser_status() {
        return user_status;
    }
}
