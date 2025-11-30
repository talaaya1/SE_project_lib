
/*

package com.library.ui;



import com.library.domain.Book;
import com.library.domain.User;
import com.library.service.Library;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LibraryUI extends JFrame {
    private Library library = new Library();
    private List<User> users = new ArrayList<>(); // NEW: لتخزين المستخدمين
    private JTextArea displayArea;
    private JTextField titleField, authorField, isbnField, searchField;
    private final String FILE_NAME = "books.txt";

    public LibraryUI() {
        setTitle("📚 Library Management System");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 245, 245));

        // 🌈 عنوان الصفحة
        JLabel header = new JLabel("📘 Library Control Panel", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 26));
        header.setForeground(new Color(60, 90, 150));
        header.setBorder(new EmptyBorder(10, 0, 10, 0));
        add(header, BorderLayout.NORTH);

        // 📗 لوحة الإدخال
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add / Delete Book"));
        inputPanel.setPreferredSize(new Dimension(300, 240));

        titleField = new JTextField();
        authorField = new JTextField();
        isbnField = new JTextField();

        inputPanel.add(new JLabel("Title:"));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("Author:"));
        inputPanel.add(authorField);
        inputPanel.add(new JLabel("ISBN:"));
        inputPanel.add(isbnField);

        JButton addButton = styledButton("➕ Add Book", new Color(76, 175, 80));
        JButton deleteButton = styledButton("🗑 Delete Book", new Color(244, 67, 54));

        inputPanel.add(addButton);
        inputPanel.add(deleteButton);

        // 🔹 Borrow / Return Panel
        JButton borrowButton = styledButton("📚 Borrow Book", new Color(33, 150, 243));
        JButton returnButton = styledButton("↩ Return Book", new Color(255, 152, 0));
        inputPanel.add(borrowButton);
        inputPanel.add(returnButton);

        // 🔍 لوحة البحث
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search / Show All"));

        searchField = new JTextField(20);
        JButton searchButton = styledButton("🔍 Search", new Color(33, 150, 243));
        JButton showAllButton = styledButton("📖 Show All", new Color(255, 152, 0));

        searchPanel.add(new JLabel("Keyword:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(showAllButton);

        // 🧩 منطقة النص لعرض الكتب
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        displayArea.setBackground(new Color(250, 250, 250));
        displayArea.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Books List"));

        // 🧠 تنظيم الصفحة
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.add(inputPanel, BorderLayout.NORTH);
        leftPanel.add(searchPanel, BorderLayout.CENTER);
        leftPanel.setBackground(new Color(245, 245, 245));
        leftPanel.setPreferredSize(new Dimension(320, 0));

        add(leftPanel, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);

        // ⚙️ تحميل الكتب عند البدء
        loadBooksFromFile();
        displayBooks(library.getBooks());

        // 🧭 الأحداث (Events)
        addButton.addActionListener(this::addBookAction);
        deleteButton.addActionListener(this::deleteBookAction);
        searchButton.addActionListener(this::searchBookAction);
        showAllButton.addActionListener(e -> displayBooks(library.getBooks()));
        borrowButton.addActionListener(this::borrowBookAction);
        returnButton.addActionListener(this::returnBookAction);

        setVisible(true);
    }

    // ================= 🔸 أزرار ملونة مخصصة =================
    private JButton styledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createLineBorder(color.darker(), 2));
        return button;
    }

    // ================= 🔸 الوظائف =================
    private void addBookAction(ActionEvent e) {
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        String isbn = isbnField.getText().trim();

        if (title.isEmpty() || author.isEmpty() || isbn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠ Please fill all fields!");
            return;
        }

        Book book = new Book(title, author, isbn);
        library.addBook(book);
        saveBooksToFile();
        JOptionPane.showMessageDialog(this, "✅ Book added successfully!");
        clearFields();
        displayBooks(library.getBooks());
    }

    private void deleteBookAction(ActionEvent e) {
        String isbn = isbnField.getText().trim();

        if (isbn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠ Please enter ISBN to delete!");
            return;
        }

        boolean removed = library.getBooks().removeIf(b -> b.getIsbn().equals(isbn));

        if (removed) {
            saveBooksToFile();
            JOptionPane.showMessageDialog(this, "🗑 Book deleted successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "❌ Book not found!");
        }

        displayBooks(library.getBooks());
        clearFields();
    }

    private void searchBookAction(ActionEvent e) {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠ Please enter a search keyword!");
            return;
        }

        List<Book> results = library.searchBook(keyword);
        if (results.isEmpty()) {
            displayArea.setText("❌ No books found for: " + keyword);
        } else {
            displayBooks(results);
        }
    }

    private void borrowBookAction(ActionEvent e) {
        String isbn = isbnField.getText().trim();
        String userName = JOptionPane.showInputDialog(this, "Enter your name:");
        if (userName == null || userName.isEmpty()) return;

        User user = getUserByName(userName);

        Book book = findBookByIsbn(isbn);
        if (book == null) {
            JOptionPane.showMessageDialog(this, "❌ Book not found!");
            return;
        }

        boolean success = library.borrowBook(user, book);
        if (success) {
            JOptionPane.showMessageDialog(this, "📚 Borrow successful! Due date: " + book.getDueDate());
        } else {
            String message = "Cannot borrow:";
            if (user.getFineBalance() > 0)
                message += " unpaid fines = " + user.getFineBalance();
            else if (user.hasOverdueBooks())
                message += " you have overdue books";
            else if (book.isBorrowed())
                message += " book already borrowed";
            JOptionPane.showMessageDialog(this, message);
        }

        displayBooks(library.getBooks());
    }

    private void returnBookAction(ActionEvent e) {
        String isbn = isbnField.getText().trim();
        String userName = JOptionPane.showInputDialog(this, "Enter your name:");
        if (userName == null || userName.isEmpty()) return;

        User user = getUserByName(userName);
        Book book = findBookByIsbn(isbn);
        if (book == null) {
            JOptionPane.showMessageDialog(this, "❌ Book not found!");
            return;
        }

        double fine = library.returnBook(user, book);
        String msg = "↩ Book returned successfully.";
        if (fine > 0) msg += "\n💰 You have been charged a fine: " + fine;
        JOptionPane.showMessageDialog(this, msg);

        displayBooks(library.getBooks());
    }

    private Book findBookByIsbn(String isbn) {
        for (Book b : library.getBooks()) {
            if (b.getIsbn().equals(isbn)) return b;
        }
        return null;
    }

    private User getUserByName(String name) {
        for (User u : users) {
            if (u.getName().equalsIgnoreCase(name)) return u;
        }
        User newUser = new User(name);
        users.add(newUser);
        return newUser;
    }

    private void displayBooks(List<Book> books) {
        displayArea.setText("");
        for (Book b : books) {
            String status = b.isBorrowed() ? "Borrowed by " + b.getBorrowerName() + ", due: " + b.getDueDate() : "Available";
            displayArea.append("📖 " + b.getTitle() + " | " + b.getAuthor() + " | ISBN: " + b.getIsbn() + " | " + status + "\n");
        }
    }

    private void clearFields() {
        titleField.setText("");
        authorField.setText("");
        isbnField.setText("");
        searchField.setText("");
    }

    // ================= 🔸 حفظ وتحميل الملفات =================

    private void saveBooksToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Book b : library.getBooks()) {
                bw.write(b.getTitle() + "," + b.getAuthor() + "," + b.getIsbn());
                bw.newLine();
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage());
        }
    }

    private void loadBooksFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    Book book = new Book(parts[0], parts[1], parts[2]);
                    library.addBook(book);
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error loading file: " + ex.getMessage());
        }
    }

    // ================= MAIN =================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LibraryUI::new);
    }
}

*/


