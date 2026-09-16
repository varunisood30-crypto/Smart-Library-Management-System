package com.library.model;

/**
 * Represents a single book title in the library catalog.
 */
public class Book {
    private String id;
    private String title;
    private String author;
    private int totalCopies;
    private int availableCopies;

    public Book(String id, String title, String author, int totalCopies) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getTotalCopies() { return totalCopies; }
    public int getAvailableCopies() { return availableCopies; }

    public void borrowCopy() { availableCopies--; }
    public void returnCopy() { availableCopies++; }
    public void setAvailableCopies(int n) { this.availableCopies = n; }

    /** One CSV line used for persistence. */
    public String toCsv() {
        return String.join(",", escape(id), escape(title), escape(author),
                String.valueOf(totalCopies), String.valueOf(availableCopies));
    }

    public static Book fromCsv(String line) {
        String[] p = split(line);
        Book b = new Book(p[0], p[1], p[2], Integer.parseInt(p[3]));
        b.setAvailableCopies(Integer.parseInt(p[4]));
        return b;
    }

    static String escape(String s) { return s.replace(",", ";"); }
    static String[] split(String line) { return line.split(",", -1); }

    @Override
    public String toString() {
        return String.format("%-6s %-30s %-20s %d/%d available",
                id, title, author, availableCopies, totalCopies);
    }
}
