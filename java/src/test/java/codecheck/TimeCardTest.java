package codecheck;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TimeCardTest {
    private TimeCard timeCard;

    private void expectWrapper(int type1,
                               int type2,
                               int type3,
                               int type4,
                               int type5) {
        assertEquals(type1, timeCard.getOvertimeWithinStatutoryWorkingMinutes());
        assertEquals(type2, timeCard.getOvertimeInExcessOfStatutoryWorkingMinutes());
        assertEquals(type3, timeCard.getLateNightOvertimeWorkingMinutes());
        assertEquals(type4, timeCard.getWorkingHoursOnPrescribedHolidayWorkingMinutes());
        assertEquals(type5, timeCard.getWorkingHoursOnStatutoryHolidayWorkingMinutes());
    }

    @Before
    public void initialize() {
        timeCard = new TimeCard("2017/02");
    }

    @Test
    public void basicUseage() {
        this.timeCard.work("2017/01/30 08:00-12:00 13:00-16:00");
        this.timeCard.work("2017/01/31 08:00-12:00 13:00-19:00");
        this.timeCard.work("2017/02/01 08:00-12:00 13:00-17:00");
        expectWrapper(60, 0, 0, 0, 0);
        this.timeCard.work("2017/02/02 08:00-12:00 13:00-16:00");
        expectWrapper(60, 0, 0, 0, 0);
        this.timeCard.work("2017/02/03 08:00-12:00 13:00-16:00");
        expectWrapper(60, 0, 0, 0, 0);
        this.timeCard.work("2017/02/07 08:00-12:00 13:00-16:00 17:00-23:00");
        expectWrapper(2 * 60, 5 * 60, 60, 0, 0);
    }

    @Test
    public void partialHour() {
        this.timeCard.work("2017/01/30 08:50-12:00 13:00-16:02");
        this.timeCard.work("2017/01/31 08:40-12:00 13:00-21:00");
        this.timeCard.work("2017/02/01 08:02-12:00 13:00-17:50");
        expectWrapper(60 + 50 - 48, 48, 0, 0, 0);
    }

    @Test
    public void givenTest1() {
        this.timeCard.work("2017/02/01 08:00-12:00 13:00-16:00");
        expectWrapper(0, 0, 0, 0, 0);
    }

    @Test
    public void givenTest2() {
        this.timeCard.work("2017/02/01 08:00-12:00 13:00-17:00");
        expectWrapper(60, 0, 0, 0, 0);
    }

    @Test
    public void givenTest3() {
        this.timeCard.work("2017/02/01 08:00-12:00 13:00-21:00");
        expectWrapper(60, 4 * 60, 0, 0, 0);
    }

    @Test
    public void givenTest4() {
        this.timeCard.work("2017/02/01 10:00-12:00 13:00-21:00");
        expectWrapper(3 * 60, 2 * 60, 0, 0, 0);
    }

    @Test
    public void givenTest5() {
        this.timeCard.work("2017/02/01 08:00-12:00 13:00-26:00");
        expectWrapper(1 * 60, 9 * 60, 4 * 60, 0, 0);
    }

    @Test
    public void givenTest6() {
        this.timeCard.work("2017/02/04 08:00-12:00 13:00-23:00");
        expectWrapper(0, 0, 60, 14 * 60, 0);
    }

    @Test
    public void givenTest7() {
        this.timeCard.work("2017/02/03 08:00-12:00 13:00-26:00");
        expectWrapper(60, 7 * 60, 4 * 60, 2 * 60, 0);
    }

    @Test
    public void givenTest8() {
        this.timeCard.work("2017/02/05 08:00-12:00 13:00-23:00");
        expectWrapper(0, 0, 1 * 60, 0, 14 * 60);
    }

    @Test
    public void givenTest9() {
        this.timeCard.work("2017/02/04 08:00-12:00 13:00-26:00");
        expectWrapper(0, 0, 4 * 60, 15 * 60, 2 * 60);
    }

    @Test
    public void givenTest10Case1() {
        this.timeCard = new TimeCard("2017/01");
        this.timeCard.work("2017/01/16 08:00-12:00 13:00-18:00");
        this.timeCard.work("2017/01/17 08:00-12:00 13:00-18:00");
        this.timeCard.work("2017/01/18 08:00-12:00 13:00-18:00");
        this.timeCard.work("2017/01/19 08:00-12:00 13:00-17:00");
        expectWrapper(4 * 60, 3 * 60, 0, 0, 0);
    }

    @Test
    public void givenTest10Case2() {
        this.timeCard = new TimeCard("2017/01");
        this.timeCard.work("2017/01/16 08:00-12:00 13:00-18:00");// 4PM-5PM: type1+=60; 5PM-6PM: type2+=60; 9
        expectWrapper(60, 60, 0, 0, 0);
        this.timeCard.work("2017/01/17 08:00-12:00 13:00-18:00");// 4PM-5PM: type1+=60; 5PM-6PM: type2+=60; 18
        expectWrapper(2 * 60, 2 * 60, 0, 0, 0);
        this.timeCard.work("2017/01/18 08:00-12:00 13:00-18:00");// 4PM-5PM: type1+=60; 5PM-6PM: type2+=60; 27
        expectWrapper(3 * 60, 3 * 60, 0, 0, 0);
        this.timeCard.work("2017/01/19 08:00-12:00 13:00-17:00");// 4PM-5PM: type1+=60; 35
        expectWrapper(4 * 60, 3 * 60, 0, 0, 0);
        this.timeCard.work("2017/01/20 08:00-12:00 13:00-21:00");// 2PM-21PM: type2+=7*60; !
        expectWrapper(4 * 60, 10 * 60, 0, 0, 0);
    }


}