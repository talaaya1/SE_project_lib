/*
package com.library;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        Library library = new Library();
        Admin admin = new Admin("admin", "1234");  // حساب الادمن جاهز
        User currentUser = null;
        boolean isAdmin = false;

        System.out.println("=== Library System ===");

        // اختيار الدور
        while (true) {
            System.out.print("Login as Admin or User? (A/U): ");
            String role = in.nextLine().trim().toUpperCase();
            if (role.equals("A")) {
                // تسجيل دخول Admin
                System.out.print("Enter admin username: ");
                String u = in.nextLine();
                System.out.print("Enter admin password: ");
                String p = in.nextLine();
                if (admin.login(u, p)) {
                    System.out.println("Admin logged in successfully.");
                    isAdmin = true;
                    break;
                } else {
                    System.out.println("Invalid admin credentials. Try again.");
                }
            } else if (role.equals("U")) {
                // تسجيل دخول User
                System.out.print("Enter username: ");
                String uname = in.nextLine();
                System.out.print("Enter password: ");
                String pass = in.nextLine();
                if (uname.equals("user") && pass.equals("1011")) {
                    currentUser = new User(uname);
                    System.out.println("User logged in successfully.");
                    break;
                } else {
                    System.out.println("Invalid user credentials. Try again.");
                }
            } else {
                System.out.println("Invalid choice. Enter A for Admin or U for User.");
            }
        }

        int choice;
        do {
            System.out.println("\n===== MENU =====");

            if (isAdmin) {
                System.out.println("1. Add Book");
                System.out.println("2. Add CD");
                System.out.println("3. List All Media");
                System.out.println("4. Search Media");
                System.out.println("5. View User Fines");
                System.out.println("0. Exit");
            } else {
                System.out.println("1. List All Media");
                System.out.println("2. Search Media");
                System.out.println("3. Borrow Media");
                System.out.println("4. Return Media");
                System.out.println("5. View My Fines");
                System.out.println("6. Pay Fine");
                System.out.println("0. Exit");
            }

            System.out.print("Choice: ");
            choice = Integer.parseInt(in.nextLine());

            if (isAdmin) {
                switch (choice) {
                    case 1: // Add Book
                        System.out.print("Book Title: ");
                        String title = in.nextLine();
                        System.out.print("Author: ");
                        String author = in.nextLine();
                        System.out.print("ISBN: ");
                        String isbn = in.nextLine();
                        library.addBook(admin, new Book(title, author, isbn));
                        System.out.println("Book added.");
                        break;

                    case 2: // Add CD
                        System.out.print("CD Title: ");
                        String ct = in.nextLine();
                        System.out.print("Artist: ");
                        String artist = in.nextLine();
                        library.addCD(admin, new CD(ct, artist));
                        System.out.println("CD added.");
                        break;

                    case 3: // List All Media
                        List<Media> mediaList = library.getMediaList();
                        if (mediaList.isEmpty()) {
                            System.out.println("No media available.");
                        } else {
                            System.out.println("\n--- All Media ---");
                            for (Media m : mediaList) System.out.println(m);
                        }
                        break;

                    case 4: // Search Media
                        System.out.print("Enter keyword: ");
                        String kw = in.nextLine();
                        List<Media> results = library.searchMedia(kw);
                        if (results.isEmpty()) {
                            System.out.println("No media found.");
                        } else {
                            System.out.println("\n--- Search Results ---");
                            for (Media m : results) System.out.println(m);
                        }
                        break;

                    case 5: // View User Fines
                        System.out.print("Enter username to check fines: ");
                        String uname = in.nextLine();
                        User user = library.getUserByName(uname); // تحتاج دالة getUserByName
                        if (user != null) {
                            System.out.println("User " + uname + " fine balance: " + user.getTotalFines());
                        } else {
                            System.out.println("User not found.");
                        }
                        break;

                    case 0:
                        System.out.println("Goodbye!");
                        break;

                    default:
                        System.out.println("Invalid choice.");
                }
            } else {
                switch (choice) {
                    case 1: // List All Media
                        List<Media> mediaList = library.getMediaList();
                        if (mediaList.isEmpty()) {
                            System.out.println("No media available.");
                        } else {
                            System.out.println("\n--- All Media ---");
                            for (Media m : mediaList) System.out.println(m);
                        }
                        break;

                    case 2: // Search Media
                        System.out.print("Enter keyword: ");
                        String kw = in.nextLine();
                        List<Media> results = library.searchMedia(kw);
                        if (results.isEmpty()) {
                            System.out.println("No media found.");
                        } else {
                            System.out.println("\n--- Search Results ---");
                            for (Media m : results) System.out.println(m);
                        }
                        break;

                    case 3: // Borrow Media
                        System.out.print("Enter title to borrow: ");
                        String bt = in.nextLine();
                        Media selectedBorrow = null;
                        for (Media m : library.getMediaList()) {
                            if (m.getTitle().equalsIgnoreCase(bt)) {
                                selectedBorrow = m;
                                break;
                            }
                        }
                        if (selectedBorrow == null) {
                            System.out.println("Media not found.");
                        } else {
                            if (library.borrowMedia(currentUser, selectedBorrow)) {
                                System.out.println("Borrowed successfully.");
                            } else {
                                System.out.println("Cannot borrow (maybe overdue or fines exist).");
                            }
                        }
                        break;

                    case 4: // Return Media
                        System.out.print("Enter title to return: ");
                        String rt = in.nextLine();
                        Media selectedReturn = null;
                        for (Media m : currentUser.getBorrowedMedia()) {
                            if (m.getTitle().equalsIgnoreCase(rt)) {
                                selectedReturn = m;
                                break;
                            }
                        }
                        if (selectedReturn == null) {
                            System.out.println("You didn’t borrow this media.");
                        } else {
                            library.returnMedia(currentUser, selectedReturn);
                            System.out.println("Returned successfully.");
                        }
                        break;

                    case 5: // View My Fines
                        System.out.println("Your total fines = " + currentUser.getTotalFines());
                        break;

                    case 6: // Pay Fine
                        System.out.print("Enter amount to pay: ");
                        double amount = Double.parseDouble(in.nextLine());
                        currentUser.payFine(amount);
                        System.out.println("New fine balance = " + currentUser.getFineBalance());
                        break;

                    case 0:
                        System.out.println("Goodbye!");
                        break;

                    default:
                        System.out.println("Invalid choice.");
                }
            }

        } while (choice != 0);

        in.close();
    }
}
*/

