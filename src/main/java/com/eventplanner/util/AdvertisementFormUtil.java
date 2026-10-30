package com.eventplanner.util;

import com.eventplanner.model.Advertisement;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;

/**
 * Reads and validates the fields of the advertisement form.
 */
public class AdvertisementFormUtil {

    private AdvertisementFormUtil() {
    }

    public static Advertisement readAdvertisement(HttpServletRequest request, Map<String, String> errors) {
        Advertisement advertisement = new Advertisement();

        String title = WebUtil.trim(request.getParameter("title"));
        String description = WebUtil.trim(request.getParameter("description"));
        String eventIdValue = WebUtil.trim(request.getParameter("eventId"));
        String eventName = WebUtil.trim(request.getParameter("eventName"));
        String dateValue = WebUtil.trim(request.getParameter("eventDate"));
        String location = WebUtil.trim(request.getParameter("location"));
        String contactNumber = WebUtil.trim(request.getParameter("contactNumber"));
        String email = WebUtil.trim(request.getParameter("email"));

        advertisement.setTitle(title);
        advertisement.setDescription(description);
        advertisement.setEventName(eventName);
        advertisement.setLocation(location);
        advertisement.setContactNumber(contactNumber);
        advertisement.setEmail(email);

        request.setAttribute("advertisementDateValue", dateValue);

        if (title.isEmpty()) {
            errors.put("title", "Please enter the advertisement title.");
        } else if (title.length() > 150) {
            errors.put("title", "Advertisement title cannot be longer than 150 characters.");
        }

        if (description.isEmpty()) {
            errors.put("description", "Please enter an advertisement description.");
        } else if (description.length() < 10) {
            errors.put("description", "Description should be at least 10 characters long.");
        }

        if (!eventIdValue.isEmpty()) {
            try {
                advertisement.setEventId(Integer.parseInt(eventIdValue));
            } catch (NumberFormatException e) {
                advertisement.setEventId(null);
            }
        }

        if (eventName.isEmpty()) {
            errors.put("eventName", "Please enter the event name.");
        } else if (eventName.length() > 150) {
            errors.put("eventName", "Event name cannot be longer than 150 characters.");
        }

        if (dateValue.isEmpty()) {
            errors.put("eventDate", "Please select the event date.");
        } else {
            try {
                advertisement.setEventDate(java.sql.Date.valueOf(LocalDate.parse(dateValue)));
            } catch (DateTimeParseException e) {
                errors.put("eventDate", "Please enter a valid event date.");
            }
        }

        if (location.isEmpty()) {
            errors.put("location", "Please enter the event location.");
        } else if (location.length() > 150) {
            errors.put("location", "Location cannot be longer than 150 characters.");
        }

        if (contactNumber.isEmpty()) {
            errors.put("contactNumber", "Please enter a contact number.");
        } else if (!WebUtil.isValidPhone(contactNumber) || contactNumber.length() > 20) {
            errors.put("contactNumber", "Please enter a valid contact number (9 to 20 characters).");
        }

        if (email.isEmpty()) {
            errors.put("email", "Please enter an email address.");
        } else if (!WebUtil.isValidEmail(email) || email.length() > 100) {
            errors.put("email", "Please enter a valid email address.");
        }

        return advertisement;
    }
}
