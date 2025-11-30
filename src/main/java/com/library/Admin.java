package com.library;

public class Admin {
    private String username;
    private String password;
    private boolean loggedIn = false;

    public Admin(String username, String password){
        this.username=username;
        this.password=password;
    }

    public boolean login(String u,String p){
        if(username.equals(u) && password.equals(p)){
            loggedIn=true;
            return true;
        }
        return false;
    }

    public void logout(){ loggedIn=false; }
    public boolean isLoggedIn(){ return loggedIn; }
}
