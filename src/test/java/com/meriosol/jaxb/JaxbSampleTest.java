package com.meriosol.jaxb;

import com.meriosol.jaxb.pojo.ObjectFactory;
import com.meriosol.jaxb.pojo.Sample;
import org.junit.Test;

import java.io.File;
import java.util.Date;
import java.util.logging.Logger;

import static org.junit.Assert.assertNotNull;

/**
 * Tests marshalling/unmarshalling of sample entity.
 *
 * @author meriosol
 * @version 0.1
 * @since 06/04/14
 */
public class JaxbSampleTest {
    private static final Class<JaxbSampleTest> MODULE = JaxbSampleTest.class;
    private static final Logger lOG = Logger.getLogger(MODULE.getName());

    private static final String SAMPLE_SCHEMA_NS = "http://com/meriosol/sample/schema";
    private static final String SAMPLE_SCHEMA_URL = "sample.xsd";

    @Test
    public void testCorrectSampleMarshalling() {
        String outputDir = System.getProperty("java.io.tmpdir");
        String eventOutputFileName = "sample.out.xml";
        File file = new File(outputDir + File.separator + eventOutputFileName);

        SampleBuilder sampleBuilder = new SampleBuilder();
        Sample sample = sampleBuilder.createSampleEvent(1001, "Sample");

        boolean validationErrorTolerant = false;
        String xmlSchemaResourceUrl = SAMPLE_SCHEMA_URL;
        boolean formattedOutput = true;
        String xmlSchemaNameSpace = SAMPLE_SCHEMA_NS;
        MarshallHelper<Sample> sampleMarshallHelper = new MarshallHelper<>(validationErrorTolerant, xmlSchemaResourceUrl
                , formattedOutput, xmlSchemaNameSpace);
        sampleMarshallHelper.marshall(file, sample);

        lOG.info("Sample POJO was marshalled into file: " + file.getAbsolutePath());
    }

    @Test
    public void testNoSchemaAndNamespaceSampleMarshalling() {
        String outputDir = System.getProperty("java.io.tmpdir");
        String eventOutputFileName = "sample.NoSchemaAndNamespace.out.xml";
        File file = new File(outputDir + File.separator + eventOutputFileName);

        SampleBuilder sampleBuilder = new SampleBuilder();
        Sample sample = sampleBuilder.createSampleEvent(1006, "SampleNoSchemaAndNamespace");

        boolean validationErrorTolerant = true;
        String xmlSchemaResourceUrl = null;
        boolean formattedOutput = true;
        String xmlSchemaNameSpace = null;
        MarshallHelper<Sample> sampleMarshallHelper = new MarshallHelper<>(validationErrorTolerant, xmlSchemaResourceUrl
                , formattedOutput, xmlSchemaNameSpace);
        sampleMarshallHelper.marshall(file, sample);

        lOG.info("Sample POJO was marshalled into file: " + file.getAbsolutePath());
    }

    @Test
    public void testNoSchemaAndWrongNamespaceSampleMarshalling() {
        String outputDir = System.getProperty("java.io.tmpdir");
        String eventOutputFileName = "sample.NoSchemaAndWrongNamespace.out.xml";
        File file = new File(outputDir + File.separator + eventOutputFileName);

        SampleBuilder sampleBuilder = new SampleBuilder();
        Sample sample = sampleBuilder.createSampleEvent(1008, "SampleNoSchemaAndNamespace");

        boolean validationErrorTolerant = true;
        String xmlSchemaResourceUrl = null;
        boolean formattedOutput = true;
        String xmlSchemaNameSpace = "incorrect_namespace";
        MarshallHelper<Sample> sampleMarshallHelper = new MarshallHelper<>(validationErrorTolerant, xmlSchemaResourceUrl
                , formattedOutput, xmlSchemaNameSpace);
        sampleMarshallHelper.marshall(file, sample);

        lOG.info("Sample POJO was marshalled into file: " + file.getAbsolutePath());
    }

