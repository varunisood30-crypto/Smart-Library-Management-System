# Project Report — Smart Library Management System (SLMS)

**Submitted by:** Varuni Sood
**Course:** Java Programming
**Date:** October 2026

---

## 1. Introduction

The Smart Library Management System (SLMS) is a console-based application
written in Core Java. It automates the day-to-day work of a small library:
maintaining a book catalog, registering members, issuing and returning
books, and charging overdue fines. The system persists its data in CSV
files, so it needs no database server or external libraries.

### 1.1 Objectives

- Replace manual, paper-based library records with a dependable program.
- Apply core Java concepts (OOP, Collections, Exceptions, File I/O) in a
  realistic, layered design.
- Provide a clear, menu-driven console interface usable by non-technical
  staff.

### 1.2 Scope

The current version supports a single library branch with three data
sets: books, members, and loans. Future scope is listed in Section 8.

## 2. Problem Statement

In most school/college libraries, book issue records are kept in registers.
This leads to lost records, no automatic fine calculation, no quick
search, and no easy way to know which books are overdue. SLMS solves
these problems with a small Java program that keeps accurate records and
computes fines automatically.

## 3. System Analysis

### 3.1 Functional Requirements

| # | Requirement | Implemented |
|---|---|---|
| FR1 | Add a book with ID, title, author, copies | Yes (menu 1) |
| FR2 | Register a member with ID, name, email | Yes (menu 2) |
| FR3 | List books / members | Yes (menu 3, 4) |
| FR4 | Search books by title | Yes (menu 5) |
| FR5 | Issue a book (check availability, due date +14 days) | Yes (menu 6) |
| FR6 | Return a book (compute overdue fine Rs.5/day) | Yes (menu 7) |
| FR7 | View active loans | Yes (menu 8) |
| FR8 | View all loans of a member | Yes (menu 9) |
| FR9 | Persist data between runs | Yes (CSV files) |

### 3.2 Non-Functional Requirements

- **Usability:** numbered menu, plain-language prompts, readable tables.
- **Reliability:** typed exceptions (`BookNotFoundException`,
  `MemberNotFoundException`, `BookUnavailableException`) prevent crashes.
- **Maintainability:** package-by-layer structure (`model`, `service`,
  `exception`, `ui` entry in `Main`).
- **Portability:** pure Core Java, runs on any OS with a JDK.

### 3.3 Use Cases

1. Librarian adds a new book to the catalog.
2. Librarian registers a new student as a member.
3. Member borrows a book; system issues it and sets a due date.
4. Member returns a book; system computes the fine.
5. Librarian views all overdue/active loans.

## 4. System Design

### 4.1 Architecture (layers)

```
Console UI (Main.java)
      |
      v
Service layer (Library.java)   <-- business rules, fine policy, persistence
      |
      v
Model layer (Book, Member, Loan)
      |
      v
Storage (data/books.csv, members.csv, loans.csv)
```

### 4.2 Class Description

| Class | Responsibility |
|---|---|
| `Book` | Catalog entry; tracks total and available copies |
| `Member` | A registered library member |
| `Loan` | One issue/return record; computes due date and fine |
| `Library` | All business logic + CSV load/save |
| `Main` | Menu loop; input validation; error messages |
| `LibraryException` (+ subclasses) | Typed, user-friendly errors |

### 4.3 Fine Calculation

```
fine = max(0, (returnDate - dueDate) in days) * Rs.5
dueDate = issueDate + 14 days
```

Example: a book due on 2026-10-19 returned on 2026-10-24 incurs
5 days × Rs.5 = **Rs.25**.

## 5. Implementation

- Language: Java 17+ (developed on OpenJDK 21)
- Compilation: `javac` directly — no Maven/Gradle needed
- Persistence format (example):

```
books.csv
B001,Clean Code,Robert C. Martin,3,2
```

```
loans.csv
B001,M001,2026-10-05,2026-10-19,2026-10-24
```

- A duplicate active loan for the same member+book is rejected.
- Issuing a book with zero available copies raises
  `BookUnavailableException`.

## 6. Testing

Manual test cases were run from the console menu:

| Test | Steps | Expected | Result |
|---|---|---|---|
| T1 | Add book B010, list books | B010 appears | Pass |
| T2 | Issue B001 to M001 | Available copies decrease by 1 | Pass |
| T3 | Issue B001 to M001 again | Error: already issued | Pass |
| T4 | Issue B009 (missing) | Error: No book found | Pass |
| T5 | Return B001 on time | Fine Rs.0 | Pass |
| T6 | Return 20 days late | Fine Rs.30 (6 days × 5) | Pass |
| T7 | Exit and restart | Data still present | Pass |

## 7. Screenshots / Sample Output

*(Insert console screenshots here when preparing the final submission.)*

Example session:

```
--- MAIN MENU ---
6. Issue a book
Choose: 6
Book ID: B001
Member ID: M001
Issued! Due date: 2026-10-19
```

## 8. Limitations & Future Enhancements

- No login/roles (admin vs librarian vs member) yet.
- No reservations queue when all copies are out.
- CSV files are not concurrent-safe; a real deployment would use SQLite/JDBC.
- Possible additions: GUI (JavaFX), email reminders, Excel export, and a
  web frontend.

## 9. Conclusion

SLMS demonstrates the practical use of OOP, collections, exception
handling, the Java Date/Time API, and file I/O in a small but complete
application. It is easy to run, easy to understand, and forms a solid
base for further enhancements.

## 10. References

- Oracle Java documentation: java.time, java.util.ArrayList, java.nio.file
- "Effective Java", Joshua Bloch (design inspiration)
