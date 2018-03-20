package serverSide;

// пользователь для регистрации
public class User {
    private String name;
    private String password;
    private String email;

    User (String user_name, String pswrd, String email) {
        name = user_name;
        password = pswrd;
        this.email = email;
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
}
