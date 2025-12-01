package com.library;

import java.util.ArrayList;

public class ReminderService {

    private final Overdue overdue;
    private final EmailService emailService;

    public ReminderService(Overdue overdue, EmailService emailService) {
        this.overdue = overdue;
        this.emailService = emailService;
    }

    public void sendReminders(ArrayList<User> users){
        for(User user :users){
            int overduecount=0;

            for(Book book : user.getBorrowBooks()){
                if(overdue.isOverdue(book)){
                    overduecount++;
                }
            }

            if(overduecount > 0){
                String message = "You have " + overduecount + " overdue book(s).";
                emailService.sendEmail(user.getEmail(), message);
            }

        }
    }
}
