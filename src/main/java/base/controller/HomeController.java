package base.controller;

import base.dao.EventDAO;
import base.models.CalendarHour;
import base.models.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import base.models.person;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;

@Controller
public class HomeController {

     private final EventDAO eventDAO;

    @Autowired
    public HomeController(EventDAO eventDAO) {
        this.eventDAO = eventDAO;
    }

    @GetMapping("/")
    public String home(@RequestParam(name = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, Model model){
        date = date != null ? date : LocalDate.now();

        var events = eventDAO.index();

        var calendarHours = new CalendarHour[25];

        var ScrollingDate = new CalendarHour.ScrollingDate();
        for (var i = 0; i < 25; i++) {
            var dateMonday = ScrollingDate.getMondayOfCurrentWeek(date);

            calendarHours[i] = new CalendarHour(i, dateMonday);

            var hour = i;

            for (var dayOfWeek : calendarHours[i].dayOfWeeks) {
                if (events.containsKey(dayOfWeek.date)) {
                    var dataEvents = events.get(dayOfWeek.date)
                            .values()
                            .stream()
                            .filter(event -> event.getStartHour().getHour() <= hour && hour < event.getEndHour().getHour())
                            .findAny()
                            .orElse(null);
                    if(dataEvents != null){
                        dayOfWeek.setEvent(dataEvents.getName(), dataEvents.getMembers(), dataEvents.getStartHour());
                    }
                }
            }
        }



        model.addAttribute("nextWeekDates", ScrollingDate.getMondayOfCurrentWeek(date).plusDays(7).toString());
        model.addAttribute("prevWeekDates", ScrollingDate.getMondayOfCurrentWeek(date).minusDays(7).toString());

        model.addAttribute("week", calendarHours[0].dayOfWeeks);
        model.addAttribute("calendar", calendarHours);
        model.addAttribute("count", person.count);
        return "home/index";
    }

    @GetMapping("/add")
    public String newEvent(Model model){
        model.addAttribute("event", new Event());
        return "home/add";
    }

    @PostMapping("/home")
    public String add(@ModelAttribute("event") @Valid Event event, BindingResult bindingResult){

        if (bindingResult.hasErrors())
            return "home/add";

        if (event.getStartHour().isAfter(event.getEndHour()))
            bindingResult.rejectValue("endHour", "error.event", "End time should be greater than start time");

        if (eventDAO.isTimeIntersect(event)) {
            bindingResult.rejectValue("startHour", "error.event", "Time should not intersect with existing events");
            bindingResult.rejectValue("endHour", "error.event", "Time should not intersect with existing events");
        }

        if (bindingResult.hasErrors())
            return "home/add";

        eventDAO.save(event);
        return "redirect:/";
    }

    @GetMapping("/{date}/{eventStart}")
    public String show(@PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @PathVariable("eventStart") LocalTime eventStart, Model model){
        model.addAttribute("event", eventDAO.show(date, eventStart));
        return "home/show";
    }

    @GetMapping("/{date}/{eventStart}/edit")
    public String edit(Model model, @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @PathVariable("eventStart") LocalTime eventStart){
        model.addAttribute("event", eventDAO.show(date, eventStart));
        return "home/edit";
    }

    @PostMapping("/{date}/{eventStart}")
    public String update(@ModelAttribute("event") Event event, @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @PathVariable("eventStart") LocalTime eventStart, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return "home/add";

        if (event.getStartHour().isAfter(event.getEndHour()))
            bindingResult.rejectValue("endHour", "error.event", "End time should be greater than start time");

        if (bindingResult.hasErrors())
            return "home/add";
        eventDAO.update(event, date, eventStart, eventDAO);
        return "redirect:/";
    }

    @GetMapping("/{date}/{eventStart}/remove")
    public String delete(@PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @PathVariable("eventStart") LocalTime eventStart){
        eventDAO.delete(date, eventStart);
        return "redirect:/";
    }
}
