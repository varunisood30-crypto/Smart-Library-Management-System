package com.library.model;

/**
 * Represents a registered library member.
 */
public class Member {
    private String id;
    private String name;
    private String email;

    public Member(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }

    public String toCsv() {
        return String.join(",", id, name.replace(",", ";"), email);
    }

    public static Member fromCsv(String line) {
        String[] p = line.split(",", -1);
        return new Member(p[0], p[1], p[2]);
    }

    @Override
    public String toString() {
        return String.format("%-6s %-20s %s", id, name, email);
    }
}