/*
package com.library.ui;

import com.library.domain.*;
import com.library.service.Library;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LibraryUI extends JFrame {
    private Library library = new Library();
    private List<User> users = new ArrayList<>();
    private JTextArea displayArea;
    private JTextField titleField, authorField, isbnField, searchField;
    private JComboBox<String> mediaTypeBox;
    private final String BOOK_FILE = "books.txt";
    private final String CD_FILE = "cds.txt";

    public LibraryUI() {
        setTitle("📚 Library Management System - Media Extension");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 245, 245));

        // Header
        JLabel header = new JLabel("📘 Library Control Panel", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 26));
        header.setForeground(new Color(60, 90, 150));
        header.setBorder(new EmptyBorder(10, 0, 10, 0));
        add(header, BorderLayout.NORTH);

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add / Borrow / Return Media"));
        inputPanel.setPreferredSize(new Dimension(320, 250));

        titleField = new JTextField();
        authorField = new JTextField();
        isbnField = new JTextField();
        mediaTypeBox = new JComboBox<>(new String[]{"Book", "CD"});

        inputPanel.add(new JLabel("Media Type:"));
        inputPanel.add(mediaTypeBox);
        inputPanel.add(new JLabel("Title:"));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("Author/Artist:"));
        inputPanel.add(authorField);
        inputPanel.add(new JLabel("ISBN (Book only):"));
        inputPanel.add(isbnField);

        JButton addButton = styledButton("➕ Add Media", new Color(76, 175, 80));
        JButton borrowButton = styledButton("📚 Borrow Media", new Color(33, 150, 243));
        JButton returnButton = styledButton("↩ Return Media", new Color(255, 152, 0));

        inputPanel.add(addButton);
        inputPanel.add(borrowButton);
        inputPanel.add(new JLabel(""));
        inputPanel.add(returnButton);

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search / Show All"));

        searchField = new JTextField(20);
        JButton searchButton = styledButton("🔍 Search", new Color(33, 150, 243));
        JButton showAllButton = styledButton("📖 Show All", new Color(255, 152, 0));

        searchPanel.add(new JLabel("Keyword:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(showAllButton);

        // Display Area
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        displayArea.setBackground(new Color(250, 250, 250));
        displayArea.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Media List"));

        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.add(inputPanel, BorderLayout.NORTH);
        leftPanel.add(searchPanel, BorderLayout.CENTER);
        leftPanel.setBackground(new Color(245, 245, 245));
        leftPanel.setPreferredSize(new Dimension(320, 0));

        add(leftPanel, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);

        // Load media from files
        loadMediaFromFiles();
        displayMedia(library.getMediaList());

        // Event Actions
        addButton.addActionListener(this::addMediaAction);
        borrowButton.addActionListener(this::borrowMediaAction);
        returnButton.addActionListener(this::returnMediaAction);
        searchButton.addActionListener(this::searchMediaAction);
        showAllButton.addActionListener(e -> displayMedia(library.getMediaList()));

        setVisible(true);
    }

    // ================= Styled Button =================
    private JButton styledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createLineBorder(color.darker(), 2));
        return button;
    }

    // ================= Media Actions =================
    private void addMediaAction(ActionEvent e) {
        String type = (String) mediaTypeBox.getSelectedItem();
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        String isbn = isbnField.getText().trim();

        if (title.isEmpty() || author.isEmpty() || (type.equals("Book") && isbn.isEmpty())) {
            JOptionPane.showMessageDialog(this, "⚠ Please fill all required fields!");
            return;
        }

        // ✅ تحقق من تكرار ISBN إذا كان كتاب
        if (type.equals("Book")) {
            for (Media m : library.getMediaList()) {
                if (m instanceof Book) {
                    Book b = (Book) m;
                    if (b.getIsbn().equals(isbn)) {
                        JOptionPane.showMessageDialog(this, "❌ Cannot add: Book with ISBN already exists!");
                        return;
                    }
                }
            }
        }

        Media media;
        if (type.equals("Book")) {
            media = new Book(title, author, isbn);
        } else {
            media = new CD(title, author);
        }

        library.addMedia(media);
        saveMediaToFile(media);
        JOptionPane.showMessageDialog(this, "✅ " + type + " added successfully!");
        clearFields();
        displayMedia(library.getMediaList());
    }

    private void borrowMediaAction(ActionEvent e) {
        String type = (String) mediaTypeBox.getSelectedItem();
        String title = titleField.getText().trim();
        String userName = JOptionPane.showInputDialog(this, "Enter your name:");
        if (userName == null || userName.isEmpty()) return;

        User user = getUserByName(userName);
        Media media = findMediaByTitle(title, type);

        if (media == null) {
            JOptionPane.showMessageDialog(this, "❌ Media not found!");
            return;
        }

        boolean success = library.borrowMedia(user, media);
        if (success) {
            JOptionPane.showMessageDialog(this, "📚 Borrow successful! Due date: " + media.getDueDate());
        } else {
            String message = "Cannot borrow: ";
            if (user.getFineBalance() > 0) message += "unpaid fines = " + user.getFineBalance();
            else if (user.hasOverdueBooks()) message += "you have overdue books";
            else if (media.isBorrowed()) message += "already borrowed";
            JOptionPane.showMessageDialog(this, message);
        }

        displayMedia(library.getMediaList());
    }

    private void returnMediaAction(ActionEvent e) {
        String type = (String) mediaTypeBox.getSelectedItem();
        String title = titleField.getText().trim();
        String userName = JOptionPane.showInputDialog(this, "Enter your name:");
        if (userName == null || userName.isEmpty()) return;

        User user = getUserByName(userName);
        Media media = findMediaByTitle(title, type);

        if (media == null) {
            JOptionPane.showMessageDialog(this, "❌ Media not found!");
            return;
        }

        library.returnMedia(user, media);
        JOptionPane.showMessageDialog(this, "↩ Media returned successfully.");
        displayMedia(library.getMediaList());
    }

    private void searchMediaAction(ActionEvent e) {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠ Please enter a search keyword!");
            return;
        }

        List<Media> results = new ArrayList<>();
        for (Media m : library.getMediaList()) {
            if (m.getTitle().toLowerCase().contains(keyword.toLowerCase())) results.add(m);
        }

        if (results.isEmpty()) displayArea.setText("❌ No media found for: " + keyword);
        else displayMedia(results);
    }

    // ================= Helpers =================
    private Media findMediaByTitle(String title, String type) {
        for (Media m : library.getMediaList()) {
            if (m.getTitle().equalsIgnoreCase(title) && m.getType().equals(type)) return m;
        }
        return null;
    }

    private User getUserByName(String name) {
        for (User u : users) if (u.getName().equalsIgnoreCase(name)) return u;
        User newUser = new User(name);
        users.add(newUser);
        return newUser;
    }

    private void displayMedia(List<Media> list) {
        displayArea.setText("");
        for (Media m : list) {
            String status = m.isBorrowed() ? "Borrowed by " + m.getBorrowerName() + ", due: " + m.getDueDate() : "Available";

            if (m instanceof Book) {
                Book b = (Book) m;
                displayArea.append("📖 [Book] " + b.getTitle() + " | Author: " + b.getAuthor() + " | ISBN: " + b.getIsbn() + " | " + status + "\n");
            } else if (m instanceof CD) {
                CD c = (CD) m;
                displayArea.append("📀 [CD] " + c.getTitle() + " | Artist: " + c.getArtist() + " | " + status + "\n");
            }
        }
    }

    private void clearFields() {
        titleField.setText("");
        authorField.setText("");
        isbnField.setText("");
        searchField.setText("");
    }

    // ================= File Save/Load =================
    private void saveMediaToFile(Media media) {
        String fileName = media instanceof Book ? BOOK_FILE : CD_FILE;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))) {
            if (media instanceof Book) {
                Book b = (Book) media;
                bw.write(b.getTitle() + "," + b.getAuthor() + "," + b.getIsbn());
            } else if (media instanceof CD) {
                CD c = (CD) media;
                bw.write(c.getTitle() + "," + c.getArtist());
            }
            bw.newLine();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage());
        }
    }

    private void loadMediaFromFiles() {
        // Load Books
        File bFile = new File(BOOK_FILE);
        if (bFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(bFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 3) library.addMedia(new Book(parts[0], parts[1], parts[2]));
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error loading books: " + ex.getMessage());
            }
        }

        // Load CDs
        File cFile = new File(CD_FILE);
        if (cFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(cFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 2) library.addMedia(new CD(parts[0], parts[1]));
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error loading CDs: " + ex.getMessage());
            }
        }
    }

    // ================= MAIN =================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LibraryUI::new);
    }
}
*/


