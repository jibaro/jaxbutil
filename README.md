JAXB Util
========

Inception year: <tt>2014</tt>

## Overview
This library is thin wrapper around JAXB marshallers. It relieves client code from access into JAXB internals.
You only need to generate JAXB POJOs from XSD (or write annotated classes as it's shown in tests) and start using helpers from this repo.

## Dependencies
TO be the least obtrusive this library was specifically designed with 0 external dependencies. Only standard JDK (SE) features are used.

## Usage
Only **MarshallHelper** and **UnmarshallHelper** are need to be looked at. Other classes supplement these 2.
Unit test **JaxbSampleTest** shows basic scenarios for loading and storing XMLs(unmarshall/marshall in JAXB terms).
Main trick applied in this library is generics binding for JAXB (generated) POJOs. You can notice **<T>** in class declarations.
You substitute **T** to your POJO when make instance of helper class.
For instance, unmarshalling XML element **sample** into POJO **Sample** looks like:
```Java
String resourcePath = "sample.xml";
UnmarshallHelper<Sample> sampleUnmarshallHelper = new UnmarshallHelper<>();
sampleUnmarshallHelper.setXmlSchemaResourceUrl(SAMPLE_SCHEMA_URL);
Sample sample = sampleUnmarshallHelper.unmarshallFromResourcePath(resourcePath, Sample.class);
```
As you see code deals with only JAXB POJOs, strings and helper classes.

### Settings
As you see in sample above, you can e.g. set XSD URL. It can be set in constructor as well, along with a few more parameters.
Common settings can be found in base helper class **MarshallingHelperBase**. Short review:
 - **validationErrorTolerant**: If true, despite on validation errors operation will be finished.
 - **xmlSchemaResourceUrl**: It's assumed(maybe not always true) XSD is to be loaded via URL.
       For simplicity you just add XSD in classpath and set its name. Library easily finds it by name (even if included in jar).

**MarshallHelper** has 2 more parameters:
 - **xmlSchemaNameSpace**: Your XSD namespace.
 - **formattedOutput**: If true, output XML will be formatted.

If **xmlSchemaResourceUrl** and **xmlSchemaNameSpace** set empty/null, marshaller won't create XSD schema reference (schemaLocation) in XML.
Sample for **schemaLocation** included:

```XML
<sample xmlns="http://com/meriosol/sample/schema"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://com/meriosol/sample/schema sample.xsd" id="1001"
        created="2014-04-07T03:41:30.493-07:00">
    <title>Sample</title>
</sample>
```

### Exception handling
From exception handling prospective your code is not forced to catch library unchecked exceptions.
Major and the only library specific exception is **JaxbRuntimeException**.

### Optimization
As one of many optimizations these particular helper classes can be instantiated once, stored in e.g. (hash) map
with entity class as a key and value - marshalling helper (e.g. Map<Class, UnmarshallHelper> map).
> NOTE: Because classes weren't designed as thread-safe, you need to take care about it in your code, at least for now.

## Plans
As of now(April 2014) all basic functionality works as expected.
Maybe some performance enhancements and usability improvements will be performed if time allows.

