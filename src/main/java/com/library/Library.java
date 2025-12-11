package com.library;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Library {

    private static final Logger logger = Logger.getLogger(Library.class.getName());

    private List<Media> mediaList = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private List<Admin> admins = new ArrayList<>();
    private List<Librarian> librarians = new ArrayList<>();
    private EmailManager emailManager;

    private final String BOOKS_FILE = "books.txt";
    private final String CDS_FILE = "cds.txt";
    private final String BORROWED_FILE = "borrowed.txt";
    private final String RETURNED_FILE = "returned.txt";
    private final String USERS_FILE = "users.txt";
    private final String ADMINS_FILE = "admins.txt";
    private final String LIBRARIANS_FILE = "librarians.txt";
    private final String FINES_DETAILS = "fines_details.txt";
    private final String FINES_FILE = "fines.txt";

    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    // ====================== Constructor ======================
    public Library() {
        loadAdmins();
        loadLibrarians();
        loadUsers();
        loadBooks();
        loadCDs();
        loadBorrowedMedia();
        loadFines();
    }

    public Library(boolean loadFiles) {
        if (loadFiles) {
            this.mediaList = new ArrayList<>();
            this.users = new ArrayList<>();
            this.admins = new ArrayList<>();
            this.librarians = new ArrayList<>();
            loadAdmins();
            loadLibrarians();
            loadUsers();
            loadBooks();
            loadCDs();
            loadBorrowedMedia();
            loadFines();
        } else {
            this.mediaList = new ArrayList<>();
            this.users = new ArrayList<>();
            this.admins = new ArrayList<>();
            this.librarians = new ArrayList<>();
        }
    }

    // ====================== Logger Helper ======================
    private void logInfo(String msg) {
        logger.info(msg);
    }

    private void logWarning(String msg) {
        logger.warning(msg);
    }

    private void logSevere(String msg, Exception e) {
        logger.log(Level.SEVERE, msg, e);
    }

    // ====================== File Helpers ======================
    private void writeToFile(String filename, String content, boolean append) {
        try (FileWriter fw = new FileWriter(filename, append)) {
            fw.write(content + "\n");
        } catch (IOException e) {
            logSevere("Error writing to file: " + filename, e);
        }
    }

    private List<String> readLinesFromFile(String filename) {
        List<String> lines = new ArrayList<>();
        File file = new File(filename);
        if (!file.exists()) return lines;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) lines.add(line);
            }
        } catch (IOException e) {
            logSevere("Error reading file: " + filename, e);
        }
        return lines;
    }

    private void overwriteFile(String filename, List<String> lines) {
        try (FileWriter fw = new FileWriter(filename)) {
            for (String line : lines) {
                fw.write(line + "\n");
            }
        } catch (IOException e) {
            logSevere("Error overwriting file: " + filename, e);
        }
    }

    // ====================== Admins ======================
    private void loadAdmins() {
        admins.clear();
        List<String> lines = readLinesFromFile(ADMINS_FILE);
        if (lines.isEmpty()) {
            createDefaultAdmins();
            return;
        }
        for (String line : lines) {
            try {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    admins.add(new Admin(parts[0].trim(), parts[1].trim(), parts[2].trim(), true));
                }
            } catch (Exception e) {
                logWarning("Skipping invalid admin line: " + line);
            }
        }
        logInfo("Loaded " + admins.size() + " admins");
    }

    private void createDefaultAdmins() {
        admins.add(new Admin("admin", "admin@gmail.com", "admin123"));
        admins.add(new Admin("superadmin", "super@gmail.com", "super123"));
        saveAdminsToFile();
        logInfo("✅ Default admins created");
    }

    private void saveAdminsToFile() {
        List<String> lines = new ArrayList<>();
        for (Admin a : admins) lines.add(a.toFileString());
        overwriteFile(ADMINS_FILE, lines);
    }

    // ====================== Librarians ======================
    private void loadLibrarians() {
        librarians.clear();
        List<String> lines = readLinesFromFile(LIBRARIANS_FILE);
        if (lines.isEmpty()) {
            createDefaultLibrarians();
            return;
        }
        for (String line : lines) {
            Librarian lib = Librarian.fromFileString(line);
            if (lib != null) librarians.add(lib);
        }
        logInfo("Loaded " + librarians.size() + " librarians");
    }

    private void createDefaultLibrarians() {
        librarians.add(new Librarian("Main Librarian", "librarian@library.com", "1234"));
        saveLibrariansToFile();
        logInfo("✅ Default librarian created");
    }

    private void saveLibrariansToFile() {
        List<String> lines = new ArrayList<>();
        for (Librarian lib : librarians) lines.add(lib.toFileString());
        overwriteFile(LIBRARIANS_FILE, lines);
    }

    // ====================== Users ======================
    private void loadUsers() {
        users.clear();
        List<String> lines = readLinesFromFile(USERS_FILE);
        for (String line : lines) {
            User u = User.fromFileString(line);
            if (u != null) users.add(u);
        }
        logInfo("Loaded " + users.size() + " users");
    }



    private void loadFines() {
        List<String> lines = readLinesFromFile(FINES_FILE);
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length < 2) continue;
            User u = getUserByEmail(parts[0].trim());
            if (u != null) {
                try { u.setTotalFines(Double.parseDouble(parts[1].trim())); }
                catch (NumberFormatException e) { logWarning("Invalid fine amount: " + line); }
            }
        }
    }

    private void saveFinesToFile() {
        List<String> lines = new ArrayList<>();
        for (User u : users) if (u.getTotalFines() > 0) {
            lines.add(u.getEmail() + "," + u.getTotalFines());
        }
        overwriteFile(FINES_FILE, lines);
    }

    // ====================== Media (Books/CDs) ======================
    private void loadBooks() {
        List<String> lines = readLinesFromFile(BOOKS_FILE);
        for (String line : lines) {
            try {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String title = parts[0].trim();
                    String author = parts[1].trim();
                    String isbn = parts[2].trim();
                    int copies = 1;
                    if (parts.length > 3) {
                        copies = Integer.parseInt(parts[3].trim());
                    }
                    mediaList.add(new Book(title, author, isbn, copies));
                }
            } catch (Exception e) {
                logWarning("Skipping invalid book line: " + line);
            }
        }
        logInfo("Loaded books: " + mediaList.stream().filter(m -> m instanceof Book).count());
    }

    private void loadCDs() {
        List<String> lines = readLinesFromFile(CDS_FILE);
        for (String line : lines) {
            try {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String title = parts[0].trim();
                    String artist = parts[1].trim();
                    String cdId = parts.length > 2 ? parts[2].trim() : "";
                    int copies = parts.length > 3 ? Integer.parseInt(parts[3].trim()) : 1;
                    mediaList.add(new CD(title, artist, cdId, copies));
                }
            } catch (Exception e) {
                logWarning("Skipping invalid CD line: " + line);
            }
        }
        logInfo("Loaded CDs: " + mediaList.stream().filter(m -> m instanceof CD).count());
    }

    // ====================== Borrowed Media ======================
    private void loadBorrowedMedia() {
        List<String> lines = readLinesFromFile(BORROWED_FILE);
        for (String line : lines) {
            try {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    String type = parts[0].trim();
                    String title = parts[1].trim();
                    String borrowerEmail = parts[2].trim();
                    LocalDate borrowDate = LocalDate.parse(parts[3].trim(), formatter);
                    LocalDate dueDate = LocalDate.parse(parts[4].trim(), formatter);
                    Media media = mediaList.stream()
                            .filter(m -> m.getType().equals(type) && m.getTitle().equals(title))
                            .findFirst().orElse(null);
                    if (media != null) {
                        media.setBorrowed(true);
                        media.setBorrowerEmail(borrowerEmail);
                        media.setBorrowDate(borrowDate);
                        media.setDueDate(dueDate);
                        User u = getUserByEmail(borrowerEmail);
                        if (u != null) u.borrowMediaRecord(media);
                    }
                }
            } catch (Exception e) {
                logWarning("Skipping invalid borrowed line: " + line);
            }
        }
    }

    // ====================== User Lookup ======================
    public User getUserByEmail(String email) {
        return users.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst().orElse(null);
    }

    // ====================== Email Manager ======================
    public void setEmailManager(EmailManager emailManager) {
        this.emailManager = emailManager;
    }

    // ===== باقي الوظائف مثل borrowMedia, returnMedia, fines, addAdmin, addLibrarian يمكن تحديثها بنفس الأسلوب مع Logger =====

}