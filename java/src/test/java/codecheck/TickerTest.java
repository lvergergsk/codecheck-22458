package codecheck;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

import static org.junit.Assert.*;

public class TickerTest {

    Ticker ticker;

    @Before
    public void initialize() {
        ticker = new Ticker("2017/01/30",0);
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
        expectWrapper(true, 2017, 1, 30, 8, 0, 240);
        expectWrapper(false, 2017, 1, 30, 12, 0, 0);

    }

    @Test
    public void acrossTheDay() {
        ticker.parseTime("13:00", "26:00");
        expectWrapper(true, 2017, 1, 30, 13, 0, 180);
        expectWrapper(true, 2017, 1, 30, 16, 0, 300);
        expectWrapper(true, 2017, 1, 30, 21, 0, 60);
        expectWrapper(true, 2017, 1, 30, 22, 0, 120);
        expectWrapper(true, 2017, 1, 31, 0, 0, 120);
        expectWrapper(false, 2017, 1, 31, 2, 0, 0);
    }

    @Test
    public void partialHour() {
        ticker.parseTime("23:32", "25:21");
        expectWrapper(true, 2017, 1, 30, 23, 32, 28);
        expectWrapper(true, 2017, 1, 31, 0, 0, 81);
        expectWrapper(false, 2017, 1, 31, 1, 21, 0);
    }

    @Test
    public void acrossTheMonth() {
        ticker = new Ticker("2017/01/31",0);
        ticker.parseTime("23:32", "25:21");
        expectWrapper(true, 2017, 1, 31, 23, 32, 28);
        expectWrapper(true, 2017, 2, 1, 0, 0, 81);
        expectWrapper(false, 2017, 2, 1, 1, 21, 0);
    }

    @Test
    public void dayWorkTimeLimitExceeded() {
        ticker.parseTime("8:00", "12:00");
        expectWrapper(true, 2017, 1, 30, 8, 0, 240);
        expectWrapper(false, 2017, 1, 30, 12, 0, 0);

        ticker.parseTime("13:00","21:00");
        expectWrapper(true, 2017, 1, 30, 13, 0, 180);
        expectWrapper(true, 2017, 1, 30, 16, 0, 60);
        expectWrapper(true, 2017, 1, 30, 17, 0, 240); //day work time exceed from here.
        expectWrapper(false, 2017, 1, 30, 21, 0, 0);
    }

    @Test
    public void dayWorkTimeLimitExceededPartialHour() {
        ticker.parseTime("8:02", "11:21");
        expectWrapper(true, 2017, 1, 30, 8, 2, 199);
        expectWrapper(false, 2017, 1, 30, 11, 21, 0);

        ticker.parseTime("13:11", "22:32");
        expectWrapper(true, 2017, 1, 30, 13, 11, 169);
        expectWrapper(true, 2017, 1, 30, 16, 0, 112);
        expectWrapper(true, 2017, 1, 30, 17, 52, 248); //day work time exceed from here.
        expectWrapper(true, 2017, 1, 30, 22, 00, 32);
        expectWrapper(false, 2017, 1, 30, 22, 32, 0);
    }

    @Test
    public void weekWorkTimeLimitExceed() {
        ticker = new Ticker("2017/01/30",2280); // 38 hours

        ticker.parseTime("8:00", "12:00");
        expectWrapper(true, 2017, 1, 30, 8, 0, 120);
        expectWrapper(true, 2017, 1, 30, 10, 0, 120);
        expectWrapper(false, 2017, 1, 30, 12, 0, 0);

    }
}