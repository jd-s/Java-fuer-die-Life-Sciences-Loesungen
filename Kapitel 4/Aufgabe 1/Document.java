package de.bit.pl2.ex10;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Document {

    private int pmid;
    private List<String> pubtype;
    private String text; //abstract


    private String authors;
    private String title;
    private Date publishingDate;
    private String journal;

    public Document() {
    }

    public Document(int pmid, List<String> pubtypes, String text) {
        this.pmid = pmid;
        this.pubtype = pubtype;
        this.text = text;
    }

    public static String bin(Document input) {
        int clinicalScore = 0;
        int reviewScore = 0;
        int otherScore = 0;

        // Text contains "clinical study" -- indication
        if (input.getText().toLowerCase().contains("clinical trial")) {
            clinicalScore += 5;
        }

        // Text contains "trial registration" -- indication
        if (input.getText().toLowerCase().contains("trial registration")) {
            clinicalScore += 5;
        }

        // Text contains "trial registration" -- indication
        if (input.getText().toLowerCase().contains("this study")) {
            clinicalScore += 5;
        }

        // Text contains "We show" -- small indication
        if (input.getText().toLowerCase().contains("we show")) {
            otherScore++;
            reviewScore--;
        }

        // Title contains "review" -- indication
        if (input.getTitle().toLowerCase().contains("review")) {
            reviewScore += 5;
        }

        // Title contains "systematic review" -- indication
        if (input.getTitle().toLowerCase().contains("systematic review")) {
            reviewScore += 5;
        }

        // Title contains "evidence review" -- indication
        if (input.getTitle().toLowerCase().contains("evidence review")) {
            reviewScore += 5;
        }

        // Title contains "screening" -- small indication
        if (input.getTitle().toLowerCase().contains("screening")) {
            reviewScore++;
        }

        HashMap<String, Integer> scores = new HashMap<String, Integer>();
        scores.put("Other Research", otherScore);
        scores.put("Clinical Study", clinicalScore);
        scores.put("Review", reviewScore);


        String highestCategory = null;
        int highestScore = 0;
        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            if (entry.getValue() >= highestScore) {
                highestCategory = entry.getKey();
                highestScore = entry.getValue();
            }
        }

        return highestCategory;
    }

    public int getPmid() {
        return pmid;
    }

    public void setPmid(int pmid) {
        this.pmid = pmid;
    }

    public List<String> getPubtypes() {
        return pubtype;
    }

    public void setPubtype(List<String> pubtype) {
        this.pubtype = pubtype;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getPublishingDate() {
        return publishingDate;
    }

    public void setPublishingDate(Date publishingDate) {
        this.publishingDate = publishingDate;
    }

    public String getJournal() {
        return journal;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }

}