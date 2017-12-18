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
        expectWrapper(60+50-48, 48, 0, 0, 0);
    }


}