package com.library;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        Library library = new Library();
        Admin admin = new Admin("admin", "1234");  // حساب الادمن جاهز
        User currentUser = null;
        boolean isAdmin = false;

        System.out.println("=== Library System ===");

        // اختيار الدور
        while (true) {
            System.out.print("Login as Admin or User? (A/U): ");
            String role = in.nextLine().trim().toUpperCase();
            if (role.equals("A")) {
                // تسجيل دخول Admin
                System.out.print("Enter admin username: ");
                String u = in.nextLine();
                System.out.print("Enter admin password: ");
                String p = in.nextLine();
                if (admin.login(u, p)) {
                    System.out.println("Admin logged in successfully.");
                    isAdmin = true;
                    break;
                } else {
                    System.out.println("Invalid admin credentials. Try again.");
                }
            } else if (role.equals("U")) {
                // تسجيل دخول User
                System.out.print("Enter username: ");
                String uname = in.nextLine();
                System.out.print("Enter password: ");
                String pass = in.nextLine();
                if (uname.equals("user") && pass.equals("1011")) {
                    currentUser = new User(uname);
                    library.addUser(currentUser); // تسجيل المستخدم
                    System.out.println("User logged in successfully.");
                    break;
                } else {
                    System.out.println("Invalid user credentials. Try again.");
                }
            } else {
                System.out.println("Invalid choice. Enter A for Admin or U for User.");
            }
        }

        int choice;
        do {
            System.out.println("\n===== MENU =====");

            if (isAdmin) {
                System.out.println("1. Add Book");
                System.out.println("2. Add CD");
                System.out.println("3. List All Media");
                System.out.println("4. Search Media");
                System.out.println("5. View User Fines");
                System.out.println("0. Exit");
            } else {
                System.out.println("1. List All Media");
                System.out.println("2. Search Media");
                System.out.println("3. Borrow Media");
                System.out.println("4. Return Media");
                System.out.println("5. View My Fines");
                System.out.println("6. Pay Fine");
                System.out.println("0. Exit");
            }

            System.out.print("Choice: ");
            choice = Integer.parseInt(in.nextLine());

            if (isAdmin) {
                switch (choice) {
                    case 1: // Add Book
                        System.out.print("Book Title: ");
                        String title = in.nextLine();
                        System.out.print("Author: ");
                        String author = in.nextLine();
                        System.out.print("ISBN: ");
                        String isbn = in.nextLine();
                        library.addBook(admin, new Book(title, author, isbn));
                        System.out.println("Book added.");
                        break;

                    case 2: // Add CD
                        System.out.print("CD Title: ");
                        String ct = in.nextLine();
                        System.out.print("Artist: ");
                        String artist = in.nextLine();
                        library.addCD(admin, new CD(ct, artist));
                        System.out.println("CD added.");
                        break;

                    case 3: // List All Media
                        List<Media> mediaList = library.getMediaList();
                        if (mediaList.isEmpty()) {
                            System.out.println("No media available.");
                        } else {
                            System.out.println("\n--- All Media ---");
                            for (Media m : mediaList) System.out.println(m);
                        }
                        break;

                    case 4: // Search Media
                        System.out.print("Enter keyword: ");
                        String kw = in.nextLine();
                        List<Media> results = library.searchMedia(kw);
                        if (results.isEmpty()) {
                            System.out.println("No media found.");
                        } else {
                            System.out.println("\n--- Search Results ---");
                            for (Media m : results) System.out.println(m);
                        }
                        break;

                    case 5: // View User Fines
                        System.out.print("Enter username to check fines: ");
                        String uname = in.nextLine();
                        User user = library.getUserByName(uname);
                        if (user != null) {
                            System.out.println("User " + uname + " fine balance: " + user.getTotalFines());
                        } else {
                            System.out.println("User not found.");
                        }
                        break;

                    case 0:
                        System.out.println("Goodbye!");
                        break;

                    default:
                        System.out.println("Invalid choice.");
                }
            } else {
                switch (choice) {
                    case 1: // List All Media
                        List<Media> mediaList = library.getMediaList();
                        if (mediaList.isEmpty()) {
                            System.out.println("No media available.");
                        } else {
                            System.out.println("\n--- All Media ---");
                            for (Media m : mediaList) System.out.println(m);
                        }
                        break;

                    case 2: // Search Media
                        System.out.print("Enter keyword: ");
                        String kw = in.nextLine();
                        List<Media> results = library.searchMedia(kw);
                        if (results.isEmpty()) {
                            System.out.println("No media found.");
                        } else {
                            System.out.println("\n--- Search Results ---");
                            for (Media m : results) System.out.println(m);
                        }
                        break;

                    case 3: // Borrow Media
                        System.out.print("Enter title to borrow: ");
                        String bt = in.nextLine();
                        Media selectedBorrow = null;
                        for (Media m : library.getMediaList()) {
                            if (m.getTitle().equalsIgnoreCase(bt)) {
                                selectedBorrow = m;
                                break;
                            }
                        }
                        if (selectedBorrow == null) {
                            System.out.println("Media not found.");
                        } else {
                            if (library.borrowMedia(currentUser, selectedBorrow)) {
                                System.out.println("Borrowed successfully.");
                                selectedBorrow.setDueDate(LocalDate.now().minusDays(5)); // الكتاب متأخر 5 أيام

                            } else {
                                System.out.println("Cannot borrow (maybe overdue or fines exist).");
                            }
                        }
                        break;

                    case 4: // Return Media
                        System.out.print("Enter title to return: ");
                        String rt = in.nextLine();
                        Media selectedReturn = null;
                        for (Media m : currentUser.getBorrowedMedia()) {
                            if (m.getTitle().equalsIgnoreCase(rt)) {
                                selectedReturn = m;
                                break;
                            }
                        }
                        if (selectedReturn == null) {
                            System.out.println("You didn’t borrow this media.");
                        } else {
                            library.returnMedia(currentUser, selectedReturn);
                            System.out.println("Returned successfully.");
                        }
                        break;

                    case 5: // View My Fines
                        System.out.println("Your total fines = " + currentUser.getTotalFines());
                        break;

                    case 6: // Pay Fine
                        System.out.print("Enter amount to pay: ");
                        double amount = Double.parseDouble(in.nextLine());
                        currentUser.payFine(amount);
                        System.out.println("New fine balance = " + currentUser.getTotalFines());
                        break;

                    case 0:
                        System.out.println("Goodbye!");
                        break;

                    default:
                        System.out.println("Invalid choice.");
                }
            }

        } while (choice != 0);

        in.close();
    }
}
