package com.meriosol.jaxb;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventLocator;
import javax.xml.bind.util.ValidationEventCollector;
import java.util.ArrayList;
import java.util.List;

/**
 * Gathers validation errors and returns assembled error message.
 * @author meriosol
 * @version 0.1
 * @since 05/04/14
 */
class CollectingValidationEventHandler extends ValidationEventCollector {
    /**
     * @return Validation errors assembled info.
     */
    String getCombinedEventsMessage() {
        final StringBuilder stringBuilder = new StringBuilder();
        final ValidationEvent[] validationEvents = getEvents();
        if ((validationEvents != null) && (validationEvents.length > 0)) {
            final List<String> eventMessages = new ArrayList<>(validationEvents.length);
            for (final ValidationEvent validationEvent : validationEvents) {
                final String severityLevel = getSeverityName(validationEvent.getSeverity());
                final ValidationEventLocator locator = validationEvent.getLocator();
                final String eventCoords = "Column is '" + locator.getColumnNumber()
                        + "' at line number '" + locator.getLineNumber() + "'.";
                eventMessages.add(" ** Message: [" + severityLevel + "] "
                        + validationEvent.getMessage() + ". " + eventCoords);
            }
            stringBuilder.append("Messages: \n" + JaxbUtils.join(eventMessages, "\n")
                    + "\n ==========");
        }
        return stringBuilder.toString();
    }

    /**
     *
     * @param severityLevel
     * @return String value of severity constant (use it for better readability).
     */
    private String getSeverityName(int severityLevel) {
        String severityName = "NA";
        switch (severityLevel) {
            case ValidationEvent.WARNING:
                severityName = "WARNING";
                break;
            case ValidationEvent.ERROR:
                severityName = "ERROR";
                break;
            case ValidationEvent.FATAL_ERROR:
                severityName = "FATAL_ERROR";
                break;
            default:
                break;
        }
        return severityName;
    }

}
