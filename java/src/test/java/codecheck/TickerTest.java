package codecheck;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

import static org.junit.Assert.*;

public class TickerTest {

    Ticker ticker;

    @Before
    public void initialize() {
        ticker = new Ticker("2017/01/30");
    }

    // Helper function
    // This Function has SIDE EFFECT.
    private void expectWrapper(boolean hasNext, int year, int month, int day, int hour, int minute, int length) {
        assertEquals(hasNext, ticker.hasNextTick());
        assertTrue(ticker.getDateTick().equals(LocalDate.of(year, month, day)));
        assertTrue(ticker.getTimeTick().equals(LocalTime.of(hour, minute)));
        assertEquals(length, ticker.getTickLengthInMinute());
    }

    @Test
    public void basicUseage() {
        ticker.parseTime("08:00", "12:00");
        expectWrapper(true, 2017, 1, 30, 8, 0, 60);
        expectWrapper(true, 2017, 1, 30, 9, 0, 60);
        expectWrapper(true, 2017, 1, 30, 10, 0, 60);
        expectWrapper(true, 2017, 1, 30, 11, 0, 60);
        expectWrapper(false, 2017, 1, 30, 12, 0, 0);

    }

    @Test
    public void acrossTheDay() {
        ticker.parseTime("13:00", "26:00");
        expectWrapper(true, 2017, 1, 30, 13, 0, 60);
        expectWrapper(true, 2017, 1, 30, 14, 0, 60);
        expectWrapper(true, 2017, 1, 30, 15, 0, 60);
        expectWrapper(true, 2017, 1, 30, 16, 0, 60);
        expectWrapper(true, 2017, 1, 30, 17, 0, 60);
        expectWrapper(true, 2017, 1, 30, 18, 0, 60);
        expectWrapper(true, 2017, 1, 30, 19, 0, 60);
        expectWrapper(true, 2017, 1, 30, 20, 0, 60);
        expectWrapper(true, 2017, 1, 30, 21, 0, 60);
        expectWrapper(true, 2017, 1, 30, 22, 0, 60);
        expectWrapper(true, 2017, 1, 30, 23, 0, 60);
        expectWrapper(true, 2017, 1, 31, 0, 0, 60);
        expectWrapper(true, 2017, 1, 31, 1, 0, 60);
        expectWrapper(false, 2017, 1, 31, 2, 0, 0);
    }

    @Test
    public void partialHour() {
        ticker.parseTime("23:32", "25:21");
        expectWrapper(true, 2017, 1, 30, 23, 32, 28);
        expectWrapper(true, 2017, 1, 31, 0, 0, 60);
        expectWrapper(true, 2017, 1, 31, 1, 0, 21);
        expectWrapper(false, 2017, 1, 31, 1, 21, 0);
    }

    @Test
    public void acrossTheMonth() {
        ticker = new Ticker("2017/01/31");
        ticker.parseTime("23:32", "25:21");
        expectWrapper(true, 2017, 1, 31, 23, 32, 28);
        expectWrapper(true, 2017, 2, 1, 0, 0, 60);
        expectWrapper(true, 2017, 2, 1, 1, 0, 21);
        expectWrapper(false, 2017, 2, 1, 1, 21, 0);
    }
}