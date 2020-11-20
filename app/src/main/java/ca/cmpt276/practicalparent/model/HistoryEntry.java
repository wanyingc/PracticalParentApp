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
    private int player, playerChoice;
    private int coinResult;
    private String time;
    private String date;
    private String dateTime;


    public HistoryEntry(int player, int playerChoice, int result) {
        this.player = player;
        this.playerChoice = playerChoice;
        this.coinResult = result;
        setTime();
        setDate();
    }

    public HistoryEntry(int player, int playerChoice, int result, String date, String time) {
        this.player = player;
        this.playerChoice = playerChoice;
        this.coinResult = result;
        this.date = date;
        this.time = time;
    }

    private void setTime() {
        LocalTime t = LocalTime.now();
        if (t.getMinute() < 10) {
            time = t.getHour()%13 + ":" + "0"+t.getMinute();
        } else {
            time = t.getHour()%13 + ":" + t.getMinute();
        }
    }

    private void setDate() {
        LocalDate d = LocalDate.now();
        String year = ""+d.getYear();
        date = d.getDayOfMonth() + "/" + d.getMonthValue() + "/" + year.substring(2,4);
    }

    // Getters

    public int getPlayer() {
        return player;
    }
    public int getPlayerChoice() {
        return playerChoice;
    }

    public boolean didWin() {
        return (playerChoice == coinResult);
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
