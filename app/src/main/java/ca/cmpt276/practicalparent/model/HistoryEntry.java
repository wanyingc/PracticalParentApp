package ca.cmpt276.practicalparent.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

public class HistoryEntry {
    private int headsPlayer, tailsPlayer;
    private int result;
    private LocalTime time;
    private LocalDate date;

    public HistoryEntry(int headsPlayer, int tailsPlayer, int result) {
        this.headsPlayer = headsPlayer;
        this.tailsPlayer = tailsPlayer;
        this.result = result;
        date = LocalDate.now();
        time = LocalTime.now();
    }


    // Getters

    public int getHeadsPlayer() {
        return headsPlayer;
    }

    public int getTailsPlayer() {
        return tailsPlayer;
    }

    public String getResult() {
        return (result == Coin.HEADS) ? "Heads" : "Tails";
    }

    public LocalTime getTime() {
        return time;
    }

    public LocalDate getDate() {
        return date;
    }

}
