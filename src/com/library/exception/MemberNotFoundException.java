package com.library.exception;

public class MemberNotFoundException extends LibraryException {
    public MemberNotFoundException(String id) { super("No member found with ID: " + id); }
}
