package com.library.exception;

public class BookNotFoundException extends LibraryException {
    public BookNotFoundException(String id) { super("No book found with ID: " + id); }
}
