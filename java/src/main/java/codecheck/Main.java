package codecheck;

import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;


class Ticker {
    private static final boolean DEV_MODE = true;

    private final LocalDate givenDate;

    private LocalDate startDate;
    private LocalTime startTime;

    private LocalDate endDate;
    private LocalTime endTime;

    private LocalDate dateTick;
    private LocalTime timeTick;
    private int tickLengthInMinute;

    // Need one instance for each line of input.
    public Ticker(String date) {
        this.givenDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
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

    // update step time length to next o'clock or end of the time span.
    private void updateTickLength() {
        if (this.dateTick.equals(this.endDate)) {
            int minutesUntilEndTime = (int) this.timeTick.until(this.endTime, ChronoUnit.MINUTES);
            int minutesUntilNextHour = 60 - timeTick.getMinute();
            this.tickLengthInMinute = minutesUntilEndTime < minutesUntilNextHour ? minutesUntilEndTime : minutesUntilNextHour;
        } else {
            this.tickLengthInMinute = 60 - timeTick.getMinute();
        }
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
}


// Monday is the start of a week.
// Saturday is the prescribed holiday.
// Sunday is statutory holiday.
// Regular working hour: 8:00〜12:00,13:00〜16:00
class WorkingTime {
    // DEV_MODE is for printing debug message.
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

    // Counting the total work time.
    Integer worktimeOfTheDayInMinutes;
    Integer worktimeOfTheWeekInMinutes;

    // You have to pass a string with format "yyyy/MM" to this constructor.
    public WorkingTime(String month) {
        // Initialize fields.
        this.overtimeWithinStatutoryWorkingMinutes = 0;
        this.overtimeInExcessOfStatutoryWorkingMinutes = 0;
        this.lateNightOvertimeWorkingMinutes = 0;
        this.workingHoursOnPrescribedHolidayWorkingMinutes = 0;
        this.workingHoursOnStatutoryHolidayWorkingMinutes = 0;
        this.worktimeOfTheDayInMinutes = 0;
        this.worktimeOfTheWeekInMinutes = 0;

        // Initialize time window.
        LocalDate dt = LocalDate.parse(month + "/01");
        this.startingDay = dt.withDayOfMonth(1);
        this.endingDay = dt.withDayOfMonth(dt.lengthOfMonth());
    }

    // Wrap parseDate and ParseWorkingHour
    public void work(String line) {
        String[] tokens = line.split("\\s|-");
        LocalDate dt = LocalDate.parse(tokens[0]);

        // Reset the total work time if necessary.
        this.worktimeOfTheDayInMinutes = 0;
        if (dt.getDayOfWeek().equals(DayOfWeek.MONDAY))
            this.worktimeOfTheWeekInMinutes = 0;

        for (int i = 1; i < tokens.length; i += 2) {


        }

    }

    // This method will update corresponding overtime hours according to [beginning,ending).
    // This method is a wrapper of countHour method.
    private void countOvertime(LocalDate dt, LocalTime beginning, LocalTime ending) {

    }


    // Update corresponding overtime hour according to [tm, nextHour).
    // e.g. [9:23,10:00) or [21:00, 22:00)
    private void countHour(LocalDate dt, LocalTime tm) throws Exception {
        // If it is workday.

        Integer durationInMinutes = (60 - tm.getMinute());

        //If the date is within the intended month.
        if (dt.isAfter(startingDay) && dt.isBefore(endingDay)) {
            if (dt.getDayOfWeek().equals(DayOfWeek.MONDAY) ||
                    dt.getDayOfWeek().equals(DayOfWeek.TUESDAY) ||
                    dt.getDayOfWeek().equals(DayOfWeek.WEDNESDAY) ||
                    dt.getDayOfWeek().equals(DayOfWeek.TUESDAY) ||
                    dt.getDayOfWeek().equals(DayOfWeek.FRIDAY)) {
                // Workday:

                // Type 1
                if (isWithinWorktimeLimit() && !isRegularWorkTime(tm)) {
                    overtimeWithinStatutoryWorkingMinutes += durationInMinutes;
                }

                // Type 2
                if (!isWithinWorktimeLimit()) {
                    overtimeInExcessOfStatutoryWorkingMinutes += durationInMinutes;
                }

                // Type 3
                if (isLateNight(tm)) {
                    lateNightOvertimeWorkingMinutes += durationInMinutes;
                }


            } else if (dt.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
                // Saturday:

                // Type4
                this.workingHoursOnPrescribedHolidayWorkingMinutes += durationInMinutes;

                // Type3
                if (isLateNight(tm)) {
                    this.lateNightOvertimeWorkingMinutes += durationInMinutes;
                }

            } else if (dt.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                // Sunday:

                // Type5
                this.workingHoursOnStatutoryHolidayWorkingMinutes += durationInMinutes;


            } else {
                throw new Exception("Something is wrong, you got " + dt.getDayOfWeek().toString() + ".");
            }
        }

        this.worktimeOfTheDayInMinutes++;
        this.worktimeOfTheWeekInMinutes++;
    }


    // Parse the first element of a working hours line
    public LocalDate parseDate(String date) throws ParseException {
        LocalDate dt = LocalDate.parse(date);
        return dt;
    }

    // Parse the remaining elements of a working hours line.
    public void parseWorkingHour() {
        // TODO: Implement this.
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

}


public class Main {
    private static final boolean DEV_MODE = true;

    public static void main(String... args) {

    }
}