/*
package com.library.ui;

import com.library.domain.*;
import com.library.service.Library;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LibraryUI extends JFrame {
    private Library library = new Library();
    private List<User> users = new ArrayList<>();
    private JTextArea displayArea;
    private JTextField titleField, authorField, isbnField, searchField;
    private JComboBox<String> mediaTypeBox;
    private final String BOOK_FILE = "books.txt";
    private final String CD_FILE = "cds.txt";

    public LibraryUI() {
        setTitle("📚 Library Management System - Media Extension");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 245, 245));

        // Header
        JLabel header = new JLabel("📘 Library Control Panel", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 26));
        header.setForeground(new Color(60, 90, 150));
        header.setBorder(new EmptyBorder(10, 0, 10, 0));
        add(header, BorderLayout.NORTH);

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add / Borrow / Return Media"));
        inputPanel.setPreferredSize(new Dimension(320, 250));

        titleField = new JTextField();
        authorField = new JTextField();
        isbnField = new JTextField();
        mediaTypeBox = new JComboBox<>(new String[]{"Book", "CD"});

        inputPanel.add(new JLabel("Media Type:"));
        inputPanel.add(mediaTypeBox);
        inputPanel.add(new JLabel("Title:"));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("Author/Artist:"));
        inputPanel.add(authorField);
        inputPanel.add(new JLabel("ISBN (Book only):"));
        inputPanel.add(isbnField);

        JButton addButton = styledButton("➕ Add Media", new Color(76, 175, 80));
        JButton borrowButton = styledButton("📚 Borrow Media", new Color(33, 150, 243));
        JButton returnButton = styledButton("↩ Return Media", new Color(255, 152, 0));

        inputPanel.add(addButton);
        inputPanel.add(borrowButton);
        inputPanel.add(new JLabel(""));
        inputPanel.add(returnButton);

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search / Show All"));

        searchField = new JTextField(20);
        JButton searchButton = styledButton("🔍 Search", new Color(33, 150, 243));
        JButton showAllButton = styledButton("📖 Show All", new Color(255, 152, 0));

        searchPanel.add(new JLabel("Keyword:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(showAllButton);

        // Display Area
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        displayArea.setBackground(new Color(250, 250, 250));
        displayArea.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Media List"));

        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.add(inputPanel, BorderLayout.NORTH);
        leftPanel.add(searchPanel, BorderLayout.CENTER);
        leftPanel.setBackground(new Color(245, 245, 245));
        leftPanel.setPreferredSize(new Dimension(320, 0));

        add(leftPanel, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);

        // Load media from files
        loadMediaFromFiles();
        displayMedia(library.getMediaList());

        // Event Actions
        addButton.addActionListener(this::addMediaAction);
        borrowButton.addActionListener(this::borrowMediaAction);
        returnButton.addActionListener(this::returnMediaAction);
        searchButton.addActionListener(this::searchMediaAction);
        showAllButton.addActionListener(e -> displayMedia(library.getMediaList()));

        setVisible(true);
    }

    // ================= Styled Button =================
    private JButton styledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createLineBorder(color.darker(), 2));
        return button;
    }

    // ================= Media Actions =================
    private void addMediaAction(ActionEvent e) {
        String type = (String) mediaTypeBox.getSelectedItem();
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        String isbn = isbnField.getText().trim();

        if (title.isEmpty() || author.isEmpty() || (type.equals("Book") && isbn.isEmpty())) {
            JOptionPane.showMessageDialog(this, "⚠ Please fill all required fields!");
            return;
        }

        // ✅ تحقق من تكرار ISBN إذا كان كتاب
        if (type.equals("Book")) {
            for (Media m : library.getMediaList()) {
                if (m instanceof Book) {
                    Book b = (Book) m;
                    if (b.getIsbn().equals(isbn)) {
                        JOptionPane.showMessageDialog(this, "❌ Cannot add: Book with ISBN already exists!");
                        return;
                    }
                }
            }
        }

        Media media;
        if (type.equals("Book")) {
            media = new Book(title, author, isbn);
        } else {
            media = new CD(title, author);
        }

        library.addMedia(media);
        saveMediaToFile(media);
        JOptionPane.showMessageDialog(this, "✅ " + type + " added successfully!");
        clearFields();
        displayMedia(library.getMediaList());
    }

    private void borrowMediaAction(ActionEvent e) {
        String type = (String) mediaTypeBox.getSelectedItem();
        String title = titleField.getText().trim();
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠ Please enter the media title!");
            return;
        }

        String userName = JOptionPane.showInputDialog(this, "Enter your name:");
        if (userName == null || userName.isEmpty()) return;

        User user = getUserByName(userName);
        Media media = findMediaByTitle(title, type);

        if (media == null) {
            JOptionPane.showMessageDialog(this, "❌ Media not found!");
            return;
        }

        // ✅ التحقق من الغرامات والمتأخرات قبل السماح بالاستعارة
        if (user.getTotalFines() > 0 || user.hasOverdueMedia()) {
            JOptionPane.showMessageDialog(this,
                    "Cannot borrow: you have overdue media or unpaid fines.\n" +
                            "Total fines: " + user.getTotalFines());
            return;
        }

        boolean success = library.borrowMedia(user, media);
        if (success) {
            JOptionPane.showMessageDialog(this, "📚 Borrow successful! Due date: " + media.getDueDate());
        } else {
            String message = "Cannot borrow: ";
            if (media.isBorrowed()) message += "already borrowed";
            JOptionPane.showMessageDialog(this, message);
        }

        displayMedia(library.getMediaList());
    }

    private void returnMediaAction(ActionEvent e) {
        String type = (String) mediaTypeBox.getSelectedItem();
        String title = titleField.getText().trim();
        String userName = JOptionPane.showInputDialog(this, "Enter your name:");
        if (userName == null || userName.isEmpty()) return;

        User user = getUserByName(userName);
        Media media = findMediaByTitle(title, type);

        if (media == null) {
            JOptionPane.showMessageDialog(this, "❌ Media not found!");
            return;
        }

        library.returnMedia(user, media);
        JOptionPane.showMessageDialog(this, "↩ Media returned successfully.");
        displayMedia(library.getMediaList());
    }

    private void searchMediaAction(ActionEvent e) {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠ Please enter a search keyword!");
            return;
        }

        List<Media> results = new ArrayList<>();
        for (Media m : library.getMediaList()) {
            if (m.getTitle().toLowerCase().contains(keyword.toLowerCase())) results.add(m);
        }

        if (results.isEmpty()) displayArea.setText("❌ No media found for: " + keyword);
        else displayMedia(results);
    }

    // ================= Helpers =================
    private Media findMediaByTitle(String title, String type) {
        for (Media m : library.getMediaList()) {
            if (m.getTitle().equalsIgnoreCase(title) && m.getType().equals(type)) return m;
        }
        return null;
    }

    private User getUserByName(String name) {
        for (User u : users) if (u.getName().equalsIgnoreCase(name)) return u;
        User newUser = new User(name);
        users.add(newUser);
        return newUser;
    }

    private void displayMedia(List<Media> list) {
        displayArea.setText("");
        for (Media m : list) {
            String status = m.isBorrowed() ? "Borrowed by " + m.getBorrowerName() + ", due: " + m.getDueDate() : "Available";

            if (m instanceof Book) {
                Book b = (Book) m;
                displayArea.append("📖 [Book] " + b.getTitle() + " | Author: " + b.getAuthor() + " | ISBN: " + b.getIsbn() + " | " + status + "\n");
            } else if (m instanceof CD) {
                CD c = (CD) m;
                displayArea.append("📀 [CD] " + c.getTitle() + " | Artist: " + c.getArtist() + " | " + status + "\n");
            }
        }
    }

    private void clearFields() {
        titleField.setText("");
        authorField.setText("");
        isbnField.setText("");
        searchField.setText("");
    }

    // ================= File Save/Load =================
    private void saveMediaToFile(Media media) {
        String fileName = media instanceof Book ? BOOK_FILE : CD_FILE;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))) {
            if (media instanceof Book) {
                Book b = (Book) media;
                bw.write(b.getTitle() + "," + b.getAuthor() + "," + b.getIsbn());
            } else if (media instanceof CD) {
                CD c = (CD) media;
                bw.write(c.getTitle() + "," + c.getArtist());
            }
            bw.newLine();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage());
        }
    }

    private void loadMediaFromFiles() {
        // Load Books
        File bFile = new File(BOOK_FILE);
        if (bFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(bFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 3) library.addMedia(new Book(parts[0], parts[1], parts[2]));
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error loading books: " + ex.getMessage());
            }
        }

        // Load CDs
        File cFile = new File(CD_FILE);
        if (cFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(cFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 2) library.addMedia(new CD(parts[0], parts[1]));
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error loading CDs: " + ex.getMessage());
            }
        }
    }

    // ================= MAIN =================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LibraryUI::new);
    }
}

*/



