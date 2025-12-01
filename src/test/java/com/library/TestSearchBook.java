package com.library;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TestSearchBook {

    @Test
    public void testSearch(){
        searchBook library = new searchBook();

        // نضيف كتب للمكتبة
        library.addBook(new Book("Java Programming", "James Gosling", "12345"));
        library.addBook(new Book("Python Basics", "Guido van Rossum", "67890"));
        library.addBook(new Book("C++ Guide", "Bjarne Stroustrup", "11111"));

        // نبحث عن كتاب حسب العنوان
        ArrayList<Book> result1 = library.search("Java Programming");
        assertEquals(1, result1.size());

        // نبحث حسب المؤلف
        ArrayList<Book> result2 = library.search("Guido van Rossum");
        assertEquals(1, result2.size());

        // نبحث حسب رقم ISBN
        ArrayList<Book> result3 = library.search("11111");
        assertEquals(1, result3.size());

        // نبحث عن شيء مش موجود
        ArrayList<Book> result4 = library.search("HTML");
        assertEquals(0, result4.size());
    }
}
