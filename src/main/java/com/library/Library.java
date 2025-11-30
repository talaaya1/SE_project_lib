/*
package com.library;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Library {

    private List<Media> mediaList = new ArrayList<>();

    // ---------------- Sprint 1 ----------------
    // إضافة كتاب
    public void addBook(Book book) {
        mediaList.add(book);
    }

    // جلب كل الكتب
    public List<Book> getBooks() {
        List<Book> books = new ArrayList<>();
        for (Media m : mediaList) {
            if (m instanceof Book) books.add((Book) m);
        }
        return books;
    }

    // البحث عن الكتب بناءً على keyword
    public List<Book> searchBook(String keyword) {
        List<Book> results = new ArrayList<>();
        for (Media m : mediaList) {
            if (m instanceof Book) {
                Book b = (Book) m;
                if (b.getTitle().contains(keyword) ||
                        b.getAuthor().contains(keyword) ||
                        b.getIsbn().contains(keyword)) {
                    results.add(b);
                }
            }
        }
        return results;
    }

    // ---------------- Sprint 2 ----------------
    public boolean borrowBook(User user, Book book) {
        if (!user.canBorrow() || book.isBorrowed()) return false;

        if (user.getFineBalance() > 0 || user.hasOverdueMedia()) return false;

        book.setBorrowed(true);
        book.setBorrowerName(user.getName());
        book.setDueDate(LocalDate.now().plusDays(28));

        user.borrowMediaRecord(book);  // تعديل ليوافق User الجديد
        return true;
    }

    public double returnBook(User user, Book book) {
        if (!book.isBorrowed() || !user.getBorrowedMedia().contains(book)) return 0.0;

        double fine = 0.0;
        if (book.getDueDate() != null && LocalDate.now().isAfter(book.getDueDate())) {
            long daysLate = LocalDate.now().toEpochDay() - book.getDueDate().toEpochDay();
            fine = daysLate * 1.0;
            user.addFine(fine);
        }

        book.setBorrowed(false);
        book.setBorrowerName(null);
        book.setDueDate(null);

        user.returnMediaRecord(book);  // تعديل ليوافق User الجديد
        return fine;
    }

    // ---------------- دعم Media عامة ----------------
    public void addMedia(Media media) {
        mediaList.add(media);
    }

    public List<Media> getMediaList() {
        return mediaList;
    }

    public boolean borrowMedia(User user, Media media) {
        if (user.getFineBalance() > 0 || user.hasOverdueMedia() || media.isBorrowed()) {
            return false;
        }
        media.borrow(user.getName());
        user.borrowMediaRecord(media);  // تسجيل في User
        return true;
    }

    public void returnMedia(User user, Media media) {
        if (!media.isBorrowed()) return;

        if (media.getDueDate() != null && LocalDate.now().isAfter(media.getDueDate())) {
            long daysLate = LocalDate.now().toEpochDay() - media.getDueDate().toEpochDay();
            user.addFine(daysLate * 1.0);
        }

        media.setBorrowed(false);
        media.setBorrowerName(null);
        media.setDueDate(null);
        user.returnMediaRecord(media);  // تسجيل في User
    }
}
*/

