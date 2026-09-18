package com.library.exception;

public class BookUnavailableException extends LibraryException {
    public BookUnavailableException(String id) { super("Book " + id + " has no copies available right now."); }
}
