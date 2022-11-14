package org.example.repositories;


import org.example.model.OrderInformation;
import org.example.model.Product;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.List;

public interface InputFile {
    OrderInformation getOrderInformationFromFile(Path path) throws FileNotFoundException, XMLStreamException;

    void writeFile(OutputStream outputStream, List<Product> products) throws XMLStreamException;

    void processObjectsFromFile(Path eventPath, Path path) throws FileNotFoundException;

    void writeFileFromListOfProducts(List<Product> products, String filename);

    String formatXml(String xml) throws TransformerException;
}
