package ca.cmpt276.practicalparent.model;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;

public class HistoryEntry {
    private int headsPlayer, tailsPlayer;
    private int result;
    private LocalTime time;
    private LocalDate date;
    private String dateTime;

    public HistoryEntry(int headsPlayer, int tailsPlayer, int result) {
        this.headsPlayer = headsPlayer;
        this.tailsPlayer = tailsPlayer;
        this.result = result;
        date = LocalDate.now();
        time = LocalTime.now();
    }

    public HistoryEntry(int headsPlayer, int tailsPlayer, int result, String date, String time) {
        this.headsPlayer = headsPlayer;
        this.tailsPlayer = tailsPlayer;
        this.result = result;
        this.dateTime = time + ", " + date;
    }

    // Getters

    public int getHeadsPlayer() {
        return headsPlayer;
    }

    public int getTailsPlayer() {
        return tailsPlayer;
    }

    public int getResult() {
        return result;
    }

    public String getTime() {
        return time.getHour()%12 + ":" + time.getMinute();
    }

    public String getDate() {
        String year = ""+date.getYear();
        return date.getDayOfMonth() + "/" + date.getMonthValue() + "/" + year.substring(2,4);
    }

}