/*
package com.library;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Library {

    private List<Media> mediaList = new ArrayList<>();

    // ---------------- Sprint 1 ----------------
    public void addBook(Admin admin, Book book) {
        if(admin != null && admin.isLoggedIn()) {
            mediaList.add(book);
        } else {
            System.out.println("Permission denied: Admin must be logged in to add books.");
        }
    }

    public void addCD(Admin admin, CD cd) {
        if(admin != null && admin.isLoggedIn()) {
            mediaList.add(cd);
        } else {
            System.out.println("Permission denied: Admin must be logged in to add CDs.");
        }
    }

    public List<Book> getBooks() {
        List<Book> books = new ArrayList<>();
        for (Media m : mediaList) {
            if (m instanceof Book) books.add((Book) m);
        }
        return books;
    }

    public List<Book> searchBook(String keyword) {
        List<Book> results = new ArrayList<>();
        for (Media m : mediaList) {
            if (m instanceof Book) {
                Book b = (Book) m;
                if (b.getTitle().contains(keyword) ||
                        b.getAuthor().contains(keyword) ||
                        b.getIsbn().contains(keyword)) {
                    results.add(b);
                }
            }
        }
        return results;
    }

    // ---------------- Sprint 2 ----------------
    public boolean borrowBook(User user, Book book) {
        if (!user.canBorrow() || book.isBorrowed()) return false;

        if (user.getFineBalance() > 0 || user.hasOverdueMedia()) return false;

        book.setBorrowed(true);
        book.setBorrowerName(user.getName());
        book.setDueDate(LocalDate.now().plusDays(28));

        user.borrowMediaRecord(book);
        return true;
    }

    public double returnBook(User user, Book book) {
        if (!book.isBorrowed() || !user.getBorrowedMedia().contains(book)) return 0.0;

        double fine = 0.0;
        if (book.getDueDate() != null && LocalDate.now().isAfter(book.getDueDate())) {
            long daysLate = LocalDate.now().toEpochDay() - book.getDueDate().toEpochDay();
            fine = daysLate * 1.0;
            user.addFine(fine);
        }

        book.setBorrowed(false);
        book.setBorrowerName(null);
        book.setDueDate(null);

        user.returnMediaRecord(book);
        return fine;
    }

    // ---------------- دعم Media عامة ----------------
    public void addMedia(Admin admin, Media media) {
        if(admin != null && admin.isLoggedIn()) {
            mediaList.add(media);
        } else {
            System.out.println("Permission denied: Admin must be logged in to add media.");
        }
    }

    public List<Media> getMediaList() {
        return mediaList;
    }

    public boolean borrowMedia(User user, Media media) {
        if (user.getFineBalance() > 0 || user.hasOverdueMedia() || media.isBorrowed()) {
            return false;
        }
        media.borrow(user.getName());
        user.borrowMediaRecord(media);
        return true;
    }

    public void returnMedia(User user, Media media) {
        if (!media.isBorrowed()) return;

        if (media.getDueDate() != null && LocalDate.now().isAfter(media.getDueDate())) {
            long daysLate = LocalDate.now().toEpochDay() - media.getDueDate().toEpochDay();
            user.addFine(daysLate * 1.0);
        }

        media.setBorrowed(false);
        media.setBorrowerName(null);
        media.setDueDate(null);
        user.returnMediaRecord(media);
    }
}
*/

