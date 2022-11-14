package org.example.service;

import org.example.display.Display;
import org.example.model.Constants;
import org.example.model.Order;
import org.example.model.OrderInformation;
import org.example.model.Product;
import org.example.repositories.InputFile;
import org.example.validation.Validation;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FilesImpl implements InputFile {

    private static FilesImpl instance = null;

    private Display display;
    private static boolean isDescription = false;
    private static boolean isGtin = false;
    private static boolean isPrice = false;
    private static boolean isSupplier = false;

    public FilesImpl(Display display) {
        this.display = display;
    }

    public static FilesImpl getInstance(Display display) {
        return instance != null ? instance : new FilesImpl(display);
    }

    /**
     * @param path is the complete path to the xml file
     * @return orderInformation that contains the list with all orders
     */
    @Override
    public OrderInformation getOrderInformationFromFile(Path path) {
        XMLInputFactory factory = XMLInputFactory.newFactory();
        OrderInformation orderInformation = null;
        try (FileInputStream fis = new FileInputStream(path.toFile())) {
            XMLStreamReader reader = factory.createXMLStreamReader(fis);
            orderInformation = processElementsFromFile(reader);
            reader.close();
        } catch (XMLStreamException | NumberFormatException | IOException e) {
            display.printMessage(e.getMessage());
        }
        return orderInformation;
    }

    /**
     * @param reader : createXMLStreamReader
     * @return OrderInformation
     * @throws XMLStreamException
     * translates all xml elements to java objects
     */
    private OrderInformation processElementsFromFile(XMLStreamReader reader) throws XMLStreamException {
        OrderInformation inputXmlFile = new OrderInformation();
        Validation validation = Validation.getInstance(Display.getInstance());
        Order order = null;
        List<Order> orders = null;
        String currency = "";
        String orderId = "";
        LocalDateTime orderDate = null;
        List<Product> products = null;
        Product product = null;
        while (reader.hasNext()) {
            switch (reader.getEventType()) {
                case XMLStreamConstants.START_ELEMENT:
                    String elementName = reader.getLocalName();
                    switch (elementName) {
                        case "orders":
                            orders = new ArrayList<>();
                            break;
                        case "order":
                            order = new Order();
                            products = new ArrayList<>();
                            orderId = reader.getAttributeValue(1);
                            orderDate = LocalDateTime.parse(reader.getAttributeValue(0));
                            break;
                        case "product":
                            product = new Product();
                            break;
                        case "description":
                            isDescription = true;
                            break;
                        case "gtin":
                            isGtin = true;
                            break;
                        case "price":
                            currency = reader.getAttributeValue(0);
                            isPrice = true;
                            break;
                        case "supplier":
                            isSupplier = true;
                            break;
                        default:
                            display.printMessage("The XML file is invalid");
                            display.printMessage("The structure is missing a start element or has a misspelled word");
                    }
                    break;
                case XMLStreamConstants.CHARACTERS:
                    processValues(currency, product, reader);
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    elementName = reader.getLocalName();
                    if (elementName.equals("product") && validation.areProductsValid(products) && validation.isProductValid(product)) {
                        product.setOrderId(orderId);
                        products.add(product);
                    }
                    if (elementName.equals("order") && validation.areProductsValid(products) && validation.areOrdersValid(orders)) {
                        order.setProducts(products);
                        order.setOrderId(orderId);
                        order.setDate(orderDate);
                        orders.add(order);
                    }
                    if (elementName.equals("orders")) {
                        inputXmlFile.setOrders(orders);
                    }
                    break;
            }
            reader.next();
        }
        return inputXmlFile;
    }

    /**
     * @param currency product currency, String
     * @param product the product from the start element
     * @param reader createXMLStreamReader
     *  sets the objects attributes using the values from xml
     */
    private static void processValues(String currency, Product product, XMLStreamReader reader) {
        Validation validation = Validation.getInstance(Display.getInstance());
        if (validation.isProductValid(product)) {
            if (isDescription) {
                product.setDescription(reader.getText());
                isDescription = false;
            }
            if (isGtin) {
                product.setGtin(reader.getText());
                isGtin = false;
            }
            if (isPrice) {
                product.setPrice(Double.valueOf(reader.getText()));
                product.setCurrency(currency);
                isPrice = false;
            }
            if (isSupplier) {
                product.setSupplier(reader.getText());
                isSupplier = false;
            }
        }
    }

    @Override
    public void writeFile(OutputStream outputStream, List<Product> products) {
        XMLOutputFactory output = XMLOutputFactory.newInstance();
        try {
            XMLStreamWriter writer = output.createXMLStreamWriter(outputStream);
            writer.writeStartDocument("utf-8", "1.0");
            writer.writeStartElement("products");
            for (Product product : products) {
                writer.writeStartElement("product");
                writer.writeStartElement("description");
                writer.writeCharacters(product.getDescription());
                writer.writeEndElement();
                writer.writeStartElement("gtin");
                writer.writeCharacters(product.getGtin());
                writer.writeEndElement();
                writer.writeStartElement("price");
                writer.writeAttribute("currency", product.getCurrency());
                writer.writeCharacters(String.valueOf(product.getPrice()));
                writer.writeEndElement();
                writer.writeStartElement("orderid");
                writer.writeCharacters(product.getOrderId());
                writer.writeEndElement();
                writer.writeEndElement();
            }
            writer.writeEndElement();
            writer.writeEndDocument();
            writer.flush();
            writer.close();
        } catch (XMLStreamException e) {
            display.printMessage(e.getMessage());
        }
    }

    /**
     * @param eventPath represents the xml file path
     * @param path represents the folders path
     *           processObjectsFromFile creates the output filename using the id form the list of orders
     *             and the supplier
     */
    @Override
    public void processObjectsFromFile(Path eventPath, Path path) {
        if (!isPathValid(eventPath)) {
            display.printMessage("The filename is invalid");
            return;
        }
        SortService sortLists = SortService.getInstance();
        Validation validation = Validation.getInstance(Display.getInstance());
        RepositoryImpl processDataStructures = RepositoryImpl.getInstance();
        Integer ID = processDataStructures.getId(eventPath.getFileName().toString());
        Path filePath = Path.of(path + "/" + eventPath);
        OrderInformation inputXmlFile = getOrderInformationFromFile(filePath);
        if (validation.isInputValid(inputXmlFile) && validation.areOrdersValid(inputXmlFile.getOrders())) {
            inputXmlFile.setID(ID);
            List<String> suppliers = processDataStructures.getAllSuppliers(inputXmlFile.getOrders());
            List<Order> orders = sortLists.sortOrders(inputXmlFile.getOrders());
            writeFileForEverySupplier(suppliers, orders, ID, processDataStructures);
        }
    }

    /**
     * @param suppliers the list with all suppliers
     * @param orders the list with all the orders
     * @param ID the id from the output file
     * @param processDataStructures the class that implements needed methods
     */
    public void writeFileForEverySupplier(List<String> suppliers, List<Order> orders, Integer ID, RepositoryImpl processDataStructures) {
        for (String supplier : suppliers) {
            String filename = Constants.OUTPUT_FOLDER + supplier + ID + Constants.XML_EXTENSION;
            List<Product> products = processDataStructures.getProductsBySupplier(supplier, orders);
            writeFileFromListOfProducts(products, filename);
        }
    }

    /**
     * @param products the list with products with the same supplier, ordered by price and order creationDate
     * @param filename the filename for the file that is written
     */
    @Override
    public void writeFileFromListOfProducts(List<Product> products, String filename) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            writeFile(out, products);
            String xml = out.toString(StandardCharsets.UTF_8);
            String prettyPrintXML = formatXml(xml);
            Files.writeString(Paths.get(filename),
                    prettyPrintXML, StandardCharsets.UTF_8);
        } catch (IOException e) {
            display.printMessage(e.getMessage());
        }
    }

    @Override
    public String formatXml(String xml) {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        try {
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
            StreamSource source = new StreamSource(new StringReader(xml));
            StringWriter output = new StringWriter();
            transformer.transform(source, new StreamResult(output));
            return output.toString();
        } catch (TransformerException e) {
            display.printMessage(e.getMessage());
        }
        return "";
    }

    private static boolean isPathValid(Path eventPath) {
        Validation validation = Validation.getInstance(Display.getInstance());
        return validation.isFileNameCorrect(eventPath.getFileName().toString());
    }
}
