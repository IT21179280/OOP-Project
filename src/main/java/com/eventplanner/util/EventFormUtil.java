package com.eventplanner.util;

import com.eventplanner.model.Event;

import javax.servlet.http.HttpServletRequest;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Map;

/**
 * Reads and validates the fields of the event form. The same class is used
 * by EventCreateServlet and EventUpdateServlet.
 */
public class EventFormUtil {

    private EventFormUtil() {
    }

    /**
     * Reads the form fields into an Event object. Any validation errors are
     * added to the errors map.
     */
    public static Event readEvent(HttpServletRequest request, Map<String, String> errors) {
        Event event = new Event();

        String title = WebUtil.trim(request.getParameter("eventTitle"));
        String description = WebUtil.trim(request.getParameter("description"));
        String categoryValue = WebUtil.trim(request.getParameter("categoryId"));
        String dateValue = WebUtil.trim(request.getParameter("eventDate"));
        String startValue = WebUtil.trim(request.getParameter("startTime"));
        String endValue = WebUtil.trim(request.getParameter("endTime"));
        String location = WebUtil.trim(request.getParameter("location"));
        String contact = WebUtil.trim(request.getParameter("contactInformation"));

        event.setEventTitle(title);
        event.setDescription(description);
        event.setLocation(location);
        event.setContactInformation(contact);

        // Keep the raw values so that the form can be filled again after
        // an error, even when a date/time value could not be parsed.
        request.setAttribute("eventDateValue", dateValue);
        request.setAttribute("startTimeValue", startValue);
        request.setAttribute("endTimeValue", endValue);

        if (title.isEmpty()) {
            errors.put("eventTitle", "Please enter the event title.");
        } else if (title.length() > 150) {
            errors.put("eventTitle", "Event title cannot be longer than 150 characters.");
        }

        if (description.isEmpty()) {
            errors.put("description", "Please enter a description for the event.");
        } else if (description.length() < 10) {
            errors.put("description", "Description should be at least 10 characters long.");
        }

        if (categoryValue.isEmpty()) {
            errors.put("categoryId", "Please select an event category.");
        } else {
            try {
                int categoryId = Integer.parseInt(categoryValue);
                event.setCategoryId(categoryId);
                if (categoryId <= 0) {
                    errors.put("categoryId", "Please select a valid event category.");
                }
            } catch (NumberFormatException e) {
                errors.put("categoryId", "Please select a valid event category.");
            }
        }

        if (dateValue.isEmpty()) {
            errors.put("eventDate", "Please select the event date.");
        } else {
            try {
                LocalDate date = LocalDate.parse(dateValue);
                event.setEventDate(java.sql.Date.valueOf(date));
            } catch (DateTimeParseException e) {
                errors.put("eventDate", "Please enter a valid event date.");
            }
        }

        if (startValue.isEmpty()) {
            errors.put("startTime", "Please enter the start time.");
        } else {
            try {
                event.setStartTime(Time.valueOf(LocalTime.parse(startValue)));
            } catch (DateTimeParseException e) {
                errors.put("startTime", "Please enter a valid start time.");
            }
        }

        if (endValue.isEmpty()) {
            errors.put("endTime", "Please enter the end time.");
        } else {
            try {
                event.setEndTime(Time.valueOf(LocalTime.parse(endValue)));
            } catch (DateTimeParseException e) {
                errors.put("endTime", "Please enter a valid end time.");
            }
        }

        if (event.getStartTime() != null && event.getEndTime() != null
                && !event.getEndTime().after(event.getStartTime())) {
            errors.put("endTime", "The end time must be after the start time.");
        }

        if (location.isEmpty()) {
            errors.put("location", "Please enter the event location.");
        } else if (location.length() > 150) {
            errors.put("location", "Location cannot be longer than 150 characters.");
        }

        if (contact.isEmpty()) {
            errors.put("contactInformation", "Please enter contact information.");
        } else if (contact.length() > 100) {
            errors.put("contactInformation", "Contact information cannot be longer than 100 characters.");
        }

        return event;
    }
}