/*
package com.library;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Library {

    private List<Media> mediaList = new ArrayList<>();
    private final String BOOKS_FILE = "books.txt";
    private final String CDS_FILE = "cds.txt";
    private final String BORROWED_FILE = "borrowed.txt";
    private final String RETURNED_FILE = "returned.txt";
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    public Library() {
        loadBooks();
        loadCDs();
        loadBorrowedMedia();
    }

    // ---------------- Load files ----------------
    private void loadBooks() {
        File f = new File(BOOKS_FILE);
        if (!f.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    mediaList.add(new Book(parts[0], parts[1], parts[2]));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading books: " + e.getMessage());
        }
    }

    private void loadCDs() {
        File f = new File(CDS_FILE);
        if (!f.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    mediaList.add(new CD(parts[0], parts[1]));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading CDs: " + e.getMessage());
        }
    }

    private void loadBorrowedMedia() {
        File f = new File(BORROWED_FILE);
        if (!f.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String type = parts[0];
                    String title = parts[1];
                    String borrower = parts[2];
                    LocalDate due = LocalDate.parse(parts[3], formatter);

                    for (Media m : mediaList) {
                        if (m.getTitle().equals(title) && m.getType().equals(type)) {
                            m.setBorrowed(true);
                            m.setBorrowerName(borrower);
                            m.setDueDate(due);
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading borrowed media: " + e.getMessage());
        }
    }

    // ---------------- Sprint 1 ----------------
    public void addBook(Admin admin, Book book) {
        if(admin != null && admin.isLoggedIn()) {
            mediaList.add(book);
            saveBookToFile(book);
        } else {
            System.out.println("Permission denied: Admin must be logged in to add books.");
        }
    }

    public void addCD(Admin admin, CD cd) {
        if(admin != null && admin.isLoggedIn()) {
            mediaList.add(cd);
            saveCDToFile(cd);
        } else {
            System.out.println("Permission denied: Admin must be logged in to add CDs.");
        }
    }

    private void saveBookToFile(Book book) {
        try (FileWriter fw = new FileWriter(BOOKS_FILE, true)) {
            fw.write(book.getTitle() + "," + book.getAuthor() + "," + book.getIsbn() + "\n");
        } catch (IOException e) {
            System.out.println("Error writing book to file: " + e.getMessage());
        }
    }

    private void saveCDToFile(CD cd) {
        try (FileWriter fw = new FileWriter(CDS_FILE, true)) {
            fw.write(cd.getTitle() + "," + cd.getArtist() + "\n");
        } catch (IOException e) {
            System.out.println("Error writing CD to file: " + e.getMessage());
        }
    }

    // ---------------- Sprint 2 ----------------
    public boolean borrowBook(User user, Book book) {
        if (!user.canBorrow() || book.isBorrowed()) return false;
        if (user.getFineBalance() > 0 || user.hasOverdueMedia()) return false;

        book.setBorrowed(true);
        book.setBorrowerName(user.getName());
        book.setDueDate(LocalDate.now().plusDays(28));
        user.borrowMediaRecord(book);

        saveBorrowedMedia(book);
        return true;
    }

    public double returnBook(User user, Book book) {
        if (!book.isBorrowed() || !user.getBorrowedMedia().contains(book)) return 0.0;

        double fine = 0.0;
        if (book.getDueDate() != null && LocalDate.now().isAfter(book.getDueDate())) {
            long daysLate = LocalDate.now().toEpochDay() - book.getDueDate().toEpochDay();
            fine = daysLate * 1.0;
            user.addFine(fine);
        }

        book.setBorrowed(false);
        book.setBorrowerName(null);
        book.setDueDate(null);

        user.returnMediaRecord(book);
        saveReturnedMedia(book);
        return fine;
    }

    // ---------------- دعم Media عامة ----------------
    public void addMedia(Admin admin, Media media) {
        if(admin != null && admin.isLoggedIn()) {
            mediaList.add(media);
            if (media instanceof Book) saveBookToFile((Book) media);
            if (media instanceof CD) saveCDToFile((CD) media);
        } else {
            System.out.println("Permission denied: Admin must be logged in to add media.");
        }
    }

    public List<Media> getMediaList() {
        return mediaList;
    }

    public boolean borrowMedia(User user, Media media) {
        if (user.getFineBalance() > 0 || user.hasOverdueMedia() || media.isBorrowed()) return false;

        media.borrow(user.getName());
        user.borrowMediaRecord(media);
        saveBorrowedMedia(media);
        return true;
    }

    public void returnMedia(User user, Media media) {
        if (!media.isBorrowed()) return;

        if (media.getDueDate() != null && LocalDate.now().isAfter(media.getDueDate())) {
            long daysLate = LocalDate.now().toEpochDay() - media.getDueDate().toEpochDay();
            user.addFine(daysLate * 1.0);
        }

        media.setBorrowed(false);
        media.setBorrowerName(null);
        media.setDueDate(null);
        user.returnMediaRecord(media);
        saveReturnedMedia(media);
    }

    // ---------------- حفظ استعارة وإرجاع ----------------
    private void saveBorrowedMedia(Media media) {
        try (FileWriter fw = new FileWriter(BORROWED_FILE, true)) {
            fw.write(media.getType() + "," + media.getTitle() + "," + media.getBorrowerName() + "," +
                    media.getDueDate().format(formatter) + "\n");
        } catch (IOException e) {
            System.out.println("Error writing borrowed media: " + e.getMessage());
        }
    }

    private void saveReturnedMedia(Media media) {
        try (FileWriter fw = new FileWriter(RETURNED_FILE, true)) {
            fw.write(media.getType() + "," + media.getTitle() + "," + LocalDate.now().format(formatter) + "\n");
        } catch (IOException e) {
            System.out.println("Error writing returned media: " + e.getMessage());
        }
    }
}
*/


