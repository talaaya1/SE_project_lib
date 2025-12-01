package com.library;

import java.util.ArrayList;

public class UserManager {
    private final ArrayList<User> users = new ArrayList<>();

    public void addUser(User user){
        users.add(user);
    }

    public void unregisterUser(Admin admin,User targetUser){
        if(admin==null){
            throw new IllegalArgumentException("Only admins can unregister users.");
        }

        for (Book book : targetUser.getBorrowBooks()){
            if(book.isBorrowed()){
                throw new IllegalArgumentException("User has active borrowed books and cannot be unregistered.");
            }
        }
        if(targetUser.getUnpaidFines()>0){
            throw new IllegalArgumentException("User has unpaid fines and cannot be unregistered.");
        }

        users.remove(targetUser);
        System.out.println("User " + targetUser.getEmail() + " has been unregistered successfully.");
    }

    public ArrayList<User> getUsers() {
        return users;
    }

}
