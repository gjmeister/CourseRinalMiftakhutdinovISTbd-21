package base.models;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CalendarHour {
    public int hour;
    public CalendarDayOfWeek[] dayOfWeeks;

    public CalendarHour(int hour, LocalDate date) {
        this.hour = hour;
        this.dayOfWeeks = createWeek(date);
    }

    public String getHour(){
        var hourStr = Integer.toString(hour);
        if(hour < 10){
            hourStr = "0" + hourStr;
        }
        return hourStr;
    }

    private CalendarDayOfWeek[] createWeek(LocalDate startDate) {
        var week = new CalendarDayOfWeek[7];

        week[0] = new CalendarDayOfWeek("Monday", startDate);
        week[1] = new CalendarDayOfWeek("Tuesday", startDate.plusDays(1));
        week[2] = new CalendarDayOfWeek("Wednesday", startDate.plusDays(2));
        week[3] = new CalendarDayOfWeek("Thursday", startDate.plusDays(3));
        week[4] = new CalendarDayOfWeek("Friday", startDate.plusDays(4));
        week[5] = new CalendarDayOfWeek("Saturday", startDate.plusDays(5));
        week[6] = new CalendarDayOfWeek("Sunday", startDate.plusDays(6));

        return week;
    }

    public static class ScrollingDate{
        public LocalDate MondayOfCurrentWeek;

        public LocalDate getMondayOfCurrentWeek(LocalDate date){
            Integer dayOfWeek = date.getDayOfWeek().compareTo( DayOfWeek.MONDAY ) ;
            LocalDate startOfWeek =  date.minusDays( dayOfWeek );

            this.MondayOfCurrentWeek = LocalDate.parse(startOfWeek.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            return MondayOfCurrentWeek;
        }
    }

    public class CalendarDayOfWeek {
        public String name;
        public LocalDate date;
        public LocalTime startHour;

        public boolean isEventExist;
        public String eventName;
        public String eventMembers;

        public CalendarDayOfWeek(String name, LocalDate date) {
            this.name = name;
            this.date = date;
            this.isEventExist = false;
        }

        public void setEvent(String name, String members, LocalTime startHour) {
            this.eventName = name;
            this.eventMembers = members;
            this.startHour = startHour;
            this.isEventExist = true;
        }
    }
}
