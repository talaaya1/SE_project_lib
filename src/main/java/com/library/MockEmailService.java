package com.library;

import java.util.ArrayList;
import java.util.List;

public class MockEmailService implements EmailService {

    private final List<String> sentmessages = new ArrayList<>();
    @Override
    public void sendEmail(String to, String message) {
        String fullMessage = "To: " + to + "| Message: "+message;
        sentmessages.add(fullMessage);
    }

    public List<String> getSentMessages(){
        return sentmessages;
    }
}
