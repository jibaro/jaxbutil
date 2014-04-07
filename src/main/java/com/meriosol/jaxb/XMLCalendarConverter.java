package com.meriosol.jaxb;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Date converter. Can be used in JAXB (un-)marshalling.<br>
 *
 * @author meriosol
 * @version 0.1
 * @since 06/04/14
 */
public class XMLCalendarConverter {
    /**
     * @param date
     * @return XML Gregorian Calendar
     */
    public static XMLGregorianCalendar convertToXmlDate(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("[JU8694253] Date should not be null!");
        }

        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(date);
        XMLGregorianCalendar xmlGregorianCalendar;
        try {
            xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
        } catch (DatatypeConfigurationException e) {
            throw new JaxbRuntimeException(
                    String.format("[JU92833357] Error occurred while converting date '%s' into XML one! Message: %s"
                            , date, e.getMessage()), e
            );
        }
        return xmlGregorianCalendar;
    }

    /**
     * @param xmlGregorianCalendar
     * @return Date
     */
    public static Date convertToDate(XMLGregorianCalendar xmlGregorianCalendar) {
        if (xmlGregorianCalendar == null) {
            throw new IllegalArgumentException("[JU4932534] XMLGregorianCalendar should not be null!");
        }

        GregorianCalendar gregorianCalendar = xmlGregorianCalendar.toGregorianCalendar();
        return gregorianCalendar.getTime();
    }
}
