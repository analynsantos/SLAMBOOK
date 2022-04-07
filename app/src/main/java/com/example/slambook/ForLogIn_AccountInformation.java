package com.example.slambook;

public class ForLogIn_AccountInformation {

    private String User_ID;
    private String accountImage;
    private String username;
    private String password;
    private String name;

    public ForLogIn_AccountInformation(String user_ID, String accountImage, String username, String password, String name) {
        User_ID = user_ID;
        this.accountImage = accountImage;
        this.username = username;
        this.password = password;
        this.name = name;
    }

    public String getAccountImage() {
        return accountImage;
    }

    public void setAccountImage(String accountImage) {
        this.accountImage = accountImage;
    }

    public String getUser_ID() {
        return User_ID;
    }

    public void setUser_ID(String user_ID) {
        User_ID = user_ID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
