package config;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Provides static utility methods for performing common data validation checks.
 * <p>
 * This class centralizes validation logic for various inputs, such as checking
 * string lengths against predefined limits and validating date formats and constraints
 * (e.g., ensuring an end date is not in the past). Most methods are static and
 * intended to be used directly without instantiating the class.
 * </p>
 *
 * @version 1.0
 * @since 2025-04-23
 * @author Louis Bertrand Ntwali
 */
public class Validation {

    /**
     * Defines the maximum allowable length for titles, measured in characters.
     * Currently set to 50.
     */
    private static final int MAX_TITLE_LENGTH = 50;

    /**
     * Defines the maximum allowable length for note content, measured in characters.
     * Currently set to 1000.
     */
    private static final int MAX_CONTENT_LENGTH = 1000;

    /**
     * Returns the configured maximum allowable length for titles.
     * Requires an instance of Validation to be called, despite returning a static constant.
     *
     * @return The value of {@link #MAX_TITLE_LENGTH}.
     * @see #MAX_TITLE_LENGTH
     */
    public static int getMaxTitleLength() {
        return MAX_TITLE_LENGTH;
    }

    /**
     * Returns the configured maximum allowable length for note content.
     * Requires an instance of Validation to be called, despite returning a static constant.
     *
     * @return The value of {@link #MAX_CONTENT_LENGTH}.
     * @see #MAX_CONTENT_LENGTH
     */
    public static int getMaxContentLength() {
        return MAX_CONTENT_LENGTH;
    }

    /**
     * Checks if a given string is null, effectively empty after trimming whitespace,
     * or exceeds a specified maximum length.
     * <p>
     * This is a general-purpose helper method used by more specific validation checks.
     * A string is considered invalid if: </p>
     * <ul>
     * <li>It is {@code null}.</li>
     * <li>Its trimmed version (leading/trailing whitespace removed) is empty.</li>
     * <li>Its original length (before trimming) is strictly greater than {@code maxLength}.</li>
     * </ul>
     *
     * @param input The string to validate. Can be {@code null}.
     * @param maxLength The maximum permitted length for the string (exclusive of trimming).
     * Must be non-negative.
     * @return {@code true} if the input string is null, empty/whitespace-only after trimming,
     * or its original length is greater than {@code maxLength}; {@code false} otherwise.
     */
    public static boolean isEmptyOrTooLong(String input, int maxLength) {
        if (input == null) {
            return true;
        }
        return input.trim().isEmpty() || input.length() > maxLength;
    }

    /**
     * Validates a title string based on predefined constraints.
     * <p>
     * A title is considered invalid if it is null, effectively empty (after trimming whitespace),
     * or its length exceeds the {@link #MAX_TITLE_LENGTH}. This method delegates the core logic
     * to {@link #isEmptyOrTooLong(String, int)}.
     * </p>
     *
     * @param title The title string to validate. Can be {@code null}.
     * @return {@code true} if the title is invalid (null, empty/whitespace-only, or too long);
     * {@code false} if the title is valid according to the length constraints.
     * @see #isEmptyOrTooLong(String, int)
     * @see #MAX_TITLE_LENGTH
     */
    public static boolean isInvalidTitle(String title) {
        return isEmptyOrTooLong(title, MAX_TITLE_LENGTH);
    }

    /**
     * Validates the content of a note based on predefined constraints.
     * <p>
     * Note content is considered invalid if it is null, effectively empty (after trimming whitespace),
     * or its length exceeds the {@link #MAX_CONTENT_LENGTH}. This method delegates the core logic
     * to {@link #isEmptyOrTooLong(String, int)}.
     * </p>
     *
     * @param content The note content string to validate. Can be {@code null}.
     * @return {@code true} if the note content is invalid (null, empty/whitespace-only, or too long);
     * {@code false} if the note content is valid according to the length constraints.
     * @see #isEmptyOrTooLong(String, int)
     * @see #MAX_CONTENT_LENGTH
     */
    public static boolean isInvalidNoteContent(String content) {
        return isEmptyOrTooLong(content, MAX_CONTENT_LENGTH);
    }

    /**
     * Validates if a given string represents a valid end date, checking both format and temporal constraint.
     * <p>
     * A valid end date must meet two criteria: </p>
     * <ol>
     * <li>It must be parseable according to the specific date format "yyyy-MM-dd". Leading/trailing
     * whitespace in the input string is ignored during parsing.</li>
     * <li>The parsed date must represent the current date (today) or a date in the future.
     * Past dates are considered invalid.</li>
     * </ol>
     * <p> If the date string cannot be parsed using the specified format (due to incorrect format or
     * invalid date values like "2023-02-30"), a {@link DateTimeParseException} is caught internally.
     * When a parsing error occurs, an error message detailing the failure is printed to
     * {@link System#out}, and the method returns {@code false}.
     * </p>
     *
     * @param endDate The date string to validate, ideally in "yyyy-MM-dd" format. Can be {@code null}.
     * Whitespace around the date string will be trimmed before parsing attempt.
     * @return {@code true} if the string is not null, represents a valid date in "yyyy-MM-dd" format
     * after trimming, and the date is not before the current system date;
     * {@code false} otherwise (if null, format is incorrect, date is invalid, or date is in the past).
     * @see java.time.format.DateTimeFormatter#ofPattern(String)
     * @see java.time.LocalDate#parse(CharSequence, java.time.format.DateTimeFormatter)
     * @see java.time.LocalDate#now()
     * @see java.time.LocalDate#isBefore(java.time.chrono.ChronoLocalDate)
     * @see java.time.format.DateTimeParseException
     */
    public static boolean isValidEndDate(String endDate) {
        if (endDate == null) {
            System.out.println("End date validation failed: Input string is null.");
            return false;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate date = LocalDate.parse(endDate.trim(), formatter);
            // Note: Uses system default time-zone for LocalDate.now()
            return !date.isBefore(LocalDate.now());
        }  catch (DateTimeParseException e) {
            System.out.println("Failed to parse the date string: \"" + endDate.trim() +
                    "\". Ensure format is yyyy-MM-dd. Error: " + e.getMessage());
            return false;
        }
    }
}