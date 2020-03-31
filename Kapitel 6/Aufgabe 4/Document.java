package de.bit.pl2.ex08;

public class Document {
    private String title;
    private String journal;
    private int year;

    public Document() {
    }

    public Document(String title, String journal, int year) {
        this.title = title;
        this.journal = journal;
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getJournal() {
        return journal;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
