package config;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Validation {
        private static final int MAX_TITLE_LENGTH = 50;
        private static final int MAX_CONTENT_LENGTH = 1000;

        public static boolean isValidNoteTitle(String title) {
            return title != null &&
                    !title.trim().isEmpty() &&
                    title.length() <= MAX_TITLE_LENGTH;
        }

        public static boolean isValidNoteContent(String content) {
            return content != null &&
                    !content.trim().isEmpty() &&
                    content.length() <= MAX_CONTENT_LENGTH;
        }

        public static boolean isValidDate(String dateStr) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate date = LocalDate.parse(dateStr, formatter);
                return !date.isBefore(LocalDate.now());
            } catch (DateTimeParseException e) {
                return false;
            }
        }
}