/*    package com.library;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Library {

    private List<Media> mediaList = new ArrayList<>();
    private List<User> users = new ArrayList<>();

    private final String BOOKS_FILE = "books.txt";
    private final String CDS_FILE = "cds.txt";
    private final String BORROWED_FILE = "borrowed.txt";
    private final String RETURNED_FILE = "returned.txt";
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    public Library() {
        loadBooks();
        loadCDs();
        loadBorrowedMedia();
    }

    public Library(boolean skipLoadingFiles) {
        if (!skipLoadingFiles) {
            loadBooks();
            loadCDs();
            loadBorrowedMedia();
        }
    }

    // ---------------- إدارة المستخدمين ----------------
    public void addUser(User user) {
        if (getUserByName(user.getName()) == null) {
            users.add(user);
        } else {
            System.out.println("User already exists.");
        }
    }

    public User getUserByName(String name) {
        for (User u : users) {
            if (u.getName().equalsIgnoreCase(name)) return u;
        }
        return null;
    }

    // ---------------- Load files ----------------
    private void loadBooks() {
        File f = new File(BOOKS_FILE);
        if (!f.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    mediaList.add(new Book(parts[0], parts[1], parts[2]));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading books: " + e.getMessage());
        }
    }

    private void loadCDs() {
        File f = new File(CDS_FILE);
        if (!f.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    mediaList.add(new CD(parts[0], parts[1]));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading CDs: " + e.getMessage());
        }
    }

    private void loadBorrowedMedia() {
        File f = new File(BORROWED_FILE);
        if (!f.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String type = parts[0];
                    String title = parts[1];
                    String borrower = parts[2];
                    LocalDate due = LocalDate.parse(parts[3], formatter);

                    for (Media m : mediaList) {
                        if (m.getTitle().equals(title) && m.getType().equals(type)) {
                            m.setBorrowed(true);
                            m.setBorrowerName(borrower);
                            m.setDueDate(due);
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading borrowed media: " + e.getMessage());
        }
    }

    // ---------------- Admin Actions ----------------
    public void addBook(Admin admin, Book book) {
        if(admin != null && admin.isLoggedIn()) {
            mediaList.add(book);
            saveBookToFile(book);
        } else {
            System.out.println("Permission denied: Admin must be logged in to add books.");
        }
    }

    public void addCD(Admin admin, CD cd) {
        if(admin != null && admin.isLoggedIn()) {
            mediaList.add(cd);
            saveCDToFile(cd);
        } else {
            System.out.println("Permission denied: Admin must be logged in to add CDs.");
        }
    }

    private void saveBookToFile(Book book) {
        try (FileWriter fw = new FileWriter(BOOKS_FILE, true)) {
            fw.write(book.getTitle() + "," + book.getAuthor() + "," + book.getIsbn() + "\n");
        } catch (IOException e) {
            System.out.println("Error writing book to file: " + e.getMessage());
        }
    }

    private void saveCDToFile(CD cd) {
        try (FileWriter fw = new FileWriter(CDS_FILE, true)) {
            fw.write(cd.getTitle() + "," + cd.getArtist() + "\n");
        } catch (IOException e) {
            System.out.println("Error writing CD to file: " + e.getMessage());
        }
    }

    // ---------------- User Actions ----------------
    public boolean borrowMedia(User user, Media media) {
        if (user.getFineBalance() > 0 || user.hasOverdueMedia() || media.isBorrowed()) return false;

        media.borrow(user.getName());
        user.borrowMediaRecord(media);
        saveBorrowedMedia(media);
        return true;
    }

    public void returnMedia(User user, Media media) {
        if (!media.isBorrowed()) return;

        if (media.getDueDate() != null && LocalDate.now().isAfter(media.getDueDate())) {
            long daysLate = LocalDate.now().toEpochDay() - media.getDueDate().toEpochDay();
            user.addFine(daysLate * 1.0);
        }

        media.setBorrowed(false);
        media.setBorrowerName(null);
        media.setDueDate(null);
        user.returnMediaRecord(media);
        saveReturnedMedia(media);
    }

    // ---------------- Save Borrow/Return ----------------
    private void saveBorrowedMedia(Media media) {
        try (FileWriter fw = new FileWriter(BORROWED_FILE, true)) {
            fw.write(media.getType() + "," + media.getTitle() + "," + media.getBorrowerName() + "," +
                    media.getDueDate().format(formatter) + "\n");
        } catch (IOException e) {
            System.out.println("Error writing borrowed media: " + e.getMessage());
        }
    }

    private void saveReturnedMedia(Media media) {
        try (FileWriter fw = new FileWriter(RETURNED_FILE, true)) {
            fw.write(media.getType() + "," + media.getTitle() + "," + LocalDate.now().format(formatter) + "\n");
        } catch (IOException e) {
            System.out.println("Error writing returned media: " + e.getMessage());
        }
    }

    // ---------------- Search ----------------
    public List<Media> searchMedia(String keyword) {
        List<Media> results = new ArrayList<>();
        for (Media m : mediaList) {
            if (m.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                results.add(m);
            } else if (m instanceof Book) {
                Book b = (Book) m;
                if (b.getAuthor().toLowerCase().contains(keyword.toLowerCase()) ||
                        b.getIsbn().toLowerCase().contains(keyword.toLowerCase())) {
                    results.add(b);
                }
            } else if (m instanceof CD) {
                CD cd = (CD) m;
                if (cd.getArtist().toLowerCase().contains(keyword.toLowerCase())) {
                    results.add(cd);
                }
            }
        }
        return results;
    }

    // ---------------- Getters ----------------
    public List<Media> getMediaList() {
        return mediaList;
    }
}
*/

