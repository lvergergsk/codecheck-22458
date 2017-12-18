package codecheck;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Scanner;

// Ticker is able to split time span according to special time point,
// Special time points are: 0:00, 5:00, 8:00, 16:00, 22:00, daily time limit, weekly time limit, end of span.
class Ticker {
    private static final boolean DEV_MODE = false;

    private final LocalDate givenDate;
    private Integer worktimeOfTheDayInMinutes;
    private Integer worktimeOfTheWeekInMinutes;

    private LocalDate startDate;
    private LocalTime startTime;

    private LocalDate endDate;
    private LocalTime endTime;

    private LocalDate dateTick;
    private LocalTime timeTick;
    private int tickLengthInMinute;

    // Need one instance for each line of input.
    public Ticker(String date, Integer worktimeOfTheWeekInMinutes) {
        this.givenDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        this.worktimeOfTheDayInMinutes = 0;
        this.worktimeOfTheWeekInMinutes = worktimeOfTheWeekInMinutes;
    }

    // Format: hh:mm
    // e.g. 08:21
    public void parseTime(String startTime, String endTime) {
        Scanner scanner = new Scanner(startTime.replace(':', ' '));
        int startHour = scanner.nextInt();
        int startMinute = scanner.nextInt();
        convert(startHour, startMinute, true);

        scanner = new Scanner(endTime.replace(':', ' '));
        int endHour = scanner.nextInt();
        int endMinute = scanner.nextInt();
        convert(endHour, endMinute, false);

        if (DEV_MODE) System.out.println("Given Date: " + this.givenDate.toString());
        if (DEV_MODE) System.out.println("Actual start date: " + this.startDate.toString());
        if (DEV_MODE) System.out.println("Actual start time: " + this.startTime.toString());
        if (DEV_MODE) System.out.println("Actual end date: " + this.endDate.toString());
        if (DEV_MODE) System.out.println("Actual end time: " + this.endTime.toString());

        // reset tick.
        dateTick = null;
        timeTick = null;
        tickLengthInMinute = 0;
    }

    // This will take care of time greater or equal to 24:00,
    // such as 24:00, 24:30, 25:00.
    // These representation of time is japanese specific (I guess).
    private void convert(int hour, int minute, boolean isStart) {
        LocalDate dt;
        LocalTime tm;

        if (hour < 24) {
            dt = LocalDate.from(this.givenDate);
            tm = LocalTime.of(hour, minute);
        } else {
            // If hour is greater or equal to 24:
            dt = LocalDate.from(this.givenDate);
            dt = dt.plusDays(1);
            tm = LocalTime.of(hour - 24, minute);
        }

        if (isStart) {
            this.startDate = dt;
            this.startTime = tm;
        } else {
            this.endDate = dt;
            this.endTime = tm;
        }
    }

    // Update to next tick, timeTick and dateTick equal to endTime and endDate, then return false,
    // otherwise return true;
    public boolean hasNextTick() {
        if (this.dateTick == null && this.timeTick == null) {
            this.dateTick = LocalDate.from(this.startDate);
            this.timeTick = LocalTime.from(this.startTime);
            updateTickLength();
        } else {
            this.timeTick = this.timeTick.plusMinutes(this.tickLengthInMinute);
            if (this.timeTick.equals(LocalTime.of(0, 0))) this.dateTick = this.dateTick.plusDays(1);
            updateTickLength();
            if (this.dateTick.equals(endDate) && this.timeTick.equals(endTime)) return false;
        }

        if (DEV_MODE) System.out.println("------------------------------------");
        if (DEV_MODE) System.out.println("dateTick: " + dateTick);
        if (DEV_MODE) System.out.println("timeTick: " + timeTick);
        if (DEV_MODE) System.out.println("tickLengthInMinute: " + tickLengthInMinute);
        return true;
    }

