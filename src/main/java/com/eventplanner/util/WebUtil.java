package com.eventplanner.util;

import com.eventplanner.model.Admin;
import com.eventplanner.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Pattern;

/**
 * Small helper methods used by the servlets and the JSP pages.
 */
public class WebUtil {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^[0-9+()\\-\\s]{9,20}$");

    private WebUtil() {
    }

    /* ------------------------------------------------------------------
     * Text helpers
     * ------------------------------------------------------------------ */

    public static String escape(String value) {
        if (value == null) {
            return "";
        }
        StringBuilder result = new StringBuilder(value.length());
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            switch (c) {
                case '&':
                    result.append("&amp;");
                    break;
                case '<':
                    result.append("&lt;");
                    break;
                case '>':
                    result.append("&gt;");
                    break;
                case '"':
                    result.append("&quot;");
                    break;
                case '\'':
                    result.append("&#39;");
                    break;
                default:
                    result.append(c);
            }
        }
        return result.toString();
    }

    public static String display(String value) {
        return isBlank(value) ? "-" : escape(value);
    }

    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static String trim(String value) {
        return value == null ? "" : value.trim();
    }

    public static String truncate(String value, int length) {
        if (value == null) {
            return "";
        }
        String text = value.trim();
        if (text.length() <= length) {
            return text;
        }
        return text.substring(0, length).trim() + "...";
    }

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }

    /* ------------------------------------------------------------------
     * Date and time helpers
     * ------------------------------------------------------------------ */

    public static String formatDate(Date date) {
        if (date == null) {
            return "-";
        }
        return new SimpleDateFormat("dd MMM yyyy").format(date);
    }

    public static String formatTime(Time time) {
        if (time == null) {
            return "-";
        }
        return new SimpleDateFormat("hh:mm a").format(time);
    }

    public static String formatDateTime(Timestamp timestamp) {
        if (timestamp == null) {
            return "-";
        }
        return new SimpleDateFormat("dd MMM yyyy, hh:mm a").format(timestamp);
    }

    public static String formatDateForInput(Date date) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public static String formatTimeForInput(Time time) {
        if (time == null) {
            return "";
        }
        return new SimpleDateFormat("HH:mm").format(time);
    }

    public static boolean isUpcoming(Date eventDate) {
        if (eventDate == null) {
            return false;
        }
        Calendar today = Calendar.getInstance();
        Calendar event = Calendar.getInstance();
        event.setTime(eventDate);

        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        event.set(Calendar.HOUR_OF_DAY, 0);
        event.set(Calendar.MINUTE, 0);
        event.set(Calendar.SECOND, 0);
        event.set(Calendar.MILLISECOND, 0);

        return !event.before(today);
    }

    /* ------------------------------------------------------------------
     * Request / session helpers
     * ------------------------------------------------------------------ */

    public static User getLoggedUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return (User) session.getAttribute("user");
    }

    public static Admin getLoggedAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return (Admin) session.getAttribute("admin");
    }

    public static Integer getIntParameter(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        if (isBlank(value)) {
            return null;
        }
        try {
            return Integer.valueOf(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static void setSuccessMessage(HttpSession session, String message) {
        session.setAttribute("successMessage", message);
    }

    public static void setErrorMessage(HttpSession session, String message) {
        session.setAttribute("errorMessage", message);
    }

    /**
     * Returns the file name of the image that should be shown. When the
     * record has no uploaded image a placeholder image is used.
     */
    public static String imageUrl(String contextPath, String imageName, String placeholder) {
        if (isBlank(imageName)) {
            return contextPath + "/images/" + placeholder;
        }
        return contextPath + "/uploads/" + imageName;
    }

    public static String eventImageUrl(String contextPath, String imageName) {
        return imageUrl(contextPath, imageName, "event-placeholder.jpg");
    }

    public static String advertisementImageUrl(String contextPath, String imageName) {
        return imageUrl(contextPath, imageName, "advert-placeholder.jpg");
    }
}
