package base.dao;

import base.models.Event;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import base.models.person;

@Component
public class EventDAO {

    private HashMap<LocalDate, HashMap<LocalTime, Event>> eventList;
    {
        eventList = new HashMap<>();

        var eventArr = new HashMap<LocalTime, Event>();
        var eventArr2 = new HashMap<LocalTime, Event>();
        eventArr.put(LocalTime.of(2, 0), new Event("Test1", "Info1...", LocalTime.of(2, 0), LocalTime.of(4, 0), LocalDate.of(2021, 12, 13), "TestMembers1"));
        eventArr2.put(LocalTime.of(7, 0), new Event("Test2", "Info2...", LocalTime.of(7, 0), LocalTime.of(10, 0), LocalDate.of(2021, 12, 14), "TestMembers2"));
        eventList.put(LocalDate.of(2021, 12, 13), eventArr);
        eventList.put(LocalDate.of(2021, 12, 14), eventArr2);
    }

    public HashMap<LocalDate, HashMap<LocalTime, Event>> index(){
        return eventList;
    }

    public void save(Event event) {
        person.count += 1;

        var date = event.getDate();

        var startTime = event.getStartHour();

        if(eventList.containsKey(date)){
            eventList.get(date).put(startTime, event);
        }
        else {
            var eventArr = new HashMap<LocalTime, Event>();

            eventArr.put(startTime, event);
            eventList.put(date, eventArr);
        }
    }

    public void delete(LocalDate date, LocalTime eventStart){
        eventList.get(date).remove(eventStart);
        person.count -= 1;
    }


    public Event show(LocalDate date, LocalTime eventStart){
        return eventList.get(date).get(eventStart);
    }

    public void update(Event updatedEvent,LocalDate date, LocalTime eventStart, EventDAO eventDAO){

        eventDAO.delete(date, eventStart);
        eventDAO.save(updatedEvent);
    }

    public boolean isTimeIntersect(Event event) {
        var currentDate = event.getDate();

        var first = eventList.get(currentDate);

        if(first == null){
            return false;
        }else {
            var second = first.values();

            var third = second.stream();

            var fourth = third.anyMatch(e -> e.getDate().isEqual(event.getDate())
                    && ((e.getStartHour().isBefore(event.getStartHour()) && e.getEndHour().isAfter(event.getStartHour()))
                    || (e.getStartHour().isBefore(event.getEndHour()) && e.getEndHour().isAfter(event.getStartHour()))
                    || (e.getEndHour().isBefore(event.getEndHour()) && e.getStartHour().isAfter(event.getStartHour()))));

            return fourth;
        }
    }
}
