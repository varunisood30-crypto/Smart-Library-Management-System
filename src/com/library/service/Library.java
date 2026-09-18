package com.library.service;

import com.library.exception.*;
import com.library.model.*;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

/**
 * Core business logic: catalog, members, issuing/returning books, fines,
 * and CSV persistence.
 */
public class Library {
    private final List<Book> books = new ArrayList<>();
    private final List<Member> members = new ArrayList<>();
    private final List<Loan> loans = new ArrayList<>();

    private final Path dataDir;

    public Library(Path dataDir) {
        this.dataDir = dataDir;
    }

    // ---------- Catalog ----------
    public void addBook(Book b) { books.add(b); }

    public List<Book> getBooks() { return books; }

    public Book findBook(String id) throws BookNotFoundException {
        return books.stream().filter(b -> b.getId().equalsIgnoreCase(id)).findFirst()
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    public List<Book> searchByTitle(String keyword) {
        List<Book> out = new ArrayList<>();
        for (Book b : books)
            if (b.getTitle().toLowerCase().contains(keyword.toLowerCase())) out.add(b);
        return out;
    }

    // ---------- Members ----------
    public void addMember(Member m) { members.add(m); }

    public List<Member> getMembers() { return members; }

    public Member findMember(String id) throws MemberNotFoundException {
        return members.stream().filter(m -> m.getId().equalsIgnoreCase(id)).findFirst()
                .orElseThrow(() -> new MemberNotFoundException(id));
    }

    // ---------- Loans ----------
    public Loan issueBook(String bookId, String memberId)
            throws BookNotFoundException, MemberNotFoundException, BookUnavailableException {
        Book b = findBook(bookId);
        findMember(memberId);
        if (b.getAvailableCopies() <= 0) throw new BookUnavailableException(bookId);
        for (Loan l : loans)
            if (l.isActive() && l.getMemberId().equalsIgnoreCase(memberId)
                    && l.getBookId().equalsIgnoreCase(bookId))
                throw new BookUnavailableException(bookId + " (already issued to this member)");
        b.borrowCopy();
        Loan loan = new Loan(b.getId(), memberId, LocalDate.now());
        loans.add(loan);
        return loan;
    }

    public Loan returnBook(String bookId, String memberId) throws LibraryException {
        for (Loan l : loans) {
            if (l.isActive() && l.getBookId().equalsIgnoreCase(bookId)
                    && l.getMemberId().equalsIgnoreCase(memberId)) {
                l.markReturned(LocalDate.now());
                findBook(bookId).returnCopy();
                return l;
            }
        }
        throw new LibraryException("No active loan found for book " + bookId + " / member " + memberId);
    }

    public List<Loan> activeLoans() {
        List<Loan> out = new ArrayList<>();
        for (Loan l : loans) if (l.isActive()) out.add(l);
        return out;
    }

    public List<Loan> loansOf(String memberId) {
        List<Loan> out = new ArrayList<>();
        for (Loan l : loans) if (l.getMemberId().equalsIgnoreCase(memberId)) out.add(l);
        return out;
    }

    // ---------- Persistence ----------
    public void load() throws IOException {
        books.clear(); members.clear(); loans.clear();
        loadLines(dataDir.resolve("books.csv"), Book::fromCsv, books);
        loadLines(dataDir.resolve("members.csv"), Member::fromCsv, members);
        loadLines(dataDir.resolve("loans.csv"), Loan::fromCsv, loans);
    }

    public void save() throws IOException {
        Files.createDirectories(dataDir);
        writeLines(dataDir.resolve("books.csv"), new ArrayList<String>());
        List<String> bl = new ArrayList<>();
        for (Book b : books) bl.add(b.toCsv());
        writeLines(dataDir.resolve("books.csv"), bl);
        List<String> ml = new ArrayList<>();
        for (Member m : members) ml.add(m.toCsv());
        writeLines(dataDir.resolve("members.csv"), ml);
        List<String> ll = new ArrayList<>();
        for (Loan l : loans) ll.add(l.toCsv());
        writeLines(dataDir.resolve("loans.csv"), ll);
    }

    private interface Parser<T> { T parse(String line); }

    private <T> void loadLines(Path p, Parser<T> parser, List<T> into) throws IOException {
        if (!Files.exists(p)) return;
        for (String line : Files.readAllLines(p))
            if (!line.isBlank()) into.add(parser.parse(line));
    }

    private void writeLines(Path p, List<String> lines) throws IOException {
        Files.write(p, lines);
    }

    // Small helper because Java has no Parser<T> functional interface inline
    private static <T> T parseCsv(Parser<T> p, String line) { return p.parse(line); }
}
