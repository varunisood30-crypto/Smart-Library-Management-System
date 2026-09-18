package com.library.exception;

/** Base class for all library-specific errors. */
public class LibraryException extends Exception {
    public LibraryException(String message) { super(message); }
}
