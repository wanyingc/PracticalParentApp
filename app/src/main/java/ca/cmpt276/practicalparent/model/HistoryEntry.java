package ca.cmpt276.practicalparent.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

public class HistoryEntry {
    private int child1, child2;
    private String childOneChoice, childTwoChoice;
    private String result;
    private LocalTime time;
    private LocalDate date;

    public HistoryEntry(int child1, int child2, String childOneChoice, String childTwoChoice, String result) {
        this.child1 = child1;
        this.child2 = child2;
        this.childOneChoice = childOneChoice;
        this.childTwoChoice = childTwoChoice;
        this.result = result;
        date = LocalDate.now();
        time = LocalTime.now();
    }


    // Getters

    public int getChild1() {
        return child1;
    }

    public int getChild2() {
        return child2;
    }

    public String getResult() {
        return result;
    }

    public String getChildOneChoice() {
        return childOneChoice;
    }

    public String getChildTwoChoice() {
        return childTwoChoice;
    }

    public LocalTime getTime() {
        return time;
    }

    public LocalDate getDate() {
        return date;
    }

}