/*
package com.library.ui;

import com.library.*;
import com.library.Library;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LibraryUI extends JFrame {
    private Library library = new Library();
    private List<User> users = new ArrayList<>();
    private JTextArea displayArea;
    private JTextField titleField, authorField, isbnField, searchField;
    private JComboBox<String> mediaTypeBox;
    private final String BOOK_FILE = "books.txt";
    private final String CD_FILE = "cds.txt";
    private boolean isAdmin;

    public LibraryUI(boolean isAdmin) {
        this.isAdmin = isAdmin;

        setTitle("📚 Library Management System");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 245, 245));

        // Header
        JLabel header = new JLabel("📘 Library Control Panel", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 26));
        header.setForeground(new Color(60, 90, 150));
        header.setBorder(new EmptyBorder(10, 0, 10, 0));
        add(header, BorderLayout.NORTH);

        // Left Panel: Inputs & Buttons
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setBackground(new Color(245, 245, 245));
        leftPanel.setPreferredSize(new Dimension(350, 0));

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add / Borrow / Return Media"));

        titleField = new JTextField();
        authorField = new JTextField();
        isbnField = new JTextField();
        mediaTypeBox = new JComboBox<>(new String[]{"Book", "CD"});

        inputPanel.add(new JLabel("Media Type:"));
        inputPanel.add(mediaTypeBox);
        inputPanel.add(new JLabel("Title:"));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("Author/Artist:"));
        inputPanel.add(authorField);
        inputPanel.add(new JLabel("ISBN (Book only):"));
        inputPanel.add(isbnField);

        JButton addButton = styledButton("➕ Add Media", new Color(76, 175, 80));
        JButton borrowButton = styledButton("📚 Borrow Media", new Color(33, 150, 243));
        JButton returnButton = styledButton("↩ Return Media", new Color(255, 152, 0));
        JButton reportButton = styledButton("💰 My Fines Report", new Color(200, 60, 60));
        JButton logoutButton = styledButton("🔒 Logout", new Color(128, 128, 128));

        addButton.setEnabled(isAdmin);

        inputPanel.add(addButton);
        inputPanel.add(borrowButton);
        inputPanel.add(returnButton);
        inputPanel.add(reportButton);
        inputPanel.add(new JLabel(""));
        inputPanel.add(logoutButton);

        leftPanel.add(inputPanel, BorderLayout.NORTH);

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search / Show All"));

        searchField = new JTextField(20);
        JButton searchButton = styledButton("🔍 Search", new Color(33, 150, 243));
        JButton showAllButton = styledButton("📖 Show All", new Color(255, 152, 0));

        searchPanel.add(new JLabel("Keyword:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(showAllButton);

        leftPanel.add(searchPanel, BorderLayout.CENTER);

        add(leftPanel, BorderLayout.WEST);

        // Display Area
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        displayArea.setBackground(new Color(250, 250, 250));
        displayArea.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Media List"));
        add(scrollPane, BorderLayout.CENTER);

        // Load media
        loadMediaFromFiles();
        displayMedia(library.getMediaList());

        // Button Actions
        addButton.addActionListener(this::addMediaAction);
        borrowButton.addActionListener(this::borrowMediaAction);
        returnButton.addActionListener(this::returnMediaAction);
        reportButton.addActionListener(this::showFinesReport);
        searchButton.addActionListener(this::searchMediaAction);
        showAllButton.addActionListener(e -> displayMedia(library.getMediaList()));
        logoutButton.addActionListener(e -> {
            new LoginUI(); // رجوع لشاشة الدخول
            dispose();     // اغلاق هذه الشاشة
        });

        setVisible(true);
    }

    private JButton styledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(color.darker(), 2));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    // ====== Actions ======
    private void addMediaAction(ActionEvent e) {
        if (!isAdmin) {
            JOptionPane.showMessageDialog(this, "❌ Only admin can add media!");
            return;
        }
        String type = (String) mediaTypeBox.getSelectedItem();
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        String isbn = isbnField.getText().trim();

        if (title.isEmpty() || author.isEmpty() || (type.equals("Book") && isbn.isEmpty())) {
            JOptionPane.showMessageDialog(this, "⚠ Please fill all required fields!");
            return;
        }

        if (type.equals("Book")) {
            for (Media m : library.getMediaList()) {
                if (m instanceof Book && ((Book) m).getIsbn().equals(isbn)) {
                    JOptionPane.showMessageDialog(this, "❌ Book with this ISBN already exists!");
                    return;
                }
            }
        }

        Media media = type.equals("Book") ? new Book(title, author, isbn) : new CD(title, author);
        library.addMedia(media);
        saveMediaToFile(media);
        JOptionPane.showMessageDialog(this, "✅ " + type + " added successfully!");
        clearFields();
        displayMedia(library.getMediaList());
    }

    private void borrowMediaAction(ActionEvent e) {
        String type = (String) mediaTypeBox.getSelectedItem();
        String title = titleField.getText().trim();
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠ Please enter media title!");
            return;
        }
        String userName = JOptionPane.showInputDialog(this, "Enter your name:");
        if (userName == null || userName.isEmpty()) return;

        User user = getUserByName(userName);
        Media media = findMediaByTitle(title, type);
        if (media == null) {
            JOptionPane.showMessageDialog(this, "❌ Media not found!");
            return;
        }

        if (user.getTotalFines() > 0 || user.hasOverdueMedia()) {
            JOptionPane.showMessageDialog(this,
                    "Cannot borrow: you have overdue media or unpaid fines.\nTotal fines: " + user.getTotalFines());
            return;
        }

        boolean success = library.borrowMedia(user, media);
        if (success) {
            JOptionPane.showMessageDialog(this, "📚 Borrow successful! Due date: " + media.getDueDate());
        } else {
            JOptionPane.showMessageDialog(this, "❌ Cannot borrow: already borrowed!");
        }

        displayMedia(library.getMediaList());
    }

    private void returnMediaAction(ActionEvent e) {
        String type = (String) mediaTypeBox.getSelectedItem();
        String title = titleField.getText().trim();
        String userName = JOptionPane.showInputDialog(this, "Enter your name:");
        if (userName == null || userName.isEmpty()) return;

        User user = getUserByName(userName);
        Media media = findMediaByTitle(title, type);
        if (media == null) {
            JOptionPane.showMessageDialog(this, "❌ Media not found!");
            return;
        }

        library.returnMedia(user, media);
        JOptionPane.showMessageDialog(this, "↩ Media returned successfully.");
        displayMedia(library.getMediaList());
    }

    private void showFinesReport(ActionEvent e) {
        String userName = JOptionPane.showInputDialog(this, "Enter your name:");
        if (userName == null || userName.isEmpty()) return;

        User user = getUserByName(userName);
        double totalFines = user.getTotalFines();

        StringBuilder sb = new StringBuilder("📊 Fine Report for " + userName + ":\n");
        for (Media m : user.getBorrowedMedia()) {
            if (m.isBorrowed()) {
                sb.append("• ").append(m.getTitle()).append(" (").append(m.getType()).append(")")
                        .append(" - Due: ").append(m.getDueDate());
                if (m.getDueDate() != null && m.getDueDate().isBefore(java.time.LocalDate.now()))
                    sb.append(" ❌ Overdue\n");
                else
                    sb.append(" ✅ On Time\n");
            }
        }
        sb.append("\n💰 Total Fines: ").append(totalFines);

        JOptionPane.showMessageDialog(this, sb.toString());
    }

    private void searchMediaAction(ActionEvent e) {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠ Please enter a search keyword!");
            return;
        }

        List<Media> results = new ArrayList<>();
        for (Media m : library.getMediaList()) {
            if (m.getTitle().toLowerCase().contains(keyword.toLowerCase())) results.add(m);
        }

        if (results.isEmpty()) displayArea.setText("❌ No media found for: " + keyword);
        else displayMedia(results);
    }

    // ====== Helpers ======
    private Media findMediaByTitle(String title, String type) {
        for (Media m : library.getMediaList()) {
            if (m.getTitle().equalsIgnoreCase(title) && m.getType().equals(type)) return m;
        }
        return null;
    }

    private User getUserByName(String name) {
        for (User u : users) if (u.getName().equalsIgnoreCase(name)) return u;
        User newUser = new User(name);
        users.add(newUser);
        return newUser;
    }

    private void displayMedia(List<Media> list) {
        StringBuilder sb = new StringBuilder();
        for (Media m : list) sb.append(m).append("\n");
        displayArea.setText(sb.toString());
    }

    private void clearFields() {
        titleField.setText("");
        authorField.setText("");
        isbnField.setText("");
    }

    private void saveMediaToFile(Media media) {
        String file = media instanceof Book ? BOOK_FILE : CD_FILE;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(media.toString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadMediaFromFiles() {
        loadFile(BOOK_FILE, "Book");
        loadFile(CD_FILE, "CD");
    }

    private void loadFile(String fileName, String type) {
        File f = new File(fileName);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (type.equals("Book")) {
                    String[] parts = line.split(",");
                    if (parts.length >= 3) library.addMedia(new Book(parts[0], parts[1], parts[2]));
                } else {
                    String[] parts = line.split(",");
                    if (parts.length >= 2) library.addMedia(new CD(parts[0], parts[1]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

*/




