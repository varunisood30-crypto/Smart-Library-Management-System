package com.library.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Records a book issued to a member, with due date and return tracking.
 */
public class Loan {
    public static final int LOAN_DAYS = 14;
    public static final double FINE_PER_DAY = 5.0;

    private String bookId;
    private String memberId;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate; // null while the book is still issued

    public Loan(String bookId, String memberId, LocalDate issueDate) {
        this.bookId = bookId;
        this.memberId = memberId;
        this.issueDate = issueDate;
        this.dueDate = issueDate.plusDays(LOAN_DAYS);
    }

    public String getBookId() { return bookId; }
    public String getMemberId() { return memberId; }
    public LocalDate getIssueDate() { return issueDate; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public boolean isActive() { return returnDate == null; }

    public void markReturned(LocalDate date) { this.returnDate = date; }

    /** Fine owed if returned on the given date (0 if on time or not returned yet counts today). */
    public double fineAsOf(LocalDate date) {
        LocalDate effective = returnDate != null ? returnDate : date;
        long overdue = ChronoUnit.DAYS.between(dueDate, effective);
        return overdue > 0 ? overdue * FINE_PER_DAY : 0.0;
    }

    public String toCsv() {
        return String.join(",", bookId, memberId, issueDate.toString(), dueDate.toString(),
                returnDate == null ? "" : returnDate.toString());
    }

    public static Loan fromCsv(String line) {
        String[] p = line.split(",", -1);
        Loan l = new Loan(p[0], p[1], LocalDate.parse(p[2]));
        // Reconstruct exact due date if it differs from default (kept simple: same rule)
        if (!p[4].isEmpty()) l.markReturned(LocalDate.parse(p[4]));
        return l;
    }

    @Override
    public String toString() {
        return String.format("Book %s -> Member %s | issued %s, due %s, %s",
                bookId, memberId, issueDate, dueDate,
                isActive() ? "ACTIVE (fine so far: Rs." + fineAsOf(LocalDate.now()) + ")"
                           : "returned " + returnDate + ", fine Rs." + fineAsOf(returnDate));
    }
}
