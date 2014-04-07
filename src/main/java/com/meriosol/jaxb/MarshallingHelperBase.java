package com.meriosol.jaxb;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.validation.Schema;

/**
 * Common part of JAXB [XML / Object POJO] handling ((un-)marshall).<br>
 *
 * @author meriosol
 * @version 0.1
 * @since 06/04/14
 */
class MarshallingHelperBase {
    private static final boolean DEFAULT_VALIDATION_ERR_TOLERANCE = true;

    private boolean validationErrorTolerant;
    private String xmlSchemaResourceUrl;
    private Schema xmlSchema;

    public MarshallingHelperBase() {
        this(DEFAULT_VALIDATION_ERR_TOLERANCE, null);
    }

    /**
     * @param validationErrorTolerant
     */
    public MarshallingHelperBase(boolean validationErrorTolerant) {
        this(validationErrorTolerant, null);
    }

    /**
     * @param xmlSchemaResourceUrl
     */
    public MarshallingHelperBase(String xmlSchemaResourceUrl) {
        this(DEFAULT_VALIDATION_ERR_TOLERANCE, xmlSchemaResourceUrl);
    }

    public MarshallingHelperBase(boolean validationErrorTolerant, String xmlSchemaResourceUrl) {
        this.validationErrorTolerant = validationErrorTolerant;
        this.xmlSchemaResourceUrl = xmlSchemaResourceUrl;
    }

    /**
     * @param clazz JAXB generated class.
     * @return JAXB context. Avoid using it directly to eliminate dependencies from JAXB API.
     */
    public static JAXBContext getJAXBContext(Class clazz) throws JaxbRuntimeException {
        try {
            return JAXBContext.newInstance(clazz);
        } catch (JAXBException e) {
            throw new JaxbRuntimeException(
                    String.format("[JU904826543] Error occurred while getting JAXBContext for class '%s'. Message: %s"
                            , clazz.getName(), e.getMessage()), e
            );
        }
    }

    /**
     * @return If true system won't check XML
     */
    public boolean isValidationErrorTolerant() {
        return validationErrorTolerant;
    }

    public void setValidationErrorTolerant(boolean validationErrorTolerant) {
        this.validationErrorTolerant = validationErrorTolerant;
    }

    /**
     * @return Schema name (assuming schema in classpath)
     */
    public String getXmlSchemaResourceUrl() {
        return xmlSchemaResourceUrl;
    }

    protected Schema getXmlSchema() {
        return xmlSchema;
    }

    /**
     * NOTE: once new name set, old XML schema cache will be invalidated.
     * New value will be loaded once client enforces validation and unmarshalls 1st time after it.
     *
     * @param xmlSchemaResourceUrl
     */
    public void setXmlSchemaResourceUrl(String xmlSchemaResourceUrl) {
        this.xmlSchemaResourceUrl = xmlSchemaResourceUrl;
        this.xmlSchema = null; // Reset XML validation schema
    }

    /**
     * One time validation schema lazy loading
     */
    protected void loadXmlSchema() {
        // Lazy load of schema:
        if (this.xmlSchema == null) {
            this.xmlSchema = JaxbUtils.loadXmlSchema(this.xmlSchemaResourceUrl);
        }
    }

}
