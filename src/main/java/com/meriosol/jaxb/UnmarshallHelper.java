package com.meriosol.jaxb;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.logging.Logger;

/**
 * Helps in loading XML into Object POJO model (unmarshall) using JAXB.<br>
 * NOTE1: You need to generate JAXB classes from XSD first (or write them yourself) before using this class.<br>
 * NOTE2: Bind generics param T is particular JAXB class unmarshalling to is going te be used.
 *
 * @author meriosol
 * @version 0.1
 * @since 06/04/14
 */
public class UnmarshallHelper<T> extends MarshallingHelperBase {
    private static final Class<UnmarshallHelper> MODULE = UnmarshallHelper.class;
    private static final Logger LOG = Logger.getLogger(MODULE.getName());

    public UnmarshallHelper() {
        super();
    }

    /**
     * @param validationErrorTolerant
     */
    public UnmarshallHelper(boolean validationErrorTolerant) {
        super(validationErrorTolerant, null);
    }

    /**
     * @param xmlSchemaResourceUrl
     */
    public UnmarshallHelper(String xmlSchemaResourceUrl) {
        super(xmlSchemaResourceUrl);
    }

    public UnmarshallHelper(boolean validationErrorTolerant, String xmlSchemaResourceUrl) {
        super(validationErrorTolerant, xmlSchemaResourceUrl);
    }


    /**
     * NOTE: This method totally encapsulates internals of JAXB. Highly recommended to be used in clients.
     *
     * @param resourcePath
     * @param entityClass
     * @return unmarshalled entity
     * @throws JaxbRuntimeException
     */
    public T unmarshallFromResourcePath(String resourcePath, Class<T> entityClass)
            throws JaxbRuntimeException {
        return unmarshall(JaxbUtils.getResourceInputStream(resourcePath), getJAXBContext(entityClass));
    }

    /**
     * @param filePath
     * @param entityClass
     * @return unmarshalled entity
     * @throws JaxbRuntimeException
     */
    public T unmarshallFromFilePath(String filePath, Class<T> entityClass)
            throws JaxbRuntimeException {
        return unmarshall(new File(filePath), entityClass);
    }

    /**
     * @param resourcePath
     * @param entityJaxbContext
     * @return unmarshalled entity
     * @throws JaxbRuntimeException
     */
    public T unmarshallFromResourcePath(String resourcePath, JAXBContext entityJaxbContext)
            throws JaxbRuntimeException {
        return unmarshall(JaxbUtils.getResourceInputStream(resourcePath), entityJaxbContext);
    }

    /**
     * @param file
     * @param entityClass
     * @return unmarshalled entity
     * @throws JaxbRuntimeException
     */
    public T unmarshall(File file, Class<T> entityClass)
            throws JaxbRuntimeException {
        return unmarshall(file, getJAXBContext(entityClass));
    }

    /**
     * @param file
     * @param entityJaxbContext
     * @return unmarshalled entity
     * @throws JaxbRuntimeException
     */
    public T unmarshall(File file, JAXBContext entityJaxbContext)
            throws JaxbRuntimeException {
        if (file == null) {
            throw new IllegalArgumentException("[JU342545] File for entity unmarshalling should not be null!");
        }
        if (entityJaxbContext == null) {
            throw new IllegalArgumentException("[JU980321] JAXBContext for entity unmarshalling should not be null!");
        }
        try {
            return unmarshall(new FileInputStream(file), entityJaxbContext);
        } catch (FileNotFoundException e) {
            throw new JaxbRuntimeException(
                    String.format("[JU46325648] FileNotFoundException error occurred while unmarshalling from file '%s'. Message: %s"
                            , file.getAbsolutePath(), e.getMessage()), e
            );
        }
    }

    /**
     * @param inputStream
     * @param entityClass
     * @return unmarshalled entity
     * @throws JaxbRuntimeException
     */
    public T unmarshall(InputStream inputStream, Class<T> entityClass)
            throws JaxbRuntimeException {
        if (inputStream == null) {
            throw new IllegalArgumentException("[JU9045674] InputStream for entity unmarshalling should not be null!");
        }
        if (entityClass == null) {
            throw new IllegalArgumentException("[JU3921744] Entity class for unmarshalling should not be null!");
        }

        return unmarshall(inputStream, getJAXBContext(entityClass));
    }

    /**
     * @param inputStream
     * @param entityJaxbContext
     * @return unmarshalled entity
     * @throws JaxbRuntimeException
     */
    public T unmarshall(InputStream inputStream, JAXBContext entityJaxbContext)
            throws JaxbRuntimeException {
        if (inputStream == null) {
            throw new IllegalArgumentException("[JU32431001] InputStream for entity unmarshalling should not be null!");
        }
        if (entityJaxbContext == null) {
            throw new IllegalArgumentException("[JU647299] JAXBContext for entity unmarshalling should not be null!");
        }

        T entity;
        final Unmarshaller unmarshaller;
        try {
            unmarshaller = entityJaxbContext.createUnmarshaller();
            final CollectingValidationEventHandler eventHandler = new CollectingValidationEventHandler();
            unmarshaller.setEventHandler(eventHandler);

            if (!isValidationErrorTolerant()) {
                loadXmlSchema();
                unmarshaller.setSchema(getXmlSchema());
            }

            Object entityElementObj;
            entityElementObj = unmarshaller.unmarshal(inputStream);
            // Validate unmarshall
            final String unmarshallCombinedEventsMessage = eventHandler.getCombinedEventsMessage();
            if (!"".equals(unmarshallCombinedEventsMessage)) {
                if (isValidationErrorTolerant()) {
                    LOG.warning("There are warns/errors while unmarshalling: [[ " + unmarshallCombinedEventsMessage + " ]]");
                } else {
                    throw new JaxbRuntimeException("[JU547445635] " + unmarshallCombinedEventsMessage);
                }
            }

            entity = (T) (entityElementObj instanceof JAXBElement ? ((JAXBElement<?>) entityElementObj)
                    .getValue() : entityElementObj);
        } catch (JAXBException e) {
            throw new JaxbRuntimeException(
                    String.format("[JU98474554] Error occurred while unmarshalling entity. Message: %s"
                            , e.getMessage()), e
            );

        }

        return entity;
    }


}
