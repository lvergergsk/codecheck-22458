package codecheck;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class MainTest {

    @Test
    public void basicUsage() throws Exception {
        String input = "2017/01\n" +
                "2017/01/16 08:00-12:00 13:00-18:00\n" +
                "2017/01/17 08:00-12:00 13:00-18:00\n" +
                "2017/01/18 08:00-12:00 13:00-18:00\n" +
                "2017/01/19 08:00-12:00 13:00-17:00\n" +
                "2017/01/20 08:00-12:00 13:00-21:00";

        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));
        Main.main(null);

        String output = baos.toString();
        assertEquals("4\n10\n0\n0\n0\n", output);
    }

    @Test
    public void partialHourRoundHour() throws Exception {
        String input = "2017/02\n" +
                "2017/01/30 08:50-12:00 13:00-16:02\n" +
                "2017/01/31 08:40-12:00 13:00-21:00\n" +
                "2017/02/01 08:02-12:00 13:00-17:50\n" +
                "2017/01/20 08:00-12:00 13:00-21:20";

        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));
        Main.main(null);

        String output = baos.toString();
        // 1 Hour 2 Minuts -> 1
        // 48 Minuts -> 1
        assertEquals("1\n1\n0\n0\n0\n", output);
    }
}