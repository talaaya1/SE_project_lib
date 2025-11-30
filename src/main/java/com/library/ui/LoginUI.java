/*
package com.library.ui;


import javax.swing.*;
import java.awt.*;
import com.library.Library;

public class LoginUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginUI() {
        setTitle("Library System - Login");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 1));

        JLabel userLabel = new JLabel("Username:");
        usernameField = new JTextField();

        JLabel passLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");

        add(userLabel);
        add(usernameField);
        add(passLabel);
        add(passwordField);
        add(loginButton);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            // ✅ تحقق من اسم المستخدم وكلمة السر
            if (username.equals("admin") && password.equals("0000")) {
                JOptionPane.showMessageDialog(this, "Welcome Admin!");
                new LibraryUI(true); // true = إدمن
                dispose();
            } else if (username.equals("aya") && password.equals("1234")) {
                JOptionPane.showMessageDialog(this, "Welcome " + username + "!");
                new LibraryUI(false); // false = مستخدم عادي
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password!");
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginUI::new);
    }
}
*/