    // Update step time length to next event or end of the time span.
    // A "event" is a shift of way of calculating salary.
    private void updateTickLength() {

        // This assume there will not be two same time in one day (Equal sign.)
        int minutesUntilEndTime = (int) this.timeTick.until(this.endTime, ChronoUnit.MINUTES);
        minutesUntilEndTime = minutesUntilEndTime >= 0 ? minutesUntilEndTime : Integer.MAX_VALUE;

        int minutesUntilEndOfDay = (int) this.timeTick.until(LocalTime.MAX, ChronoUnit.MINUTES) + 1;

        int minutesUntilDayWorktimeLimit = 480 - this.worktimeOfTheDayInMinutes;
        minutesUntilDayWorktimeLimit = minutesUntilDayWorktimeLimit > 0 ? minutesUntilDayWorktimeLimit : Integer.MAX_VALUE;

        int minutesUntilWeekWorktimeLimit = 2400 - this.worktimeOfTheWeekInMinutes;
        minutesUntilWeekWorktimeLimit = minutesUntilWeekWorktimeLimit > 0 ? minutesUntilWeekWorktimeLimit : Integer.MAX_VALUE;

        int minutesUntilEndOfRegularWorkTime = (int) this.timeTick.until(LocalTime.of(16, 0), ChronoUnit.MINUTES);
        minutesUntilEndOfRegularWorkTime = minutesUntilEndOfRegularWorkTime > 0 ? minutesUntilEndOfRegularWorkTime : Integer.MAX_VALUE;

        int minutesUntilStartOfRegularWorkTime = (int) this.timeTick.until(LocalTime.of(8, 0), ChronoUnit.MINUTES);
        minutesUntilStartOfRegularWorkTime = minutesUntilStartOfRegularWorkTime > 0 ? minutesUntilStartOfRegularWorkTime : Integer.MAX_VALUE;

        int minutesUntilStartOfLateNight = (int) this.timeTick.until(LocalTime.of(22, 0), ChronoUnit.MINUTES);
        minutesUntilStartOfLateNight = minutesUntilStartOfLateNight > 0 ? minutesUntilStartOfLateNight : Integer.MAX_VALUE;

        int minutesUntilEndOfLateNight = (int) this.timeTick.until(LocalTime.of(5, 0), ChronoUnit.MINUTES);
        minutesUntilEndOfLateNight = minutesUntilEndOfLateNight > 0 ? minutesUntilEndOfLateNight : Integer.MAX_VALUE;

        this.tickLengthInMinute = min(minutesUntilEndTime, minutesUntilEndOfDay, minutesUntilDayWorktimeLimit,
                minutesUntilWeekWorktimeLimit, minutesUntilStartOfRegularWorkTime, minutesUntilEndOfRegularWorkTime,
                minutesUntilStartOfLateNight, minutesUntilEndOfLateNight);

        this.worktimeOfTheDayInMinutes += tickLengthInMinute;
        this.worktimeOfTheWeekInMinutes += tickLengthInMinute; // TODO: Confirm this is passed to the caller.

    }

    // Helper function
    public static int min(int... n) {
        int i = 0;
        int min = n[i];

        while (++i < n.length)
            if (n[i] < min)
                min = n[i];

        return min;
    }

    // Getter
    public LocalDate getDateTick() {
        return dateTick;
    }

    // Getter
    public LocalTime getTimeTick() {
        return timeTick;
    }

    // Getter
    public int getTickLengthInMinute() {
        return tickLengthInMinute;
    }

    // Getter
    public LocalDate getGivenDate() {
        return this.givenDate;
    }
}


// Monday is the start of a week.
// Saturday is the prescribed holiday.
// Sunday is statutory holiday.
// Regular working hour: 8:00〜12:00,13:00〜16:00
class TimeCard {
    private static final boolean DEV_MODE = true;

    // These are what we need to figure out.
    private int overtimeWithinStatutoryWorkingMinutes;
    private int overtimeInExcessOfStatutoryWorkingMinutes;
    private int lateNightOvertimeWorkingMinutes;
    private int workingHoursOnPrescribedHolidayWorkingMinutes;
    private int workingHoursOnStatutoryHolidayWorkingMinutes;

    // Time window.
    private LocalDate startingDay;
    private LocalDate endingDay;
    private LocalDate nextMonday;

    // Counting the total work time.
    private Integer worktimeOfTheDayInMinutes;
    private Integer worktimeOfTheWeekInMinutes;

    // You have to pass a string with format "yyyy/MM" to this constructor.
    public TimeCard(String month) {
        // Initialize fields.
        this.overtimeWithinStatutoryWorkingMinutes = 0;
        this.overtimeInExcessOfStatutoryWorkingMinutes = 0;
        this.lateNightOvertimeWorkingMinutes = 0;
        this.workingHoursOnPrescribedHolidayWorkingMinutes = 0;
        this.workingHoursOnStatutoryHolidayWorkingMinutes = 0;
        this.worktimeOfTheWeekInMinutes = 0;

        // Initialize time window.
        LocalDate dt = LocalDate.parse(month + "/01", DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        this.startingDay = dt.withDayOfMonth(1);
        this.endingDay = dt.withDayOfMonth(dt.lengthOfMonth());
        this.nextMonday = LocalDate.from(startingDay).with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));

