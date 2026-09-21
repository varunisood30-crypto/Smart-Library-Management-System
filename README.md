# Smart Library Management System (SLMS)

A simple, **console-based Library Management System written in Core Java**.
It lets a librarian manage a book catalog, register members, issue and
return books, and automatically calculates overdue fines. All data is saved
to plain CSV files, so the project runs with **zero external libraries** —
all you need is a JDK.

> Academic project for Java Programming course submission.

---

## What the system can do (Features)

1. **Add books** to the catalog with ID, title, author, and copy count.
2. **Register members** with ID, name, and email.
3. **View** the full book list and member list.
4. **Search** books by title keyword.
5. **Issue a book** to a member — the system records the issue date and a
   due date 14 days later, and refuses to issue a book with no copies left.
6. **Return a book** — the system records the return date and computes any
   overdue fine at **Rs. 5 per day**.
7. **View active loans** and **all loans of a specific member**.
8. **Automatic saving** of books, members, and loans to CSV files under
   `data/`, so data persists between runs.

## Java concepts demonstrated

| Concept | Where it appears |
|---|---|
| Classes & Objects | `Book`, `Member`, `Loan`, `Library`, `Main` |
| Encapsulation | private fields with getters/setters in models |
| Inheritance & Exception handling | `LibraryException` and its subclasses |
| Collections Framework | `ArrayList`, `List` in `Library` |
| File I/O | CSV load/save in `Library` |
| Date/Time API | due dates, overdue calculation in `Loan` |
| Lambdas / method references | CSV parsers in `Library` |

## Project structure

```
SmartLibraryManagementSystem/
├── README.md
├── PROJECT_REPORT.md
├── build.bat                # Windows compile+run helper
├── build.sh                 # Linux/Mac compile+run helper
├── .gitignore
├── data/                    # generated at runtime (CSV files)
└── src/com/library/
    ├── Main.java            # console menu (entry point)
    ├── model/               # Book, Member, Loan
    ├── exception/           # LibraryException, BookNotFoundException, ...
    └── service/             # Library (business logic + persistence)
```

## How to compile and run

### Prerequisites
- JDK 17 or newer (`javac -version` and `java -version` should work).

### Windows (Command Prompt / PowerShell)
```powershell
javac -d out $(Get-ChildItem -Recurse src -Filter *.java | % FullName)
java -cp out com.library.Main
```
or double-click / run:
```powershell
.\build.bat
```

### Linux / Mac
```bash
javac -d out $(find src -name "*.java")
java -cp out com.library.Main
```
or:
```bash
chmod +x build.sh && ./build.sh
```

## Sample walkthrough

1. Start the program — it loads `data/*.csv` if present, otherwise it
   seeds 3 sample books and 2 sample members.
2. Choose **3** to view all books.
3. Choose **6**, enter book ID `B001` and member ID `M001` to issue a book.
4. Choose **8** to see active loans with the due date.
5. Choose **7** to return it and see the fine (Rs. 0 if within 14 days).
6. Choose **0** to save and exit — next run keeps all your data.

## Fine policy

- Loan period: **14 days**
- Overdue fine: **Rs. 5 per day** beyond the due date
- A member cannot borrow the same book twice while one copy is active.

## License

Academic use only — submitted for course evaluation.
