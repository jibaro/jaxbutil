package com.meriosol.jaxb;

import org.xml.sax.SAXException;

import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 * Common utils used in a few classes of this package.<br>
 * NOTE: it's not in OO style and ideally they should be moved to corresponding classes.
 *
 * @author meriosol
 * @version 0.1
 * @since 06/04/14
 */
public class JaxbUtils {
    private static final Class<JaxbUtils> MODULE = JaxbUtils.class;
    private static final Logger LOG = Logger.getLogger(MODULE.getName());

    private JaxbUtils() {
    }

    /**
     * @return XML schema object for validation
     */
    public static Schema loadXmlSchema(String xmlSchemaResourceUrlStr) {
        final SchemaFactory sfSchemaFactory = SchemaFactory
                .newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
        if (sfSchemaFactory == null) {
            throw new IllegalArgumentException("[JU875673622] SchemaFactory should not be null!");
        }
        if (xmlSchemaResourceUrlStr == null || "".equals(xmlSchemaResourceUrlStr)) {
            throw new IllegalArgumentException("[JU19033223] XML schema URL should not be null or empty!");
        }
        final URL xmlSchemaResourceUrl = getResourceURL(xmlSchemaResourceUrlStr);
        if (xmlSchemaResourceUrl == null) {
            throw new IllegalArgumentException("[JU74527197] Schema URL is null for xml schema " + xmlSchemaResourceUrl);
        }
        Schema schema = null;
        try {
            schema = sfSchemaFactory.newSchema(xmlSchemaResourceUrl);
        } catch (final SAXException e) {
            throw new JaxbRuntimeException("[JU7432622432] Error while parsing schema '" + xmlSchemaResourceUrl + "'", e);
        }
        return schema;
    }

    /**
     * @param resourcePath
     * @return InputStream for <code>resourcePath</code>.
     */
    public static InputStream getResourceInputStream(String resourcePath) {
        InputStream inputStream = null;
        final URL url = getResourceURL(resourcePath);
        if (url == null) {
            LOG.warning("URL is null for resource " + resourcePath);
        } else {
            try {
                inputStream = url.openStream();
            } catch (IOException e) {
                throw new JaxbRuntimeException("[JU68832239] ",e);
            }
        }

        return inputStream;
    }

    /**
     *
     * @param resourcePath
     * @return URL loaded using module class loader.
     */
    public static URL getResourceURL(String resourcePath) {
        if (resourcePath == null) {
            throw new IllegalArgumentException("[JU165035211] ResourcePath should not be null!");
        }
        return MODULE.getClassLoader().getResource(resourcePath);
    }

    /**
     * Joins elements of <code>collection</code> using <code>delimiter</code>.<br>
     * NOTE: apache commons collection package has good utils for that. Only to avoid excessive dependencies that package was not included.
     *
     * @param collection Objects to join their string representation.
     * @param delimiter  Delimiter to use while joining
     * @param <T>        What sort of collection element to expect
     * @return Joined collection. For example: (collection = ['a','b','c'], delimiter =";") => 'a;b;c'.
     */
    public static <T> String join(final Collection<T> collection, String delimiter) {
        String result = "";
        if (collection != null && !collection.isEmpty()) {
            final Collection<T> unmodifiableCollection = Collections.unmodifiableCollection(collection);
            final Iterator<T> iterator = unmodifiableCollection.iterator();
            if (iterator.hasNext()) {
                StringBuilder stringBuilder = new StringBuilder(String.valueOf(iterator.next()));
                while (iterator.hasNext()) {
                    stringBuilder.append(delimiter).append(String.valueOf(iterator.next()));
                }
                result = stringBuilder.toString();
            }
        }
        return result;
    }


}