        System.out.println("startingDay: " + startingDay);
        System.out.println("endingDay: " + endingDay);
    }

    // Consume a line.
    public void work(String line) {
        String[] tokens = line.split("\\s|-");
        if (DEV_MODE) {
            System.out.println("------------------------------------------------------------");
            for (String s : tokens) System.out.print(s + " ");
            System.out.println();
        }
        // Consume first element.
        Ticker ticker = new Ticker(tokens[0], this.worktimeOfTheWeekInMinutes);
        if (DEV_MODE) System.out.println(ticker.getGivenDate().getDayOfWeek().toString());

        // reset accumulated work time if necessary.
        worktimeOfTheDayInMinutes = 0;
        if (ticker.getGivenDate().isAfter(nextMonday) || ticker.getGivenDate().isEqual(nextMonday)) {
            this.worktimeOfTheWeekInMinutes = 0;
            nextMonday = nextMonday.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        }

        // Consume the remaining element.
        for (int f1 = 1; f1 < tokens.length; f1 += 2) {
            ticker.parseTime(tokens[f1], tokens[f1 + 1]);
            while (ticker.hasNextTick()) {
                addTime(ticker.getDateTick(), ticker.getTimeTick(), ticker.getTickLengthInMinute());
            }
        }
        if (DEV_MODE)
            System.out.println("Work time of the day: " + this.worktimeOfTheDayInMinutes / 60 + " hours "
                    + this.worktimeOfTheDayInMinutes % 60 + " minutes.");
        if (DEV_MODE)
            System.out.println("Work time of the week: " + this.worktimeOfTheWeekInMinutes / 60 + " hours "
                    + this.worktimeOfTheWeekInMinutes % 60 + " minutes.");

    }

    private void addTime(LocalDate dt, LocalTime tm, int minutes) {
        // If the date is within the intended month:
        if ((dt.isAfter(startingDay) || dt.equals(startingDay)) && dt.isBefore(endingDay)) {
            // Monday to Friday:
            if (dt.getDayOfWeek().equals(DayOfWeek.MONDAY) ||
                    dt.getDayOfWeek().equals(DayOfWeek.TUESDAY) ||
                    dt.getDayOfWeek().equals(DayOfWeek.WEDNESDAY) ||
                    dt.getDayOfWeek().equals(DayOfWeek.TUESDAY) ||
                    dt.getDayOfWeek().equals(DayOfWeek.FRIDAY)) {
                // Type 1
                if (isWithinWorktimeLimit() && !isRegularWorkTime(tm)) {
                    overtimeWithinStatutoryWorkingMinutes += minutes;
                    if (DEV_MODE) System.out.println("Type 1 += " + minutes + " minutes");
                }

                // Type 2
                if (!isWithinWorktimeLimit()) {
                    overtimeInExcessOfStatutoryWorkingMinutes += minutes;
                    if (DEV_MODE) System.out.println("Type 2 += " + minutes + " minutes");
                }

                // Type 3
                if (isLateNight(tm)) {
                    lateNightOvertimeWorkingMinutes += minutes;
                    if (DEV_MODE) System.out.println("Type 3 += " + minutes + " minutes");
                }
            }
            // Saturday:
            else if (dt.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
                // Type3
                if (isLateNight(tm)) {
                    this.lateNightOvertimeWorkingMinutes += minutes;
                    if (DEV_MODE) System.out.println("Type 3 += " + minutes + " minutes");
                }

                // Type4
                this.workingHoursOnPrescribedHolidayWorkingMinutes += minutes;
                if (DEV_MODE) System.out.println("Type 4 += " + minutes + " minutes");
            }
            // Sunday:
            else if (dt.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                // Type3
                if (isLateNight(tm)) {
                    this.lateNightOvertimeWorkingMinutes += minutes;
                    if (DEV_MODE) System.out.println("Type 3 += " + minutes + " minutes");
                }

                // Type5
                this.workingHoursOnStatutoryHolidayWorkingMinutes += minutes;
                if (DEV_MODE) System.out.println("Type 5 += " + minutes + " minutes");
            } else {
                if (DEV_MODE) System.out.println("ERROR!");
            }

        }
        this.worktimeOfTheDayInMinutes += minutes;
        this.worktimeOfTheWeekInMinutes += minutes;
    }

    public void printWorkingTime() {
        // TODO: Round up the value.

        System.out.print(overtimeWithinStatutoryWorkingMinutes + "\n");
        System.out.print(overtimeInExcessOfStatutoryWorkingMinutes + "\n");
        System.out.print(lateNightOvertimeWorkingMinutes + "\n");
        System.out.print(workingHoursOnPrescribedHolidayWorkingMinutes + "\n");
        System.out.print(workingHoursOnStatutoryHolidayWorkingMinutes + "\n");
    }

    // Helper function
    private boolean isRegularWorkTime(LocalTime tm) {
        return (tm.isAfter(LocalTime.of(8, 0)) || tm.equals(LocalTime.of(8, 0))) && tm.isBefore(LocalTime.of(16, 0));
    }

    // Helper function
    private boolean isLateNight(LocalTime tm) {
        return tm.isAfter(LocalTime.of(22, 0)) || tm.equals(LocalTime.of(22, 0)) || tm.isBefore(LocalTime.of(5, 0));
    }

    // Helper function
    private boolean isWithinWorktimeLimit() {
        return this.worktimeOfTheWeekInMinutes < 2400 && this.worktimeOfTheDayInMinutes < 480;
    }

    public int getOvertimeWithinStatutoryWorkingMinutes() {
        return overtimeWithinStatutoryWorkingMinutes;
    }

    public int getOvertimeInExcessOfStatutoryWorkingMinutes() {
        return overtimeInExcessOfStatutoryWorkingMinutes;
    }

    public int getLateNightOvertimeWorkingMinutes() {
        return lateNightOvertimeWorkingMinutes;
    }

    public int getWorkingHoursOnPrescribedHolidayWorkingMinutes() {
        return workingHoursOnPrescribedHolidayWorkingMinutes;
    }

    public int getWorkingHoursOnStatutoryHolidayWorkingMinutes() {
        return workingHoursOnStatutoryHolidayWorkingMinutes;
    }
}


public class Main {
    private static final boolean DEV_MODE = true;

    public static void main(String... args) {

    }
}
