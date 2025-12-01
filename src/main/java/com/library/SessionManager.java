package com.library;

import java.util.ArrayList;

public class SessionManager {

    private final ArrayList<Admin> admins = new ArrayList<>();
    private Admin loginAdmin;

    public void regAdmin(Admin admin){
        admins.add(admin);
    }

    public boolean login(String username,String password) {
        for (Admin admin : admins) {
            if (admin.getUsername().equals(username) && admin.getPassword().equals(password)) {
                loginAdmin = admin;
                System.out.println("Login successful: " + username);
                return true;
            }
        }
        System.out.println("Invalid credentials for: " + username);
        return false;
    }

    public void logout(){
        if(loginAdmin != null){
            System.out.println(loginAdmin.getUsername() + " has logged out");
            loginAdmin = null;
        } else {
            System.out.println("No admin is currently logged in");
        }
    }

    public boolean isLoggedIn() {
        return loginAdmin != null;
    }

    public Admin getLoggedInAdmin() {
        return loginAdmin;
    }

    // أي إجراء يحتاج admin
    public void performAdminAction(String action) {
        if (!isLoggedIn()) {
            throw new IllegalStateException("Action denied. Admin must be logged in.");
        }
        System.out.println("Admin " + loginAdmin.getUsername() + " performed: " + action);
    }

    // مثال لإجراء إضافي
    public void addBook(String bookName) {
        if (!isLoggedIn()) {
            throw new IllegalStateException("Cannot add book. Admin must be logged in.");
        }
        System.out.println("Book added: " + bookName);
    }
}
