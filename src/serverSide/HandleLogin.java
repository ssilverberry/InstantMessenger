package serverSide;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by o.spichak on 14.03.2018.
 */
public class HandleLogin {

    private static String status;
    private static String userId;
    private static HashSet<User> userList = new HashSet<>();
    private static final String pathToFile = "./regUser.txt";
    private static DataOutput writer;
    public static String getStatus() {
        return status;
    }

    public static String getUserId() {
        return userId;
    }

    public static void setStatus(String line) {
        status = line;
    }

    public static String getResponse() {
        return status;
    }

    public static void start(InputChatMessage object) {
        Integer q [] = {1, 2};
        User user1 = new User("11", "11", "asdf@asdf.com");
        User user2 = new User("22", "22", "qwer@qwer.com");
        userList.add(user1);
        setStatus("access " + q[0]);
        userList.add(user2);
        setStatus("access " + q[1]);
        if (object.getLogin() != null) {
            //readFromFile();
            Iterator<User> itr = userList.iterator();
            if (userList.size() == 0) {
                setStatus("noRegUser");
                registration(object);
            } else {
                /*while (itr.hasNext()) {
                    User user = itr.next();
                    if (!object.getLogin().equals(user.getName())) {
                        setStatus("noRegUser");
                        registration(object);
                    }
                    if (object.getLogin().equals(user.getName()) & object.getPassword().equals(user.getPassword())) {
                        setStatus("access 1234");
                    } else {
                        setStatus("noRegUser");
                    }
                }*/
            }
        }
        else if(object.getChatMessage() !=null){
            System.out.println(getResponse());
            sendMsgAll();
            System.out.println(getResponse());
        }
    }

    public static void registration(InputChatMessage object) {
        User user = new User(object.getLogin(), object.getPassword(), object.getMail());
        System.out.println(user.getName() + " " + user.getPassword() + " " + user.getEmail());
        userList.add(user);
        // write to file user login and password, our current data base
        try {
            writer = new DataOutputStream(new FileOutputStream(pathToFile));
            writer.writeUTF(user.getName() + " " + user.getPassword() + " " + user.getEmail() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        setStatus("regUser");
    }

    public static void sendMsgAll () {
        //if (input.getChatMessage() != null & input.getIdUsers() != 0) {
           // System.out.println(input.getChatMessage() + input.getIdUsers());
            setStatus("messageAll");
       // }
    }

    private static void readFromFile () {
        User user;
            try {
                FileInputStream fileReader = new FileInputStream(pathToFile);
                DataInputStream reader = new DataInputStream(fileReader);
                String info = reader.readUTF();
                String[] tokens ;
                if (info.equals(null))
                    return;
                else
                    tokens = StringUtils.split(info);
                if (tokens.length != 0) {
                    user = new User (tokens[0], tokens[1], tokens[2]);
                    if (userList.size() == 0) {
                        userList.add(user);
                    } else {
                        Iterator<User> itr = userList.iterator();
                        while (itr.hasNext()) {
                            User user1 = itr.next();
                            if (!user1.getName().equals(user.getName()) & !user1.getPassword().equals(user.getPassword()))
                                userList.add(user);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public static boolean isMemberOfChat(Object object) {
        return userList.contains(object);
    }
    public static HashSet<User> getUserList() {
        return userList;
    }
}
