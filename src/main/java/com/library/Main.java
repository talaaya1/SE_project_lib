/*
package com.library;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static Library library = new Library();
    private static Admin currentAdmin = null;

    private static Librarian  currentLibrarian = null;
    private static User currentUser = null;
    private static EmailManager emailManager = null;

    static {
        // تهيئة مدير البريد
        if (EmailConfig.isConfigured()) {
            emailManager = new EmailManager(
                    EmailConfig.getEmail(),
                    EmailConfig.getAppPassword()
            );
            System.out.println("✅ Email service initialized");

            // ربط emailManager مع Library
            library.setEmailManager(emailManager);
        } else {
            System.out.println("⚠️  Email service not configured");
            System.out.println("💡 Edit email_config.properties to enable emails");
        }
    }

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   LIBRARY MANAGEMENT SYSTEM");
        System.out.println("========================================");

        boolean running = true;
        while (running) {
            // كل الـ3 يجب يكونوا null عشان نعرض Login Menu
            if (currentAdmin == null && currentLibrarian == null && currentUser == null) {
                running = showLoginMenu();
            } else if (currentAdmin != null) {
                running = showAdminMenu();
            } else if (currentLibrarian != null) {  // ← أضيفي هذا الشرط
                running = showLibrarianMenu();
            } else {
                running = showUserMenu();
            }
        }

        System.out.println("\nThank you for using our Library!");
        scanner.close();
    }

    private static boolean showLoginMenu() {
        System.out.println("\n=== LOGIN MENU ===");
        System.out.println("1. Admin Login");
        System.out.println("2. Librarian Login"); // ← جديد
        System.out.println("3. User Login");
        System.out.println("4. User Registration");
        System.out.println("5. Change Password");
        System.out.println("6. Exit");
        System.out.print("Choice: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    adminLogin();
                    break;
                case 2: // ← جديد
                    librarianLogin();
                    break;
                case 3:
                    userLogin();
                    break;
                case 4:
                    userRegistration();
                    break;
                case 5:
                    changePasswordFromLogin();
                    break;
                case 6:
                    return false;
                default:
                    System.out.println("Invalid choice.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }

        return true;
    }

    private static void adminLogin() {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        currentAdmin = library.authenticateAdmin(email, password);
        if (currentAdmin != null && currentAdmin.isLoggedIn()) {
            System.out.println("✅ Login successful! Welcome Admin " + currentAdmin.getUsername());
        } else {
            System.out.println("❌ Invalid credentials.");
            currentAdmin = null;
        }
    }

    private static void librarianLogin() {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        currentLibrarian = library.authenticateLibrarian(email, password);
        if (currentLibrarian != null) {
            System.out.println("✅ Login successful! Welcome Librarian " + currentLibrarian.getName());
        } else {
            System.out.println("❌ Invalid credentials.");
            currentLibrarian = null;
        }
    }


    private static boolean showLibrarianMenu() {
        if (currentLibrarian == null || !currentLibrarian.isLoggedIn()) {
            System.out.println("❌ Session expired.");
            currentLibrarian = null;
            return true;
        }

        System.out.println("\n=== LIBRARIAN MENU ===");
        System.out.println("1. View Available Media");
        System.out.println("2. Search Media");
        System.out.println("3. View All Users");
        System.out.println("4. View All Overdue Media");
        System.out.println("5. Detect Severely Overdue Books (>28 days)");
        System.out.println("6. Issue Fines for Severe Overdue");
        System.out.println("7. Generate Severe Overdue Report");
        System.out.println("8. Logout");
        System.out.print("Choice: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    viewAvailableMedia();
                    break;
                case 2:
                    searchMedia();
                    break;
                case 3:
                    viewAllUsers();
                    break;
                case 4:
                    viewOverdueMedia();
                    break;
                case 5:
                    detectSeverelyOverdue();
                    break;
                case 6:
                    issueFinesForSevereOverdue();
                    break;
                case 7:
                    generateSevereOverdueReport();
                    break;
                case 8:
                    currentLibrarian.logout();
                    currentLibrarian = null;
                    System.out.println("✅ Logged out.");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }

        return true;
    }

    private static void detectSeverelyOverdue() {
        List<Media> severelyOverdue = library.detectSeverelyOverdueBooks();

        if (severelyOverdue.isEmpty()) {
            System.out.println("✅ No books overdue more than 28 days.");
            return;
        }

        System.out.println("\n" + "=".repeat(70));
        System.out.println("⚠️  SEVERELY OVERDUE BOOKS DETECTED (>28 DAYS)");
        System.out.println("Found " + severelyOverdue.size() + " critical overdue books");
        System.out.println("=".repeat(70));

        System.out.printf("%-3s %-25s %-20s %-12s %-10s%n",
                "No", "Book Title", "Borrower", "Due Date", "Days Overdue");
        System.out.println("-".repeat(70));

        for (int i = 0; i < severelyOverdue.size(); i++) {
            Media media = severelyOverdue.get(i);
            long overdueDays = java.time.temporal.ChronoUnit.DAYS.between(
                    media.getDueDate(), LocalDate.now());

            String borrowerName = "Unknown";
            User borrower = library.getUserByEmail(media.getBorrowerEmail());
            if (borrower != null) {
                borrowerName = borrower.getName();
            }

            System.out.printf("%-3d %-25s %-20s %-12s %-10d%n",
                    (i + 1),
                    media.getTitle(),
                    borrowerName,
                    media.getDueDate(),
                    overdueDays);
        }

        // حساب الغرامات المتوقعة
        double totalFines = 0.0;
        for (Media media : severelyOverdue) {
            long overdueDays = java.time.temporal.ChronoUnit.DAYS.between(
                    media.getDueDate(), LocalDate.now());
            totalFines += overdueDays * 10.0;
        }

        System.out.println("-".repeat(70));
        System.out.println("💰 Estimated total fines: " + totalFines + " NIS");
        System.out.println("💡 Use option 6 to issue fines automatically");
    }

    private static void issueFinesForSevereOverdue() {
        System.out.println("\n=== ISSUE FINES FOR SEVERE OVERDUE BOOKS ===");

        List<Media> severelyOverdue = library.detectSeverelyOverdueBooks();

        if (severelyOverdue.isEmpty()) {
            System.out.println("✅ No books overdue more than 28 days.");
            return;
        }

        System.out.println("Found " + severelyOverdue.size() + " books overdue > 28 days");

        double minFines = severelyOverdue.size() * 280; // 28 يوم × 10 NIS
        System.out.println("Minimum fines to issue: " + minFines + " NIS");

        System.out.print("\n⚠️  This will issue fines automatically to all affected users.");
        System.out.print("\nAre you sure? (Type 'YES' to confirm): ");
        String confirm = scanner.nextLine().trim().toUpperCase();

        if (confirm.equals("YES")) {
            library.issueFinesForSeverelyOverdue(currentLibrarian);
        } else {
            System.out.println("Operation cancelled.");
        }
    }

    private static void generateSevereOverdueReport() {
        library.showSevereOverdueReport(currentLibrarian);

        System.out.print("\nSave this report to file? (Y/N): ");
        String saveChoice = scanner.nextLine().trim().toUpperCase();

        if (saveChoice.equals("Y") || saveChoice.equals("YES")) {
            String filename = "severe_overdue_report_" + LocalDate.now() + ".txt";
            System.out.println("✅ Report would be saved as: " + filename);
        }
    }
    private static void userLogin() {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        currentUser = library.authenticateUser(email, password);
        if (currentUser != null) {
            System.out.println("✅ Login successful! Welcome " + currentUser.getName());
        } else {
            System.out.println("❌ Invalid credentials.");
        }
    }

    private static void userRegistration() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User newUser = library.registerUser(name, email, password);
        if (newUser != null) {
            System.out.println("✅ Registration successful!");

            // إرسال بريد ترحيبي
            if (emailManager != null) {
                System.out.print("📧 Sending welcome email... ");
                emailManager.sendWelcomeEmail(email, name);
                System.out.println("Done!");
            }

            currentUser = newUser;
        } else {
            System.out.println("❌ Registration failed. Email may already exist.");
        }
    }

    private static void changePasswordFromLogin() {
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();

        User user = library.getUserByEmail(email);
        if (user == null) {
            System.out.println("❌ User not found.");
            return;
        }

        System.out.print("Enter current password: ");
        String current = scanner.nextLine();

        if (!user.verifyPassword(current)) {
            System.out.println("❌ Current password is incorrect.");
            return;
        }

        System.out.print("Enter new password: ");
        String newPass = scanner.nextLine();

        System.out.print("Confirm new password: ");
        String confirm = scanner.nextLine();

        if (!newPass.equals(confirm)) {
            System.out.println("❌ New passwords don't match.");
            return;
        }

        if (user.changePassword(current, newPass)) {
            System.out.println("✅ Password changed successfully!");
            library.updateUserPassword(user);
        } else {
            System.out.println("❌ Failed to change password.");
        }
    }

    private static boolean showAdminMenu() {
        if (currentAdmin == null || !currentAdmin.isLoggedIn()) {
            System.out.println("❌ Session expired.");
            currentAdmin = null;
            return true;
        }

        System.out.println("\n=== ADMIN MENU ===");
        System.out.println("1. Add Book");
        System.out.println("2. Add CD");
        System.out.println("3. View All Media");
        System.out.println("4. Search Media");
        System.out.println("5. View All Users");
        System.out.println("6. Unregister User");
        System.out.println("7. View Overdue Media");
        System.out.println("8. Send Overdue Reminders");
        System.out.println("9. Add New Librarian"); // ← جديد
        System.out.println("10. System Statistics");
        System.out.println("11. Logout");
        System.out.print("Choice: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    addCD();
                    break;
                case 3:
                    viewAllMedia();
                    break;
                case 4:
                    searchMedia();
                    break;
                case 5:
                    viewAllUsers();
                    break;
                case 6:
                    unregisterUser();
                    break;
                case 7:
                    viewOverdueMedia();
                    break;
                case 8:
                    sendOverdueReminders(); // ← تم التعديل
                    break;
                case 9:
                    addNewLibrarian();
                    break;
                case 10:
                    showSystemStatistics();
                    break;
                case 11:
                    currentAdmin.logout();
                    currentAdmin = null;
                    System.out.println("✅ Logged out.");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }

        return true;
    }

    private static void addNewLibrarian() {
        System.out.println("\n=== ADD NEW LIBRARIAN ===");

        System.out.print("Enter librarian name: ");
        String name = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        Librarian newLibrarian = new Librarian(name, email, password);
        boolean added = library.addLibrarian(currentAdmin, newLibrarian);

        if (added) {
            System.out.println("✅ Librarian account created successfully!");
            System.out.println("Login details:");
            System.out.println("- Email: " + email);
            System.out.println("- Password: " + password);
        }
    }

    // ===== دالة إحصائيات النظام =====
    private static void showSystemStatistics() {
        System.out.println("\n=== SYSTEM STATISTICS ===");

        List<Media> mediaList = library.getAllMedia();
        List<User> users = library.getAllUsers();
        List<Media> overdue = library.getAllOverdueMedia();
        List<Media> severeOverdue = library.detectSeverelyOverdueBooks();

        System.out.println("📊 LIBRARY STATISTICS:");
        System.out.println("• Total media items: " + mediaList.size());

        int books = 0, cds = 0;
        for (Media media : mediaList) {
            if (media instanceof Book) books++;
            else if (media instanceof CD) cds++;
        }
        System.out.println("  - Books: " + books);
        System.out.println("  - CDs: " + cds);

        System.out.println("• Total users: " + users.size());
        System.out.println("• Total overdue items: " + overdue.size());
        System.out.println("• Severely overdue books (>28 days): " + severeOverdue.size());

        // حساب إجمالي الغرامات
        double totalFines = 0.0;
        for (User user : users) {
            totalFines += user.getTotalFines();
        }
        System.out.println("• Total unpaid fines: " + totalFines + " NIS");

        // حساب الكتب المستعارة حالياً
        int borrowedCount = 0;
        for (Media media : mediaList) {
            if (media.isBorrowed()) borrowedCount++;
        }
        System.out.println("• Currently borrowed items: " + borrowedCount);
    }

    // أضيفي هذه الدالة الجديدة في Main.java
    private static void sendOverdueReminders() {
        System.out.println("\n=== SEND OVERDUE REMINDERS ===");

        // عرض إحصائيات أولاً
        List<Media> allOverdue = library.getAllOverdueMedia();
        if (allOverdue.isEmpty()) {
            System.out.println("✅ No overdue items in the system.");
            return;
        }

        System.out.println("Found " + allOverdue.size() + " overdue items:");

        // تجميع حسب المستخدم
        java.util.Map<String, List<Media>> userOverdueMap = new java.util.HashMap<>();
        for (Media media : allOverdue) {
            String userEmail = media.getBorrowerEmail();
            userOverdueMap.computeIfAbsent(userEmail, k -> new java.util.ArrayList<>())
                    .add(media);
        }

        System.out.println("\nAffected users: " + userOverdueMap.size());
        for (String email : userOverdueMap.keySet()) {
            User user = library.getUserByEmail(email);
            if (user != null) {
                System.out.println("• " + user.getName() + " (" + email + "): " +
                        userOverdueMap.get(email).size() + " items");
            }
        }

        // طلب التأكيد
        System.out.print("\nSend reminder emails to these users? (Y/N): ");
        String confirm = scanner.nextLine().trim().toUpperCase();

        if (confirm.equals("Y") || confirm.equals("YES")) {
            System.out.println("\n📧 Sending reminders...");
            library.sendOverdueReminders(currentAdmin);
        } else {
            System.out.println("Operation cancelled.");
        }
    }

    private static boolean showUserMenu() {
        System.out.println("\n=== USER MENU ===");
        System.out.println("1. View Available Media");
        System.out.println("2. Search Media");
        System.out.println("3. Borrow Media");
        System.out.println("4. Return Media");
        System.out.println("5. View My Borrowed Items");
        System.out.println("6. View My Fines");
        System.out.println("7. Pay Fine");
        System.out.println("8. Change Password");
        System.out.println("9. Logout");
        System.out.print("Choice: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    viewAvailableMedia();
                    break;
                case 2:
                    searchMedia();
                    break;
                case 3:
                    borrowMedia();
                    break;
                case 4:
                    returnMedia();
                    break;
                case 5:
                    viewMyBorrowedItems();
                    break;
                case 6:
                    viewMyFines();
                    break;
                case 7:
                    payFine();
                    break;

                case 8:
                    changePassword();
                    break;
                case 9:
                    currentUser = null;
                    System.out.println("✅ Logged out.");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }

        return true;
    }

    private static void addBook() {
        System.out.print("Book title: ");
        String title = scanner.nextLine();
        System.out.print("Author: ");
        String author = scanner.nextLine();
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();
        System.out.print("Copies: ");

        try {
            int copies = Integer.parseInt(scanner.nextLine());
            Book book = new Book(title, author, isbn, copies);
            library.addBook(currentAdmin, book);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number.");
        }
    }

    private static void addCD() {
        System.out.print("CD title: ");
        String title = scanner.nextLine();
        System.out.print("Artist: ");
        String artist = scanner.nextLine();
        System.out.print("CD ID: ");
        String cdId = scanner.nextLine();
        System.out.print("Copies: ");

        try {
            int copies = Integer.parseInt(scanner.nextLine());
            CD cd = new CD(title, artist, cdId, copies);
            library.addCD(currentAdmin, cd);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number.");
        }
    }

    private static void viewAllMedia() {
        List<Media> mediaList = library.getAllMedia();
        if (mediaList.isEmpty()) {
            System.out.println("No media available.");
            return;
        }

        System.out.println("\n=== ALL MEDIA ===");
        for (int i = 0; i < mediaList.size(); i++) {
            Media media = mediaList.get(i);
            System.out.println((i + 1) + ". " + media);
        }
    }

    private static void viewAvailableMedia() {
        List<Media> mediaList = library.getAllMedia();
        if (mediaList.isEmpty()) {
            System.out.println("No media available.");
            return;
        }

        System.out.println("Which type do you want to view?");
        System.out.println("1. Books");
        System.out.println("2. CDs");
        System.out.print("Choice: ");
        int typeChoice = Integer.parseInt(scanner.nextLine());

        int count = 0;

        if (typeChoice == 1) {
            System.out.println("\n=== AVAILABLE BOOKS ===");
            // طباعة عنوان الجدول مع زيادة المسافة بين الأعمدة
            System.out.printf("%-3s %-25s %-20s %-15s %-12s%n", "No", "Title", "Author", "Available Copies", "Status");
            System.out.println("-------------------------------------------------------------------------------");
            for (Media media : mediaList) {
                if (media instanceof Book) {
                    Book book = (Book) media;
                    String status = (book.getAvailableCopies() > 0) ? "Available" : "Not Available";
                    System.out.printf("%-3d %-25s %-20s %-15d %-12s%n", ++count, book.getTitle(), book.getAuthor(),
                            book.getAvailableCopies(), status); // هنا بدلنا getTotalCopies() بـ getAvailableCopies()
                }
            }
        } else if (typeChoice == 2) {
            System.out.println("\n=== AVAILABLE CDS ===");
            System.out.printf("%-3s %-25s %-20s %-15s %-12s%n", "No", "Title", "Artist", "Available Copies", "Status");
            System.out.println("-------------------------------------------------------------------------------");
            for (Media media : mediaList) {
                if (media instanceof CD) {
                    CD cd = (CD) media;
                    String status = (cd.getAvailableCopies() > 0) ? "Available" : "Not Available";
                    System.out.printf("%-3d %-25s %-20s %-15d %-12s%n", ++count, cd.getTitle(), cd.getArtist(),
                            cd.getAvailableCopies(), status); // هنا برضو
                }
            }
        } else {
            System.out.println("Invalid choice!");
        }

        if (count == 0) {
            System.out.println("No available items in this category.");
        }
    }

    private static void searchMedia() {
        System.out.print("Search keyword: ");
        String keyword = scanner.nextLine();

        List<Media> results = library.searchMedia(keyword);
        if (results.isEmpty()) {
            System.out.println("No results found.");
            return;
        }

        System.out.println("\n=== SEARCH RESULTS ===");
        for (int i = 0; i < results.size(); i++) {
            Media media = results.get(i);
            System.out.println((i + 1) + ". " + media);
        }
    }

    private static void viewAllUsers() {
        List<User> users = library.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("No users registered.");
            return;
        }

        System.out.println("\n=== ALL USERS ===");
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            System.out.println((i + 1) + ". " + user.getName() +
                    " (" + user.getEmail() + ") - Fines: " +
                    user.getTotalFines() + " NIS");
        }
    }

    private static void unregisterUser() {
        System.out.print("User email to unregister: ");
        String email = scanner.nextLine();

        library.unregisterUser(currentAdmin, email);
    }

    private static void viewOverdueMedia() {
        List<Media> overdue = library.getAllOverdueMedia();
        if (overdue.isEmpty()) {
            System.out.println("✅ No overdue media.");
            return;
        }

        System.out.println("\n=== OVERDUE MEDIA REPORT ===");
        System.out.println("Total overdue items: " + overdue.size());
        System.out.println("============================\n");

        System.out.printf("%-3s %-25s %-20s %-15s %-15s %-10s%n",
                "No", "Title", "Type", "Borrower", "Due Date", "Days Late");
        System.out.println("--------------------------------------------------------------------------------");

        for (int i = 0; i < overdue.size(); i++) {
            Media media = overdue.get(i);
            LocalDate today = LocalDate.now();
            long daysLate = java.time.temporal.ChronoUnit.DAYS.between(
                    media.getDueDate(), today);

            String borrowerName = "Unknown";
            User borrower = library.getUserByEmail(media.getBorrowerEmail());
            if (borrower != null) {
                borrowerName = borrower.getName();
            }

            System.out.printf("%-3d %-25s %-20s %-15s %-15s %-10d%n",
                    (i + 1),
                    media.getTitle(),
                    media.getType(),
                    borrowerName,
                    media.getDueDate(),
                    daysLate);
        }

        // حساب إجمالي الغرامات
        double totalFines = 0.0;
        for (Media media : overdue) {
            long daysLate = java.time.temporal.ChronoUnit.DAYS.between(
                    media.getDueDate(), LocalDate.now());
            double fine = media instanceof Book ? daysLate * 10.0 : daysLate * 20.0;
            totalFines += fine;
        }

        System.out.println("\n📊 Summary:");
        System.out.println("• Total overdue items: " + overdue.size());
        System.out.println("• Total estimated fines: " + totalFines + " NIS");
        System.out.println("• Affected users: " +
                overdue.stream().map(Media::getBorrowerEmail).distinct().count());
    }

    private static void borrowMedia() {
        if (!currentUser.canBorrow()) {
            System.out.println("❌ Cannot borrow. Check fines or overdue items.");
            return;
        }

        System.out.print("Media title to borrow: ");
        String title = scanner.nextLine();

        boolean success = library.borrowMedia(currentUser, title); // في Library.java سيتم تقليل availableCopies
        if (success && emailManager != null) {
            // إرسال تأكيد الاستعارة
            for (Media media : currentUser.getBorrowedMedia()) {
                if (media.getTitle().equals(title)) {
                    emailManager.sendBorrowConfirmation(
                            currentUser.getEmail(),
                            currentUser.getName(),
                            media,
                            media.getDueDate()
                    );
                    break;
                }
            }
        }
    }

    private static void returnMedia() {
        System.out.print("Enter media title to return: ");
        String title = scanner.nextLine();

        System.out.print("Enter return date (YYYY-MM-DD) or press Enter for today: ");
        String dateInput = scanner.nextLine();

        LocalDate returnDate;
        if (dateInput.isEmpty()) {
            returnDate = LocalDate.now();
            System.out.println("📅 Using today's date: " + returnDate);
        } else {
            try {
                returnDate = LocalDate.parse(dateInput);
                System.out.println("📅 Using specified date: " + returnDate);
            } catch (Exception e) {
                System.out.println("❌ Invalid date format. Using today's date.");
                returnDate = LocalDate.now();
            }
        }

        // البحث عن المادة للتأكيد
        Media borrowedMedia = null;
        for (Media m : currentUser.getBorrowedMedia()) {
            if (m.getTitle().equalsIgnoreCase(title)) {
                borrowedMedia = m;
                break;
            }
        }

        if (borrowedMedia == null) {
            System.out.println("❌ You haven't borrowed this item: \"" + title + "\"");
            showBorrowedItems();
            return;
        }

        // إذا كان الإرجاع متأخراً، طلب التأكيد
        if (returnDate.isAfter(borrowedMedia.getDueDate())) {
            long overdueDays = java.time.temporal.ChronoUnit.DAYS.between(
                    borrowedMedia.getDueDate(), returnDate);
            double fine = borrowedMedia instanceof Book ?
                    overdueDays * 10.0 : overdueDays * 20.0;

            System.out.println("\n⚠️  This return is " + overdueDays + " days late!");
            System.out.println("💰 Fine to be applied: " + fine + " NIS");

            System.out.print("Continue? (Y/N): ");
            String confirm = scanner.nextLine().trim().toUpperCase();

            if (!confirm.equals("Y") && !confirm.equals("YES")) {
                System.out.println("Return cancelled.");
                return;
            }
        }

        // استدعاء المكتبة للإرجاع + زيادة availableCopies تلقائيًا
        library.returnMedia(currentUser, title, returnDate);
    }


    private static void viewMyBorrowedItems() {
        List<Media> borrowed = currentUser.getBorrowedMedia();
        if (borrowed.isEmpty()) {
            System.out.println("You have no borrowed items.");
            return;
        }

        System.out.println("\n=== MY BORROWED ITEMS ===");
        for (int i = 0; i < borrowed.size(); i++) {
            Media media = borrowed.get(i);
            System.out.println((i + 1) + ". " + media.getTitle() +
                    " (" + media.getType() + ") - Due: " + media.getDueDate());
        }
    }

    private static void showBorrowedItems() {
        List<Media> borrowed = currentUser.getBorrowedMedia();
        if (borrowed.isEmpty()) {
            System.out.println("📭 You have no borrowed items.");
            return;
        }

        System.out.println("\n📋 Your borrowed items:");
        for (int i = 0; i < borrowed.size(); i++) {
            Media m = borrowed.get(i);
            System.out.println((i + 1) + ". " + m.getTitle() +
                    " (" + m.getType() + ") - Due: " + m.getDueDate());
        }
    }

    private static void viewMyFines() {
        System.out.println("Your fines: " + currentUser.getTotalFines() + " NIS");
    }

    private static void payFine() {
        System.out.println("Current fines: " + currentUser.getTotalFines() + " NIS");

        if (currentUser.getTotalFines() <= 0) {
            System.out.println("You have no fines to pay.");
            return;
        }

        System.out.print("Amount to pay: ");
        try {
            double amount = Double.parseDouble(scanner.nextLine());
            double oldBalance = currentUser.getTotalFines();

            if (currentUser.payFine(amount)) {
                System.out.println("✅ Payment successful.");

                double remaining = currentUser.getTotalFines();
                System.out.println("💰 Remaining unpaid fines: " + remaining + " NIS");

                // إرسال إيصال الدفع
                if (emailManager != null) {
                    emailManager.sendPaymentReceipt(
                            currentUser.getEmail(),
                            currentUser.getName(),
                            amount,
                            currentUser.getTotalFines(),
                            "Online Payment"
                    );
                }
            } else {
                System.out.println("❌ Payment failed.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount.");
        }
    }

    private static void changePassword() {
        System.out.print("Enter current password: ");
        String current = scanner.nextLine();

        System.out.print("Enter new password: ");
        String newPass = scanner.nextLine();

        System.out.print("Confirm new password: ");
        String confirm = scanner.nextLine();

        if (!newPass.equals(confirm)) {
            System.out.println("❌ New passwords don't match.");
            return;
        }

        if (newPass.length() < 6) {
            System.out.println("❌ Password must be at least 6 characters.");
            return;
        }

        if (currentUser.changePassword(current, newPass)) {
            System.out.println("✅ Password changed successfully!");
            library.updateUserPassword(currentUser);
        } else {
            System.out.println("❌ Current password is incorrect.");
        }
    }

    private static void setupDefaultAdmin() {
        System.out.println("\n🔧 Setting up default administrator...");

        // تحقق إذا كان Admin موجوداً
        Admin existingAdmin = library.authenticateAdmin("admin@gmail.com", "admin123");
        if (existingAdmin == null) {
            System.out.println("⚠️  No admin found. Creating default admin...");

            // أنشئ Admin جديد
            Admin defaultAdmin = new Admin("System Admin", "admin@gmail.com", "admin123");

            // أضفه إلى المكتبة
            library.addAdmin(defaultAdmin);

            System.out.println("✅ Default admin created!");
            System.out.println("   Email: admin@gmail.com");
            System.out.println("   Password: admin123");
            System.out.println("   Username: System Admin");
        } else {
            System.out.println("✅ Admin already exists in system.");
        }
        System.out.println();
    }
}


*/