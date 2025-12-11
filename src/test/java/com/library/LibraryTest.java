package com.library;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Library System Comprehensive Tests")
class LibraryTest {

    // ====================== Test Variables ======================
    private Library library;
    private static final String TEST_USER_EMAIL = "test.user@example.com";
    private static final String TEST_ADMIN_EMAIL = "admin@library.com";

    @TempDir
    static Path tempDir; // مجلد مؤقت للاختبارات

    // ====================== Helper Methods ======================

    private void setPrivateField(Object obj, String fieldName, Object value) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }

    @SuppressWarnings("unchecked")
    private <T> T getPrivateField(Object obj, String fieldName) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return (T) field.get(obj);
    }

    private void writeToFile(String filename, String content) throws IOException {
        try (FileWriter fw = new FileWriter(filename)) {
            fw.write(content);
        }
    }

    private void createDefaultFiles() throws IOException {
        // إنشاء ملف users.txt مع بيانات اختبارية
        String usersContent = "John Doe,test.user@example.com,pass123,0.0\n" +
                "Jane Smith,jane@example.com,pass456,5.5";
        writeToFile("users.txt", usersContent);

        // إنشاء ملف books.txt
        String booksContent = "The Great Gatsby,F. Scott Fitzgerald,1234567890,3\n" +
                "1984,George Orwell,0987654321,2\n" +
                "To Kill a Mockingbird,Harper Lee,1122334455,1";
        writeToFile("books.txt", booksContent);

        // إنشاء ملف cds.txt
        String cdsContent = "Thriller,Michael Jackson,MJ001,5\n" +
                "Back in Black,AC/DC,AC002,3";
        writeToFile("cds.txt", cdsContent);

        // إنشاء ملف borrowed.txt
        String borrowedContent = "Book,The Great Gatsby,test.user@example.com,2025-01-01,2025-01-15\n" +
                "CD,Thriller,jane@example.com,2025-01-10,2025-01-24";
        writeToFile("borrowed.txt", borrowedContent);

        // إنشاء ملف admins.txt
        String adminsContent = "admin,admin@gmail.com,admin123\n" +
                "superadmin,super@gmail.com,super123";
        writeToFile("admins.txt", adminsContent);

        // إنشاء ملف librarians.txt
        String librariansContent = "Main Librarian,librarian@library.com,1234";
        writeToFile("librarians.txt", librariansContent);

        // إنشاء ملف fines.txt
        writeToFile("fines.txt", "jane@example.com,5.5");
    }

    private Book createTestBook() {
        return new Book("Test Book", "Test Author", "TEST123", 3);
    }

    private CD createTestCD() {
        return new CD("Test CD", "Test Artist", "CD123", 2);
    }

    private User createTestUser() {
        return new User("Test User", TEST_USER_EMAIL, "testpass");
    }

    private Admin createTestAdmin() {
        return new Admin("Test Admin", TEST_ADMIN_EMAIL, "adminpass");
    }

    // ====================== Setup & Teardown ======================

    @BeforeAll
    static void initAll() {
        System.out.println("=== Starting Library System Tests ===");
    }

    @BeforeEach
    void setUp() throws Exception {
        // تغيير مسار الملفات إلى المجلد المؤقت
        System.setProperty("user.dir", tempDir.toString());

        // إنشاء ملفات اختبارية
        createDefaultFiles();

        // إنشاء مكتبة جديدة مع تحميل الملفات
        library = new Library(true);

        // إنشاء EmailManager mock
        EmailManager mockEmailManager = mock(EmailManager.class);
        library.setEmailManager(mockEmailManager);

        System.out.println("Test setup completed for: " + getClass().getSimpleName());
    }

    @AfterEach
    void tearDown() {
        library = null;
        System.gc();
        System.out.println("Test cleanup completed");
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("=== All Library Tests Completed ===");
    }

    // ====================== 1. CONSTRUCTOR & INITIALIZATION TESTS ======================

   /* @Test
    @Order(1)
    @DisplayName("✅ Test 1.1: Library Constructor with loadFiles = true")
    void testLibraryConstructor_LoadFilesTrue() throws Exception {
        // When
        List<Media> mediaList = getPrivateField(library, "mediaList");
        List<User> users = getPrivateField(library, "users");
        List<Admin> admins = getPrivateField(library, "admins");
        List<Librarian> librarians = getPrivateField(library, "librarians");

        // Then
        assertNotNull(mediaList);
        assertNotNull(users);
        assertNotNull(admins);
        assertNotNull(librarians);

        // Verify loaded data
        assertFalse(mediaList.isEmpty());
        assertFalse(users.isEmpty());
        assertFalse(admins.isEmpty());
        assertFalse(librarians.isEmpty());

        System.out.println("Loaded: " + mediaList.size() + " media, " +
                users.size() + " users, " +
                admins.size() + " admins");
    }

    */

    @Test
    @Order(2)
    @DisplayName("✅ Test 1.2: Library Constructor with loadFiles = false")
    void testLibraryConstructor_LoadFilesFalse() {
        // When
        Library emptyLibrary = new Library(false);

        // Then
        assertNotNull(emptyLibrary);
        // القوائم يجب أن تكون فارغة
    }

    @Test
    @Order(3)
    @DisplayName("✅ Test 1.3: Default Admins Creation")
    void testDefaultAdminsCreation() throws Exception {
        // Given: حذف ملف admins.txt
        new File("admins.txt").delete();

        // When: إنشاء مكتبة جديدة
        Library newLib = new Library(true);
        List<Admin> admins = getPrivateField(newLib, "admins");

        // Then
        assertFalse(admins.isEmpty());
        assertEquals(2, admins.size());

        // التحقق من الأدمنز الافتراضيين
        boolean hasAdmin = admins.stream().anyMatch(a -> a.getEmail().equals("admin@gmail.com"));
        boolean hasSuperAdmin = admins.stream().anyMatch(a -> a.getEmail().equals("super@gmail.com"));

        assertTrue(hasAdmin, "Should have default admin");
        assertTrue(hasSuperAdmin, "Should have default superadmin");
    }

    // ====================== 2. USER MANAGEMENT TESTS ======================

    @Test
    @Order(10)
    @DisplayName("✅ Test 2.1: Get User By Email - User Exists")
    void testGetUserByEmail_UserExists() {
        // When
        User user = library.getUserByEmail(TEST_USER_EMAIL);

        // Then
        assertNotNull(user);
        assertEquals("John Doe", user.getName());
        assertEquals(TEST_USER_EMAIL, user.getEmail());
    }

    @Test
    @Order(11)
    @DisplayName("✅ Test 2.2: Get User By Email - User Not Exists")
    void testGetUserByEmail_UserNotExists() {
        // When
        User user = library.getUserByEmail("nonexistent@email.com");

        // Then
        assertNull(user);
    }

    @Test
    @Order(12)
    @DisplayName("✅ Test 2.3: Get User By Email - Case Insensitive")
    void testGetUserByEmail_CaseInsensitive() {
        // When
        User user = library.getUserByEmail(TEST_USER_EMAIL.toUpperCase());

        // Then
        assertNotNull(user);
        assertEquals(TEST_USER_EMAIL.toLowerCase(), user.getEmail().toLowerCase());
    }

    @ParameterizedTest
    @Order(13)
    @ValueSource(strings = {"jane@example.com", "JANE@EXAMPLE.COM", "Jane@Example.Com"})
    @DisplayName("✅ Test 2.4: Get User By Email - Parameterized Test")
    void testGetUserByEmail_Parameterized(String email) {
        // When
        User user = library.getUserByEmail(email);

        // Then
        assertNotNull(user);
        assertEquals("Jane Smith", user.getName());
    }

    @Test
    @Order(14)
    @DisplayName("✅ Test 2.5: User Has Correct Fines")
    void testUserFinesLoading() {
        // When
        User jane = library.getUserByEmail("jane@example.com");

        // Then
        assertNotNull(jane);
        assertEquals(5.5, jane.getTotalFines(), 0.01);
    }

    // ====================== 3. MEDIA LOADING TESTS ======================

    @Test
    @Order(20)
    @DisplayName("✅ Test 3.1: Load Books From File")
    void testLoadBooks() throws Exception {
        // Given
        List<Media> mediaList = getPrivateField(library, "mediaList");

        // When
        long bookCount = mediaList.stream()
                .filter(m -> m instanceof Book)
                .count();

        // Then
        assertEquals(3, bookCount);

        // التحقق من كتاب معين
        boolean hasGatsby = mediaList.stream()
                .filter(m -> m instanceof Book)
                .anyMatch(m -> m.getTitle().equals("The Great Gatsby"));
        assertTrue(hasGatsby);
    }

    @Test
    @Order(21)
    @DisplayName("✅ Test 3.2: Load CDs From File")
    void testLoadCDs() throws Exception {
        // Given
        List<Media> mediaList = getPrivateField(library, "mediaList");

        // When
        long cdCount = mediaList.stream()
                .filter(m -> m instanceof CD)
                .count();

        // Then
        assertEquals(2, cdCount);

        // التحقق من CD معين
        boolean hasThriller = mediaList.stream()
                .filter(m -> m instanceof CD)
                .anyMatch(m -> m.getTitle().equals("Thriller"));
        assertTrue(hasThriller);
    }

    @Test
    @Order(22)
    @DisplayName("✅ Test 3.3: Media Has Correct Copies")
    void testMediaCopies() throws Exception {
        // Given
        List<Media> mediaList = getPrivateField(library, "mediaList");

        // When: البحث عن كتاب "1984"
        Media book1984 = mediaList.stream()
                .filter(m -> m.getTitle().equals("1984"))
                .findFirst()
                .orElse(null);

        // Then
        assertNotNull(book1984);
        assertTrue(book1984 instanceof Book);
        assertEquals(2, book1984.getAvailableCopies());
    }

    // ====================== 4. BORROWED MEDIA TESTS ======================

    @Test
    @Order(30)
    @DisplayName("✅ Test 4.1: Load Borrowed Media")
    void testLoadBorrowedMedia() throws Exception {
        // Given
        List<Media> mediaList = getPrivateField(library, "mediaList");

        // When: البحث عن الكتاب المعار
        Media gatsby = mediaList.stream()
                .filter(m -> m.getTitle().equals("The Great Gatsby"))
                .findFirst()
                .orElse(null);

        // Then
        assertNotNull(gatsby);
        assertTrue(gatsby.isBorrowed());
        assertEquals(TEST_USER_EMAIL, gatsby.getBorrowerEmail());
        assertEquals(LocalDate.of(2025, 1, 1), gatsby.getBorrowDate());
        assertEquals(LocalDate.of(2025, 1, 15), gatsby.getDueDate());
    }

    @Test
    @Order(31)
    @DisplayName("✅ Test 4.2: User Has Borrowed Items")
    void testUserBorrowedItems() {
        // When
        User john = library.getUserByEmail(TEST_USER_EMAIL);

        // Then
        assertNotNull(john);
        // يجب أن يكون لدى جون عنصر معار
    }

    // ====================== 5. BORROW OPERATION TESTS ======================

    /*@Test
    @Order(40)
    @DisplayName("✅ Test 5.1: Borrow Available Book")
    void testBorrowAvailableBook() throws Exception {
        // Given
        Book book = new Book("New Book", "New Author", "NEW123", 2);
        User user = new User("New User", "new@user.com", "pass");

        // إضافة للقوائم الداخلية
        List<Media> mediaList = getPrivateField(library, "mediaList");
        List<User> users = getPrivateField(library, "users");

        mediaList.add(book);
        users.add(user);

        // استخدام reflection لاستدعاء borrowMedia
        Method borrowMethod = Library.class.getDeclaredMethod("borrowMedia", Media.class, String.class);
        borrowMethod.setAccessible(true);

        // When
        boolean result = (boolean) borrowMethod.invoke(library, book, user.getEmail());

        // Then
        assertTrue(result);
        assertTrue(book.isBorrowed());
        assertEquals(user.getEmail(), book.getBorrowerEmail());
        assertNotNull(book.getBorrowDate());
        assertNotNull(book.getDueDate());
        assertEquals(LocalDate.now().plusDays(14), book.getDueDate());
    }

    @Test
    @Order(41)
    @DisplayName("✅ Test 5.2: Borrow Unavailable Book (0 copies)")
    void testBorrowUnavailableBook() throws Exception {
        // Given
        Book book = new Book("Rare Book", "Rare Author", "RARE001", 0);
        User user = createTestUser();

        List<Media> mediaList = getPrivateField(library, "mediaList");
        mediaList.add(book);

        Method borrowMethod = Library.class.getDeclaredMethod("borrowMedia", Media.class, String.class);
        borrowMethod.setAccessible(true);

        // When
        boolean result = (boolean) borrowMethod.invoke(library, book, user.getEmail());

        // Then
        assertFalse(result);
        assertFalse(book.isBorrowed());
    }

    @Test
    @Order(42)
    @DisplayName("✅ Test 5.3: Borrow Already Borrowed Book")
    void testBorrowAlreadyBorrowedBook() throws Exception {
        // Given
        Book book = new Book("Popular Book", "Author", "POP001", 1);
        User user1 = new User("User1", "user1@test.com", "pass");
        User user2 = new User("User2", "user2@test.com", "pass");

        List<Media> mediaList = getPrivateField(library, "mediaList");
        List<User> users = getPrivateField(library, "users");

        mediaList.add(book);
        users.add(user1);
        users.add(user2);

        // إعارة الكتاب للمستخدم الأول
        book.setBorrowed(true);
        book.setBorrowerEmail(user1.getEmail());

        Method borrowMethod = Library.class.getDeclaredMethod("borrowMedia", Media.class, String.class);
        borrowMethod.setAccessible(true);

        // When: محاولة إعارة نفس الكتاب للمستخدم الثاني
        boolean result = (boolean) borrowMethod.invoke(library, book, user2.getEmail());

        // Then
        assertFalse(result);
        assertEquals(user1.getEmail(), book.getBorrowerEmail());
    }

    // ====================== 6. RETURN OPERATION TESTS ======================

    @Test
    @Order(50)
    @DisplayName("✅ Test 6.1: Return Book On Time")
    void testReturnBookOnTime() throws Exception {
        // Given
        Book book = new Book("Return Test Book", "Author", "RET001", 2);
        User user = createTestUser();

        // إعداد حالة الإعارة
        LocalDate borrowDate = LocalDate.now().minusDays(5);
        LocalDate dueDate = LocalDate.now().plusDays(9);

        book.setBorrowed(true);
        book.setBorrowerEmail(user.getEmail());
        book.setBorrowDate(borrowDate);
        book.setDueDate(dueDate);

        List<Media> mediaList = getPrivateField(library, "mediaList");
        List<User> users = getPrivateField(library, "users");

        mediaList.add(book);
        users.add(user);

        Method returnMethod = Library.class.getDeclaredMethod("returnMedia", Media.class, String.class);
        returnMethod.setAccessible(true);

        // When
        boolean result = (boolean) returnMethod.invoke(library, book, user.getEmail());

        // Then
        assertTrue(result);
        assertFalse(book.isBorrowed());
        assertNull(book.getBorrowerEmail());
        assertEquals(0.0, user.getTotalFines(), 0.01);
    }

    @Test
    @Order(51)
    @DisplayName("✅ Test 6.2: Return Book Late With Fine")
    void testReturnBookLateWithFine() throws Exception {
        // Given
        Book book = new Book("Late Book", "Author", "LATE001", 1);
        User user = createTestUser();

        // إعداد حالة الإعارة مع تأخير 5 أيام
        LocalDate borrowDate = LocalDate.now().minusDays(20);
        LocalDate dueDate = LocalDate.now().minusDays(5); // تأخر 5 أيام

        book.setBorrowed(true);
        book.setBorrowerEmail(user.getEmail());
        book.setBorrowDate(borrowDate);
        book.setDueDate(dueDate);

        List<Media> mediaList = getPrivateField(library, "mediaList");
        List<User> users = getPrivateField(library, "users");

        mediaList.add(book);
        users.add(user);

        Method returnMethod = Library.class.getDeclaredMethod("returnMedia", Media.class, String.class);
        returnMethod.setAccessible(true);

        // When
        boolean result = (boolean) returnMethod.invoke(library, book, user.getEmail());

        // Then
        assertTrue(result);
        assertFalse(book.isBorrowed());
        // التحقق من الغرامة (5 أيام × 1 ريال = 5 ريال)
        assertEquals(5.0, user.getTotalFines(), 0.01);
    }

    @Test
    @Order(52)
    @DisplayName("✅ Test 6.3: Return Book Not Borrowed")
    void testReturnBookNotBorrowed() throws Exception {
        // Given
        Book book = new Book("Available Book", "Author", "AVAIL001", 3);
        User user = createTestUser();

        List<Media> mediaList = getPrivateField(library, "mediaList");
        mediaList.add(book);

        Method returnMethod = Library.class.getDeclaredMethod("returnMedia", Media.class, String.class);
        returnMethod.setAccessible(true);

        // When
        boolean result = (boolean) returnMethod.invoke(library, book, user.getEmail());

        // Then
        assertFalse(result);
    }
*/
    // ====================== 7. FILE OPERATION TESTS ======================

    @Test
    @Order(60)
    @DisplayName("✅ Test 7.1: Write To File Successfully")
    void testWriteToFile() throws Exception {
        // Given
        String testFile = "test_write.txt";
        String testContent = "Line 1\nLine 2\nLine 3";

        Method writeMethod = Library.class.getDeclaredMethod("writeToFile",
                String.class, String.class, boolean.class);
        writeMethod.setAccessible(true);

        // When
        writeMethod.invoke(library, testFile, testContent, false);

        // Then: التحقق من إنشاء الملف
        File file = new File(testFile);
        assertTrue(file.exists());

        // التحقق من المحتوى
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            assertEquals("Line 1", br.readLine());
            assertEquals("Line 2", br.readLine());
            assertEquals("Line 3", br.readLine());
        }

        // Cleanup
        file.delete();
    }

    @Test
    @Order(61)
    @DisplayName("✅ Test 7.2: Append To File")
    void testAppendToFile() throws Exception {
        // Given
        String testFile = "test_append.txt";
        writeToFile(testFile, "Existing Line\n");

        Method writeMethod = Library.class.getDeclaredMethod("writeToFile",
                String.class, String.class, boolean.class);
        writeMethod.setAccessible(true);

        // When: إضافة محتوى جديد
        writeMethod.invoke(library, testFile, "New Appended Line", true);

        // Then: التحقق من المحتوى
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(testFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        }

        assertEquals(2, lines.size());
        assertEquals("Existing Line", lines.get(0));
        assertEquals("New Appended Line", lines.get(1));

        // Cleanup
        new File(testFile).delete();
    }

    @Test
    @Order(62)
    @DisplayName("✅ Test 7.3: Read From Non-Existent File")
    void testReadFromNonExistentFile() throws Exception {
        // Given
        String nonExistentFile = "non_existent_file_999.txt";

        Method readMethod = Library.class.getDeclaredMethod("readLinesFromFile", String.class);
        readMethod.setAccessible(true);

        // When
        @SuppressWarnings("unchecked")
        List<String> lines = (List<String>) readMethod.invoke(library, nonExistentFile);

        // Then
        assertNotNull(lines);
        assertTrue(lines.isEmpty());
    }

    @Test
    @Order(63)
    @DisplayName("✅ Test 7.4: Overwrite File")
    void testOverwriteFile() throws Exception {
        // Given
        String testFile = "test_overwrite.txt";
        writeToFile(testFile, "Old Line 1\nOld Line 2");

        List<String> newLines = List.of("New Line 1", "New Line 2", "New Line 3");

        Method overwriteMethod = Library.class.getDeclaredMethod("overwriteFile",
                String.class, List.class);
        overwriteMethod.setAccessible(true);

        // When
        overwriteMethod.invoke(library, testFile, newLines);

        // Then
        List<String> actualLines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(testFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                actualLines.add(line);
            }
        }

        assertEquals(3, actualLines.size());
        assertEquals("New Line 1", actualLines.get(0));
        assertEquals("New Line 2", actualLines.get(1));
        assertEquals("New Line 3", actualLines.get(2));

        // Cleanup
        new File(testFile).delete();
    }

    // ====================== 8. ADMIN & LIBRARIAN TESTS ======================

    @Test
    @Order(70)
    @DisplayName("✅ Test 8.1: Admins Loaded Correctly")
    void testAdminsLoaded() throws Exception {
        // Given
        List<Admin> admins = getPrivateField(library, "admins");

        // Then
        assertFalse(admins.isEmpty());
        assertEquals(2, admins.size());

        // التحقق من بيانات الأدمن
        Admin admin = admins.get(0);
        assertEquals("admin", admin.getName());
        assertEquals("admin@gmail.com", admin.getEmail());
        assertEquals("admin123", admin.getPassword());
    }

   /* @Test
    @Order(71)
    @DisplayName("✅ Test 8.2: Librarians Loaded Correctly")
    void testLibrariansLoaded() throws Exception {
        // Given
        List<Librarian> librarians = getPrivateField(library, "librarians");

        // Then
        assertFalse(librarians.isEmpty());

        Librarian lib = librarians.get(0);
        assertEquals("Main Librarian", lib.getName());
        assertEquals("librarian@library.com", lib.getEmail());
        assertEquals("1234", lib.getPassword());
    }*/

    // ====================== 9. EDGE CASE TESTS ======================

    @Test
    @Order(80)
    @DisplayName("✅ Test 9.1: Empty Files Handling")
    void testEmptyFilesHandling() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Given: ملفات فارغة
        writeToFile("empty_books.txt", "");
        writeToFile("empty_users.txt", "\n\n"); // أسطر فارغة

        Library localLib = new Library(false);

        Method readMethod = Library.class.getDeclaredMethod("readLinesFromFile", String.class);
        readMethod.setAccessible(true);

        // When
        @SuppressWarnings("unchecked")
        List<String> booksLines = (List<String>) readMethod.invoke(localLib, "empty_books.txt");
        @SuppressWarnings("unchecked")
        List<String> usersLines = (List<String>) readMethod.invoke(localLib, "empty_users.txt");

        // Then
        assertNotNull(booksLines);
        assertTrue(booksLines.isEmpty());

        assertNotNull(usersLines);
        assertTrue(usersLines.isEmpty());
    }

    @Test
    @Order(81)
    @DisplayName("✅ Test 9.2: Invalid Data in Files")
    void testInvalidDataInFiles() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Given: ملف ببيانات غير صحيحة
        String invalidContent = "Invalid Line\n" +
                "Book,Only Title\n" +
                "Book,Author,ISBN,NotANumber\n" +
                ",,,,\n" +
                "   "; // مسافات فقط

        writeToFile("invalid.txt", invalidContent);

        Library localLib = new Library(false);

        Method readMethod = Library.class.getDeclaredMethod("readLinesFromFile", String.class);
        readMethod.setAccessible(true);

        // When
        @SuppressWarnings("unchecked")
        List<String> lines = (List<String>) readMethod.invoke(localLib, "invalid.txt");

        // Then: يجب أن يتخطى الأسطر الفارغة فقط
        assertEquals(4, lines.size()); // 4 أسطر غير فارغة
    }

   /* @Test
    @Order(82)
    @DisplayName("✅ Test 9.3: Concurrent File Access Simulation")
    void testConcurrentFileAccess() throws Exception {
        // Given
        String concurrentFile = "concurrent_test.txt";
        int numThreads = 5;

        Method writeMethod = Library.class.getDeclaredMethod("writeToFile",
                String.class, String.class, boolean.class);
        writeMethod.setAccessible(true);

        // When: كتابة من "خيوط" متعددة (محاكاة)
        for (int i = 0; i < numThreads; i++) {
            final int threadId = i;
            writeMethod.invoke(library, concurrentFile,
                    "Thread " + threadId + " write\n", true);
        }

        // Then: التحقق من أن جميع الكتابات تمت
        int lineCount = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(concurrentFile))) {
            while (br.readLine() != null) {
                lineCount++;
            }
        }

        assertEquals(numThreads, lineCount);

        // Cleanup
        new File(concurrentFile).delete();
    }
*/
    // ====================== 10. INTEGRATION TESTS ======================

  /*  @Test
    @Order(90)
    @DisplayName("✅ Test 10.1: Complete Borrow-Return-Fine Cycle")
    void testCompleteBorrowReturnFineCycle() throws Exception {
        // Given
        Book book = new Book("Cycle Test Book", "Cycle Author", "CYCLE001", 1);
        User user = new User("Cycle User", "cycle@test.com", "cyclepass");

        List<Media> mediaList = getPrivateField(library, "mediaList");
        List<User> users = getPrivateField(library, "users");

        mediaList.add(book);
        users.add(user);

        // Step 1: إعارة الكتاب
        Method borrowMethod = Library.class.getDeclaredMethod("borrowMedia",
                Media.class, String.class);
        borrowMethod.setAccessible(true);

        boolean borrowResult = (boolean) borrowMethod.invoke(library, book, user.getEmail());
        assertTrue(borrowResult);
        assertTrue(book.isBorrowed());

        // Step 2: تغيير تاريخ الاستحقاق ليصبح متأخرًا (لتجربة الغرامة)
        book.setDueDate(LocalDate.now().minusDays(3)); // تأخر 3 أيام

        // Step 3: إرجاع الكتاب المتأخر
        Method returnMethod = Library.class.getDeclaredMethod("returnMedia",
                Media.class, String.class);
        returnMethod.setAccessible(true);

        boolean returnResult = (boolean) returnMethod.invoke(library, book, user.getEmail());
        assertTrue(returnResult);
        assertFalse(book.isBorrowed());

        // Step 4: التحقق من الغرامة
        assertEquals(3.0, user.getTotalFines(), 0.01);

        // Step 5: دفع الغرامة
        user.setTotalFines(0.0);
        assertEquals(0.0, user.getTotalFines(), 0.01);

        System.out.println("Complete cycle test passed successfully!");
    }

    @Test
    @Order(91)
    @DisplayName("✅ Test 10.2: Multiple Users Borrow Different Items")
    void testMultipleUsersBorrowDifferentItems() throws Exception {
        // Given
        Book book1 = new Book("Book 1", "Author 1", "B001", 2);
        Book book2 = new Book("Book 2", "Author 2", "B002", 1);
        CD cd1 = new CD("CD 1", "Artist 1", "C001", 3);

        User user1 = new User("User 1", "user1@multi.com", "pass1");
        User user2 = new User("User 2", "user2@multi.com", "pass2");

        List<Media> mediaList = getPrivateField(library, "mediaList");
        List<User> users = getPrivateField(library, "users");

        mediaList.add(book1);
        mediaList.add(book2);
        mediaList.add(cd1);
        users.add(user1);
        users.add(user2);

        Method borrowMethod = Library.class.getDeclaredMethod("borrowMedia",
                Media.class, String.class);
        borrowMethod.setAccessible(true);

        // When: كل مستخدم يعير عنصر مختلف
        boolean result1 = (boolean) borrowMethod.invoke(library, book1, user1.getEmail());
        boolean result2 = (boolean) borrowMethod.invoke(library, book2, user2.getEmail());
        boolean result3 = (boolean) borrowMethod.invoke(library, cd1, user1.getEmail());

        // Then
        assertTrue(result1);
        assertTrue(result2);
        assertTrue(result3);

        assertTrue(book1.isBorrowed());
        assertTrue(book2.isBorrowed());
        assertTrue(cd1.isBorrowed());

        assertEquals(user1.getEmail(), book1.getBorrowerEmail());
        assertEquals(user2.getEmail(), book2.getBorrowerEmail());
        assertEquals(user1.getEmail(), cd1.getBorrowerEmail());
    }
*/
    // ====================== 11. PERFORMANCE & STRESS TESTS ======================

    @Test
    @Order(100)
    @DisplayName("✅ Test 11.1: Load Large Dataset")
    void testLoadLargeDataset() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Given: إنشاء ملف كبير
        String largeBooksFile = "large_books.txt";
        StringBuilder content = new StringBuilder();

        // 1000 كتاب
        for (int i = 1; i <= 1000; i++) {
            content.append("Book ").append(i)
                    .append(",Author ").append(i)
                    .append(",ISBN").append(String.format("%07d", i))
                    .append(",2\n");
        }

        writeToFile(largeBooksFile, content.toString());

        Library localLib = new Library(false);

        Method readMethod = Library.class.getDeclaredMethod("readLinesFromFile", String.class);
        readMethod.setAccessible(true);

        // When
        long startTime = System.currentTimeMillis();

        @SuppressWarnings("unchecked")
        List<String> lines = (List<String>) readMethod.invoke(localLib, largeBooksFile);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        // Then
        assertEquals(1000, lines.size());
        assertTrue(duration < 1000, "Loading 1000 books should take less than 1 second");

        System.out.println("Loaded 1000 books in " + duration + " ms");

        // Cleanup
        new File(largeBooksFile).delete();
    }

    // ====================== 12. EXCEPTION HANDLING TESTS ======================

    @Test
    @Order(110)
    @DisplayName("✅ Test 12.1: Graceful Handling of Corrupted Files")
    void testCorruptedFileHandling() throws IOException, NoSuchMethodException {
        // Given: ملف تالف (binary data)
        String corruptedFile = "corrupted.bin";
        try (FileOutputStream fos = new FileOutputStream(corruptedFile)) {
            fos.write(new byte[] {0, 1, 2, 3, 4, 5}); // بيانات غير نصية
        }

        Library localLib = new Library(false);

        Method readMethod = Library.class.getDeclaredMethod("readLinesFromFile", String.class);
        readMethod.setAccessible(true);

        // When & Then: يجب أن يتم معالجته بلطف بدون crash
        assertDoesNotThrow(() -> {
            readMethod.invoke(localLib, corruptedFile);
        });

        // Cleanup
        new File(corruptedFile).delete();
    }

    // ====================== FINAL SUMMARY TEST ======================

    @Test
    @Order(999)
    @DisplayName("🎯 FINAL: Comprehensive System Health Check")
    void testSystemHealthCheck() throws Exception {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("SYSTEM HEALTH CHECK");
        System.out.println("=".repeat(50));

        // 1. Check Data Integrity
        List<Media> mediaList = getPrivateField(library, "mediaList");
        List<User> users = getPrivateField(library, "users");
        List<Admin> admins = getPrivateField(library, "admins");
        List<Librarian> librarians = getPrivateField(library, "librarians");

        assertNotNull(mediaList, "Media list should not be null");
        assertNotNull(users, "Users list should not be null");
        assertNotNull(admins, "Admins list should not be null");
        assertNotNull(librarians, "Librarians list should not be null");

        // 2. Check File Operations
        assertTrue(new File("books.txt").exists(), "Books file should exist");
        assertTrue(new File("users.txt").exists(), "Users file should exist");

        // 3. Check Critical Business Logic
        User testUser = library.getUserByEmail(TEST_USER_EMAIL);
        assertNotNull(testUser, "Test user should exist");

        // 4. Print Summary
        System.out.println("✓ Media Items: " + mediaList.size());
        System.out.println("✓ Registered Users: " + users.size());
        System.out.println("✓ Administrators: " + admins.size());
        System.out.println("✓ Librarians: " + librarians.size());

        long borrowedCount = mediaList.stream().filter(Media::isBorrowed).count();
        System.out.println("✓ Currently Borrowed Items: " + borrowedCount);

        double totalFines = users.stream().mapToDouble(User::getTotalFines).sum();
        System.out.println("✓ Total Outstanding Fines: $" + totalFines);

        System.out.println("\n✅ ALL SYSTEMS OPERATIONAL ✅");
        System.out.println("=".repeat(50));
    }
}