package com.library;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Library {

    private List<Media> mediaList = new ArrayList<>();
    private List<User> users = new ArrayList<>();

    private final String BOOKS_FILE = "books.txt";
    private final String CDS_FILE = "cds.txt";
    private final String BORROWED_FILE = "borrowed.txt";
    private final String RETURNED_FILE = "returned.txt";
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    public Library() {
        loadBooks();
        loadCDs();
        loadBorrowedMedia();
    }

    public Library(boolean skipLoadingFiles) {
        if (!skipLoadingFiles) {
            loadBooks();
            loadCDs();
            loadBorrowedMedia();
        }
    }

    // ---------------- إدارة المستخدمين ----------------
    public void addUser(User user) {
        if (getUserByName(user.getName()) == null) {
            users.add(user);
        } else {
            System.out.println("User already exists.");
        }
    }

    public User getUserByName(String name) {
        for (User u : users) {
            if (u.getName().equalsIgnoreCase(name)) return u;
        }
        return null;
    }

    // ---------------- Load files ----------------
    private void loadBooks() {
        File f = new File(BOOKS_FILE);
        if (!f.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    mediaList.add(new Book(parts[0], parts[1], parts[2]));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading books: " + e.getMessage());
        }
    }

    private void loadCDs() {
        File f = new File(CDS_FILE);
        if (!f.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    mediaList.add(new CD(parts[0], parts[1]));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading CDs: " + e.getMessage());
        }
    }

    private void loadBorrowedMedia() {
        File f = new File(BORROWED_FILE);
        if (!f.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String type = parts[0];
                    String title = parts[1];
                    String borrower = parts[2];
                    LocalDate due = LocalDate.parse(parts[3], formatter);

                    for (Media m : mediaList) {
                        if (m.getTitle().equals(title) && m.getType().equals(type)) {
                            m.setBorrowed(true);
                            m.setBorrowerName(borrower);
                            m.setDueDate(due);
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading borrowed media: " + e.getMessage());
        }
    }

    // ---------------- Admin Actions ----------------
    public void addBook(Admin admin, Book book) {
        if(admin != null && admin.isLoggedIn()) {
            mediaList.add(book);
            saveBookToFile(book);
        } else {
            System.out.println("Permission denied: Admin must be logged in to add books.");
        }
    }

    public void addCD(Admin admin, CD cd) {
        if(admin != null && admin.isLoggedIn()) {
            mediaList.add(cd);
            saveCDToFile(cd);
        } else {
            System.out.println("Permission denied: Admin must be logged in to add CDs.");
        }
    }

    private void saveBookToFile(Book book) {
        try (FileWriter fw = new FileWriter(BOOKS_FILE, true)) {
            fw.write(book.getTitle() + "," + book.getAuthor() + "," + book.getIsbn() + "\n");
        } catch (IOException e) {
            System.out.println("Error writing book to file: " + e.getMessage());
        }
    }

    private void saveCDToFile(CD cd) {
        try (FileWriter fw = new FileWriter(CDS_FILE, true)) {
            fw.write(cd.getTitle() + "," + cd.getArtist() + "\n");
        } catch (IOException e) {
            System.out.println("Error writing CD to file: " + e.getMessage());
        }
    }

    // ---------------- User Actions ----------------
    public boolean borrowMedia(User user, Media media) {
        if (!user.canBorrow() || media.isBorrowed()) return false;

        media.borrow(user.getName());
        user.borrowMediaRecord(media);
        saveBorrowedMedia(media);
        return true;
    }

    public void returnMedia(User user, Media media) {
        if (!media.isBorrowed()) return;

        // حساب الغرامة على الأيام المتأخرة
        if (media.getDueDate() != null && LocalDate.now().isAfter(media.getDueDate())) {
            long daysLate = LocalDate.now().toEpochDay() - media.getDueDate().toEpochDay();
            user.addFine(daysLate * 1.0); // 1 وحدة نقدية لكل يوم
        }

        media.setBorrowed(false);
        media.setBorrowerName(null);
        media.setDueDate(null);
        user.returnMediaRecord(media);
        saveReturnedMedia(media);
    }

    // ---------------- Save Borrow/Return ----------------
    private void saveBorrowedMedia(Media media) {
        try (FileWriter fw = new FileWriter(BORROWED_FILE, true)) {
            fw.write(media.getType() + "," + media.getTitle() + "," + media.getBorrowerName() + "," +
                    media.getDueDate().format(formatter) + "\n");
        } catch (IOException e) {
            System.out.println("Error writing borrowed media: " + e.getMessage());
        }
    }

    private void saveReturnedMedia(Media media) {
        try (FileWriter fw = new FileWriter(RETURNED_FILE, true)) {
            fw.write(media.getType() + "," + media.getTitle() + "," + LocalDate.now().format(formatter) + "\n");
        } catch (IOException e) {
            System.out.println("Error writing returned media: " + e.getMessage());
        }
    }

    // ---------------- Search ----------------
    public List<Media> searchMedia(String keyword) {
        List<Media> results = new ArrayList<>();
        for (Media m : mediaList) {
            if (m.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                results.add(m);
            } else if (m instanceof Book) {
                Book b = (Book) m;
                if (b.getAuthor().toLowerCase().contains(keyword.toLowerCase()) ||
                        b.getIsbn().toLowerCase().contains(keyword.toLowerCase())) {
                    results.add(b);
                }
            } else if (m instanceof CD) {
                CD cd = (CD) m;
                if (cd.getArtist().toLowerCase().contains(keyword.toLowerCase())) {
                    results.add(cd);
                }
            }
        }
        return results;
    }

    // ---------------- Getters ----------------
    public List<Media> getMediaList() {
        return mediaList;
    }
}
