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
    private int headsPlayer, tailsPlayer, winner;
    private int coinResult;
    private LocalTime time;
    private LocalDate date;
    private String dateTime;

    public HistoryEntry(int headsPlayer, int tailsPlayer, int result) {
        this.headsPlayer = headsPlayer;
        this.tailsPlayer = tailsPlayer;
        this.coinResult = result;
        date = LocalDate.now();
        time = LocalTime.now();
        setWinner();
    }

    public HistoryEntry(int headsPlayer, int tailsPlayer, int result, String date, String time) {
        this.headsPlayer = headsPlayer;
        this.tailsPlayer = tailsPlayer;
        this.coinResult = result;
        this.dateTime = time + ", " + date;
        setWinner();
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
        if (time.getMinute() < 10) {
            return time.getHour()%12 + ":" + time.getMinute();
        } else {
            return time.getHour()%12 + ":" + "0"+time.getMinute();
        }
    }

    public String getDate() {
        String year = ""+date.getYear();
        return date.getDayOfMonth() + "/" + date.getMonthValue() + "/" + year.substring(2,4);
    }

}
