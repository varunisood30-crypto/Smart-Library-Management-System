package com.library;

import com.library.exception.*;
import com.library.model.*;
import com.library.service.Library;

import java.nio.file.*;
import java.util.*;

/**
 * Console entry point. Run: java -cp out com.library.Main
 */
public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static Library library = new Library(Paths.get("data"));

    public static void main(String[] args) {
        try {
            library.load();
        } catch (Exception e) {
            System.out.println("Could not load saved data (starting fresh): " + e.getMessage());
        }
        if (library.getBooks().isEmpty()) seed();

        System.out.println("==============================================");
        System.out.println("   SMART LIBRARY MANAGEMENT SYSTEM  (SLMS)");
        System.out.println("==============================================");

        boolean running = true;
        while (running) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Add a book");
            System.out.println("2. Register a member");
            System.out.println("3. View all books");
            System.out.println("4. View all members");
            System.out.println("5. Search books by title");
            System.out.println("6. Issue a book");
            System.out.println("7. Return a book");
            System.out.println("8. View active loans");
            System.out.println("9. View loans of a member");
            System.out.println("0. Save & Exit");
            System.out.print("Choose: ");
            String choice = sc.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> addBook();
                    case "2" -> addMember();
                    case "3" -> list(library.getBooks());
                    case "4" -> list(library.getMembers());
                    case "5" -> searchBooks();
                    case "6" -> issueBook();
                    case "7" -> returnBook();
                    case "8" -> list(library.activeLoans());
                    case "9" -> memberLoans();
                    case "0" -> {
                        library.save();
                        System.out.println("Data saved. Goodbye!");
                        running = false;
                    }
                    default -> System.out.println("Invalid option, try again.");
                }
            } catch (LibraryException e) {
                System.out.println("ERROR: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Something went wrong: " + e.getMessage());
            }
        }
    }

    private static void seed() {
        library.addBook(new Book("B001", "Clean Code", "Robert C. Martin", 3));
        library.addBook(new Book("B002", "Effective Java", "Joshua Bloch", 2));
        library.addBook(new Book("B003", "Head First Java", "Kathy Sierra", 4));
        library.addMember(new Member("M001", "Asha Verma", "asha@example.com"));
        library.addMember(new Member("M002", "Rohan Mehta", "rohan@example.com"));
    }

    private static void addBook() {
        System.out.print("Book ID: "); String id = sc.nextLine().trim();
        System.out.print("Title: "); String title = sc.nextLine().trim();
        System.out.print("Author: "); String author = sc.nextLine().trim();
        System.out.print("Number of copies: "); int copies = Integer.parseInt(sc.nextLine().trim());
        library.addBook(new Book(id, title, author, copies));
        System.out.println("Book added!");
    }

    private static void addMember() {
        System.out.print("Member ID: "); String id = sc.nextLine().trim();
        System.out.print("Name: "); String name = sc.nextLine().trim();
        System.out.print("Email: "); String email = sc.nextLine().trim();
        library.addMember(new Member(id, name, email));
        System.out.println("Member registered!");
    }

    private static void searchBooks() {
        System.out.print("Search keyword: ");
        list(library.searchByTitle(sc.nextLine().trim()));
    }

    private static void issueBook() throws LibraryException {
        System.out.print("Book ID: "); String bid = sc.nextLine().trim();
        System.out.print("Member ID: "); String mid = sc.nextLine().trim();
        Loan loan = library.issueBook(bid, mid);
        System.out.println("Issued! Due date: " + loan.getDueDate());
    }

    private static void returnBook() throws LibraryException {
        System.out.print("Book ID: "); String bid = sc.nextLine().trim();
        System.out.print("Member ID: "); String mid = sc.nextLine().trim();
        Loan loan = library.returnBook(bid, mid);
        System.out.println("Returned. Fine: Rs." + loan.fineAsOf(loan.getReturnDate()));
    }

    private static void memberLoans() throws MemberNotFoundException {
        System.out.print("Member ID: "); String mid = sc.nextLine().trim();
        library.findMember(mid);
        list(library.loansOf(mid));
    }

    private static <T> void list(List<T> items) {
        if (items.isEmpty()) { System.out.println("(none)"); return; }
        for (T t : items) System.out.println(t);
    }
}
