package com.meriosol.jaxb;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

/**
 * Helps to serialize(marshall) JAXB POJO model into XML.<br>
 * NOTE1: You need to generate JAXB classes from XSD first (or write them yourself) before using this class.<br>
 * NOTE2: Bind generics param T is particular JAXB class marshalling from is going te be used.
 *
 * @author meriosol
 * @version 0.1
 * @since 06/04/14
 */
public class MarshallHelper<T> extends MarshallingHelperBase {
    private static final Class<MarshallHelper> MODULE = MarshallHelper.class;
    private static final Logger LOG = Logger.getLogger(MODULE.getName());

    private String xmlSchemaNameSpace;
    private boolean formattedOutput;

    public MarshallHelper() {
        super();
    }

    /**
     * @param validationErrorTolerant
     */
    public MarshallHelper(boolean validationErrorTolerant) {
        super(validationErrorTolerant, null);
    }

    /**
     * @param xmlSchemaResourceUrl
     */
    public MarshallHelper(String xmlSchemaResourceUrl) {
        super(xmlSchemaResourceUrl);
    }

    public MarshallHelper(boolean validationErrorTolerant, String xmlSchemaResourceUrl) {
        super(validationErrorTolerant, xmlSchemaResourceUrl);
    }

    public MarshallHelper(String xmlSchemaNameSpace, boolean formattedOutput) {
        super();
        this.formattedOutput = formattedOutput;
        this.xmlSchemaNameSpace = xmlSchemaNameSpace;
    }

    public MarshallHelper(boolean validationErrorTolerant, String xmlSchemaResourceUrl
            , boolean formattedOutput, String xmlSchemaNameSpace) {
        super(validationErrorTolerant, xmlSchemaResourceUrl);
        this.formattedOutput = formattedOutput;
        this.xmlSchemaNameSpace = xmlSchemaNameSpace;
    }

    /**
     * @param filePath
     * @param entity
     * @throws JaxbRuntimeException
     */
    public void marshall(String filePath, T entity)
            throws JaxbRuntimeException {
        if (entity == null) {
            throw new IllegalArgumentException("[JU492934122] Entity marshalling should not be null!");
        }
        marshall(filePath, getJAXBContext(entity.getClass()), entity);
    }

    /**
     * @param file
     * @param entity
     * @throws JaxbRuntimeException
     */
    public void marshall(File file, T entity)
            throws JaxbRuntimeException {
        if (entity == null) {
            throw new IllegalArgumentException("[JU841231130] Entity marshalling should not be null!");
        }

        marshall(file, getJAXBContext(entity.getClass()), entity);
    }

    /**
     * @param filePath
     * @param entityJaxbContext
     * @param entity
     * @throws JaxbRuntimeException
     */
    public void marshall(String filePath, JAXBContext entityJaxbContext, T entity)
            throws JaxbRuntimeException {
        if (filePath == null || "".equals(filePath)) {
            throw new IllegalArgumentException("[JU] FilePath for marshalling should not be null or empty!");
        }
        marshall(new File(filePath), entityJaxbContext, entity);
    }

    /**
     * @param file
     * @param entityJaxbContext
     * @param entity
     * @throws JaxbRuntimeException
     */
    public void marshall(File file, JAXBContext entityJaxbContext, T entity)
            throws JaxbRuntimeException {
        if (file == null) {
            throw new IllegalArgumentException("[JU72932651] File for entity marshalling should not be null!");
        }
        if (entityJaxbContext == null) {
            throw new IllegalArgumentException("[JU543225402] JAXBContext for entity marshalling should not be null!");
        }
        if (entity == null) {
            throw new IllegalArgumentException("[JU68468773] Entity for marshalling should not be null!");
        }
        try {
            marshall(new FileOutputStream(file), entityJaxbContext, entity);
        } catch (FileNotFoundException e) {
            throw new JaxbRuntimeException(
                    String.format("FileNotFoundException error occurred while marshall entity of class '%s' into file '%s'. Message: %s"
                            , entity.getClass().getName(), file.getAbsolutePath(), e.getMessage()), e
            );
        }
    }

    /**
     * @param outputStream
     * @param entityJaxbContext
     * @throws FileNotFoundException
     */
    public void marshall(OutputStream outputStream, JAXBContext entityJaxbContext, T entity)
            throws JaxbRuntimeException {
        if (outputStream == null) {
            throw new IllegalArgumentException("[JU97908332] OutputStream for entity marshalling should not be null!");
        }
        if (entityJaxbContext == null) {
            throw new IllegalArgumentException("[JU76784933] JAXBContext for entity marshalling should not be null!");
        }
        if (entity == null) {
            throw new IllegalArgumentException("[JU312936345] Entity for marshalling should not be null!");
        }

        final Marshaller marshaller;
        try {
            marshaller = entityJaxbContext.createMarshaller();
            final CollectingValidationEventHandler eventHandler = new CollectingValidationEventHandler();
            marshaller.setEventHandler(eventHandler);

            if (!isValidationErrorTolerant()) {
                loadXmlSchema();
                marshaller.setSchema(getXmlSchema());
            }

            // TODO: add schema validation..
            if (this.xmlSchemaNameSpace != null && getXmlSchemaResourceUrl() != null) {
                marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, this.xmlSchemaNameSpace + " "
                        + getXmlSchemaResourceUrl());
            }

            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, this.formattedOutput);
            marshaller.marshal(entity, outputStream);

            // Validate marshall
            final String marshallCombinedEventsMessage = eventHandler.getCombinedEventsMessage();
            if (!"".equals(marshallCombinedEventsMessage)) {
                if (isValidationErrorTolerant()) {
                    LOG.warning("There are warns/errors while marshalling: [[ " + marshallCombinedEventsMessage + " ]]");
                } else {
                    throw new JaxbRuntimeException("[JU932763254] " + marshallCombinedEventsMessage);
                }
            }
        } catch (JAXBException e) {
            throw new JaxbRuntimeException(
                    String.format("[JU42523482] Error occurred while marshalling entity of class '%s'. Message: %s"
                            , entity.getClass().getName(), e.getMessage()), e
            );

        }

    }

    public String getXmlSchemaNameSpace() {
        return xmlSchemaNameSpace;
    }

    public void setXmlSchemaNameSpace(String xmlSchemaNameSpace) {
        this.xmlSchemaNameSpace = xmlSchemaNameSpace;
    }

    public boolean isFormattedOutput() {
        return formattedOutput;
    }

    public void setFormattedOutput(boolean formattedOutput) {
        this.formattedOutput = formattedOutput;
    }


}
