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

/**
 * Stores the previous turn of a coin flip
 */
public class HistoryEntry {
    private int headsPlayer, tailsPlayer, winner;
    private int coinResult;
    private String time;
    private String date;
    private String dateTime;


    public HistoryEntry(int headsPlayer, int tailsPlayer, int result) {
        this.headsPlayer = headsPlayer;
        this.tailsPlayer = tailsPlayer;
        this.coinResult = result;
        setTime();
        setDate();
        setWinner();
    }

    public HistoryEntry(int headsPlayer, int tailsPlayer, int result, String date, String time) {
        this.headsPlayer = headsPlayer;
        this.tailsPlayer = tailsPlayer;
        this.coinResult = result;
        this.date = date;
        this.time = time;
        setWinner();
    }

    private void setTime() {
        LocalTime t = LocalTime.now();
        if (t.getMinute() < 10) {
            time = t.getHour()%12 + ":" + "0"+t.getMinute();
        } else {
            time = t.getHour()%12 + ":" + t.getMinute();
        }
    }

    private void setDate() {
        LocalDate d = LocalDate.now();
        String year = ""+d.getYear();
        date = d.getDayOfMonth() + "/" + d.getMonthValue() + "/" + year.substring(2,4);
    }

    private void setWinner() {
        if (coinResult == Coin.HEADS) {
            winner = headsPlayer;
        } else {
            winner = tailsPlayer;
        }
    }

    // Getters

    public int getHeadsPlayer() {
        return headsPlayer;
    }

    public int getTailsPlayer() {
        return tailsPlayer;
    }

    public int getWinner() {
        return winner;
    }

    public int getCoinResult() {
        return coinResult;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

}
