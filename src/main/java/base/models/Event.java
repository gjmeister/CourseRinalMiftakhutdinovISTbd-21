package base.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public class Event {

    @NotNull(message = "Date should not be empty")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    public LocalDate date;

    @NotNull(message = "Start hour should not be empty")
    public LocalTime startHour;

    @NotNull(message = "End hour should not be empty")
    public LocalTime endHour;

    @NotEmpty(message = "Name should not be empty")
    public String name;

    @NotNull(message = "More information should not be empty")
    public String moreInformation;

    @NotEmpty(message = "Members should not be empty")
    public String members;

    public Event() {

    }

    public Event(String name, String moreInformation,
                 LocalTime eventStartHour, LocalTime eventEndHour, LocalDate eventDate, String members) {

        this.name = name;
        this.moreInformation = moreInformation;
        this.startHour = eventStartHour;
        this.endHour = eventEndHour;
        this.date = eventDate;
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMoreInformation(String moreInformation) {
        this.moreInformation = moreInformation;
    }

    public String getMoreInformation() {
        return moreInformation;
    }

    public void setStartHour(LocalTime startHour) {
        this.startHour = startHour;
    }

    public LocalTime getStartHour() {
        return startHour;
    }

    public void setEndHour(LocalTime endHour) {
        this.endHour = endHour;
    }

    public LocalTime getEndHour() {
        return endHour;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public String getMembers() {
        return members;
    }
}
