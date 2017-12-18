package codecheck;

import org.junit.Test;

import java.time.LocalTime;

import static org.junit.Assert.*;

public class WorkingTimeTest {

    @Test
    public void getDayOfWeek() {
        LocalTime tm = LocalTime.of(23, 0);
        tm = tm.plusMinutes(60);
        assertTrue(tm.equals(LocalTime.of(0, 0)));
    }

    @Test
    public void parseMonth() {
    }

    @Test
    public void parseLine() {
    }

    @Test
    public void parseDate() {
    }

    @Test
    public void parseWorkingHour() {
    }

    @Test
    public void printWorkingTime() {
    }
}