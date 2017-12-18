package codecheck;

import java.time.LocalDate;

class WorkingTime {
    // DEV_MODE is for printing debug message.
    private static final boolean DEV_MODE = true;

    // These are what we need to figure out.
    int overtimeWithinStatutoryWorkingHours;
    int overtimeInExcessOfStatutoryWorkingHours;
    int lateNightOvertimeWorkingHours;
    int workingHoursOnPrescribedHoliday;
    int workingHoursOnStatutoryHoliday;

    public WorkingTime() {
        overtimeWithinStatutoryWorkingHours = 0;
        overtimeInExcessOfStatutoryWorkingHours = 0;
        lateNightOvertimeWorkingHours = 0;
        workingHoursOnPrescribedHoliday = 0;
        workingHoursOnStatutoryHoliday = 0;
    }


    // Monday is the start of a week.
    // Saturday is the prescribed holiday.
    // Sunday is statutory holiday.
    public static String getDayOfWeek(String day, String month, String year) {
        LocalDate dt = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
        return dt.getDayOfWeek().toString();
    }

    public void parseMonth() {
        // TODO: Implement this.
    }

    // Wrap parseDate and ParseWorkingHour
    public void parseLine() {
        // TODO: Implement this.
    }

    // Parse the first element of a working hours line
    public void parseDate() {
        // TODO: Implement this.
    }

    // Parse the remaining elements of a working hours line.
    public void parseWorkingHour() {
        // TODO: Implement this.
    }

    public void printWorkingTime() {
        // TODO: Round up the value.

        System.out.print(overtimeWithinStatutoryWorkingHours + "\n");
        System.out.print(overtimeInExcessOfStatutoryWorkingHours + "\n");
        System.out.print(lateNightOvertimeWorkingHours + "\n");
        System.out.print(workingHoursOnPrescribedHoliday + "\n");
        System.out.print(workingHoursOnStatutoryHoliday + "\n");
    }

}


public class Main {
    private static final boolean DEV_MODE = true;

    public static void main(String... args) {


        System.out.println("Hello, world!");
    }
}
