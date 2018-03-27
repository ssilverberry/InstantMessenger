package com.group42.server.controller;


import com.group42.server.model.StringCrypter;
import com.group42.server.model.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class UsersDAOimpl implements DAOusers {

    private static final UsersDAOimpl instance = new UsersDAOimpl();
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private StringCrypter crypter = new StringCrypter(new byte[]{1, 4, 5, 6, 8, 9, 7, 8});

    public static UsersDAOimpl getInstance() {
        return instance;
    }

    private Properties props = new Properties();

    private UsersDAOimpl() {
        super();
    }

    private String username;
    private String password;
    private String url;
    private boolean dbAccess;

    @Override
    public void init() {
        try {
            String path = "db.properties";
            String file = new File(path).getAbsolutePath();
            props.load(new FileInputStream(file));
            username = props.getProperty("user");
            password = props.getProperty("password");
            url = props.getProperty("dburl");
            System.out.println(props.getProperty("user") + props.getProperty("password") + props.getProperty("dburl"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean connect() {
        try {
            init();
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection(url, username, password);
            if (!connection.isClosed()) {
                System.out.println("Connection successful!");
                return true;
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void disconnect() {
        try {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getUsers() {
        List<User> list = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM USERS");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list.add(parseUser(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //disconnect();
        return list;
    }

    public void insertInto(String login, String pswrd, String email) {
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO users (user_login, user_password, USER_EMAIL, USER_STATUS) " + "VALUES (?, ?, ?, ?)");
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, crypter.encrypt(pswrd));
            preparedStatement.setString(3, email);
            preparedStatement.setInt(4, 0);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //disconnect();
    }

    private User parseUser(ResultSet resultSet) {
        User user = null;
        try {
            int id = resultSet.getInt("user_id");
            String name = resultSet.getString("user_login");
            String pswrd = resultSet.getString("user_password");
            String email = resultSet.getString("user_email");
            Integer usr_status = resultSet.getInt("user_status");
            String decryptedPswrd = crypter.decrypt(pswrd);
            System.out.println("------> " + pswrd);
            user = new User(id, name, decryptedPswrd, email, usr_status);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public void updateUsrStatus(Integer user_id, Integer status) {
        try {
            preparedStatement = connection.prepareStatement("UPDATE USERS SET USER_STATUS =" + status + " WHERE USER_ID = " + user_id);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
