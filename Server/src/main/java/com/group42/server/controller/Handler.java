package com.group42.server.controller;


import com.group42.server.model.InputMessage;
import com.group42.server.model.StringCrypter;
import com.group42.server.model.User;

import java.io.DataOutput;
import java.util.HashSet;
import java.util.List;

public class Handler {

    private static final String pathToFile = "./regUser.txt";
    private static String status;
    private static String userId;
    private static HashSet<User> userList = new HashSet<>();
    private static DataOutput writer;
    private static int counter = 0;
    private static List<User> usrList;
    private static StringCrypter crypter = new StringCrypter(new byte[]{1,4,5,6,8,9,7,8});

    public static String getStatus() {
        return status;
    }

    public static void setStatus(String line) {
        status = line;
    }

    public static String getUserId() {
        return userId;
    }

    public static String getResponse() {
        return status;
    }


    public static void start(InputMessage object) {
        if (object != null) {
            switch (object.getActionId()) {
                case 1:
                    System.out.println("case 1");
                    if(!authorized(object))
                        registered(object);
                    break;
                case 2:
                    System.out.println("case 2");
                    registered(object);
                    break;
                case 32:
                    System.out.println("case 32");
                    setStatus("messageAll");
                    break;
                case 33:
                    System.out.println("case 33");
                    setStatus("messagePrivate");
                    break;
                case 34:
                    System.out.println("case 34");
                    setStatus("createPrivateChatRoom");
                    break;
                case 35:
                    System.out.println("case 35");
                    setStatus("logOut");
                    break;
                case 31:
                    System.out.println("case 31");
                    setStatus("activeList");
                    break;
            }
        }
    }

    private static void registration(InputMessage object) {
       // System.out.println(object.getLogin() + " +  " + object.+ " + " + object.getMail());
        UsersDAOimpl.getInstance().insertInto(object.getLogin(), object.getPassword(), object.getEmail());
        setStatus("regUser " + object.getLogin());
    }

    private static void sendMsgAll(InputMessage obj) {
        if (obj.getActionId() == 32)
            setStatus("messageAll");
        else
            setStatus("messagePrivate");
    }

    private static boolean authorized(InputMessage usr) {
        if (usr.getActionId() == 1) {
            usrList = UsersDAOimpl.getInstance().getUsers();
            if (usrList.size() != 0) {
                for (User user : usrList) {
                        //String pswr = crypter.decrypt(user.getPassword());
                        System.out.println("password from database: " + user.getPassword() + " " + "password from client " + usr.getPassword());
                        if (user.getName().equals(usr.getLogin()) & user.getPassword().equals(usr.getPassword())) {
                            setStatus("access " + user.getName());
                            return true;
                        } else {
                            setStatus("noRegUser");
                        }
                    }
            } else {
                setStatus("noRegUser");
            }
        }
        return false;
    }

    private static boolean registered(InputMessage usr) {
        if (usr.getActionId() == 2) {
            registration(usr);
            return true;
        }
        return false;
    }
}