    @Test
    public void testNonexistentSchemaAndWrongNamespaceSampleMarshalling() {
        String outputDir = System.getProperty("java.io.tmpdir");
        String eventOutputFileName = "sample.NonexistentSchemaAndWrongNamespace.out.xml";
        File file = new File(outputDir + File.separator + eventOutputFileName);

        SampleBuilder sampleBuilder = new SampleBuilder();
        Sample sample = sampleBuilder.createSampleEvent(1009, "NonexistentSchemaAndWrongNamespace");

        boolean validationErrorTolerant = true;
        String xmlSchemaResourceUrl = "nonexistent_sample_schema_path.xsd";
        boolean formattedOutput = true;
        String xmlSchemaNameSpace = "incorrect_namespace";
        MarshallHelper<Sample> sampleMarshallHelper = new MarshallHelper<>(validationErrorTolerant, xmlSchemaResourceUrl
                , formattedOutput, xmlSchemaNameSpace);
        sampleMarshallHelper.marshall(file, sample);

        lOG.info("Sample POJO was marshalled into file: " + file.getAbsolutePath());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testSampleAllNullMarshalling() {
        File file = null;
        Sample sample = null;

        boolean validationErrorTolerant = false;
        String xmlSchemaResourceUrl = null;
        boolean formattedOutput = true;
        String xmlSchemaNameSpace = null;
        MarshallHelper<Sample> sampleMarshallHelper = new MarshallHelper<>(validationErrorTolerant, xmlSchemaResourceUrl
                , formattedOutput, xmlSchemaNameSpace);
        sampleMarshallHelper.marshall(file, sample);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testSampleFileNullMarshalling() {
        File file = null;
        SampleBuilder sampleBuilder = new SampleBuilder();
        Sample sample = sampleBuilder.createSampleEvent(1004, "SampleFileNull");

        boolean validationErrorTolerant = false;
        String xmlSchemaResourceUrl = SAMPLE_SCHEMA_URL;
        boolean formattedOutput = true;
        String xmlSchemaNameSpace = SAMPLE_SCHEMA_NS;
        MarshallHelper<Sample> sampleMarshallHelper = new MarshallHelper<>(validationErrorTolerant, xmlSchemaResourceUrl
                , formattedOutput, xmlSchemaNameSpace);
        sampleMarshallHelper.marshall(file, sample);
    }


    @Test
    public void testSampleUnmarshalling() {
        String resourcePath = "sample.xml";

        UnmarshallHelper<Sample> sampleUnmarshallHelper = new UnmarshallHelper<>();
        sampleUnmarshallHelper.setXmlSchemaResourceUrl(SAMPLE_SCHEMA_URL);
        Sample sample = sampleUnmarshallHelper.unmarshallFromResourcePath(resourcePath, Sample.class);
        assertNotNull(String.format("Sample POJO loaded from resource '%s' should be not null", resourcePath), sample);
        lOG.info(String.format("Unmarshalled sample POJO info: ID='%s', title='%s', created='%s'.", sample.getId(), sample.getTitle(), sample.getCreated()));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testNonexistentSampleUnmarshalling() {
        String resourcePath = "sample_no_existent_in_universe_679304738235463.xml";
        UnmarshallHelper<Sample> sampleUnmarshallHelper = new UnmarshallHelper<>();
        Sample sample = sampleUnmarshallHelper.unmarshallFromResourcePath(resourcePath, Sample.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSampleUnmarshalling() {
        String resourcePath = null;
        UnmarshallHelper<Sample> sampleUnmarshallHelper = new UnmarshallHelper<>();
        Sample sample = sampleUnmarshallHelper.unmarshallFromResourcePath(resourcePath, Sample.class);
    }
    //---------------------------------------
    // Utils:

    /**
     * Basic sample entity builder.
     */
    private class SampleBuilder {
        private ObjectFactory objectFactory = new ObjectFactory();

        Sample createSampleEvent(long id, String title) {
            Sample sample = objectFactory.createSample();
            sample.setId(id);
            sample.setTitle(title);
            sample.setCreated(XMLCalendarConverter.convertToXmlDate(new Date()));
            return sample;
        }
    }

}
