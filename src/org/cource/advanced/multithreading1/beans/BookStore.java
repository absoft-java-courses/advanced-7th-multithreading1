package org.cource.advanced.multithreading1.beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class BookStore {
    private volatile List<Book> books = new ArrayList<>();
    private final Object monitor = new Object();

    public void add(Book book) {
        synchronized (monitor) {
            var newBooks = new ArrayList<>(books);
            newBooks.add(book);
            books = newBooks;
        }
    }

    public synchronized void removeAllBooksOfAuthor(String author) {
        synchronized (monitor) {
            var newBooks = new ArrayList<>(books);
            newBooks.removeIf(book -> Objects.equals(book.author(), author));
            books = newBooks;
        }
    }

    public List<Book> getAllBooks() {
        return Collections.unmodifiableList(books);
    }
}
