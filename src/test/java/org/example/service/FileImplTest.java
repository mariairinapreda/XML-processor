package org.example.service;


import org.example.model.OrderInformation;
import org.example.model.Product;
import org.example.display.Display;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;

import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class FileImplTest {
    private static FilesImpl processFilesImpl;
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @BeforeAll
    public static void initialization(){
        processFilesImpl= FilesImpl.getInstance(Display.getInstance());
    }

    @Test
    public void getInstanceShouldReturnInstanceOfProcessFileImpl() {
        assertThat(processFilesImpl).isInstanceOf(FilesImpl.class);

    }

    @Test
    public void getOrderInformationFromXmlFileShouldReturnListOfProducts() {
        OrderInformation orderInformation =processFilesImpl.getOrderInformationFromFile(Path.of("src/test/testOrders/orders55.xml"));
        assertThat(orderInformation.getOrders().size()).isEqualTo(2);
    }

    @Test
    public void writeXmlFileFromListOfProductsShouldCreateXmlFileWithGivenFilename() throws FileNotFoundException {
        List<Product> products=new ArrayList<>();
        Product product=new Product();
        product.setDescription("00");
        product.setCurrency("USD");
        product.setGtin("0000");
        product.setPrice((double)0);
        product.setSupplier("sony");
        products.add(product);
        processFilesImpl.writeFileFromListOfProducts(products, "src/test/testOrders/testFile.xml");
        FileOutputStream fileOutputStream=new FileOutputStream("src/test/testOrders/testFile.xml");
        assertThat(fileOutputStream).isNotNull();

    }
 @Test
    public void orderInformationFromXmlFileShouldCatchIOException(){
     exception.expect(XMLStreamException.class);
     processFilesImpl.getOrderInformationFromFile(Path.of("src/test/testOrders/orders23.xml"));
 }

    @Test
    public void orderInformationFromXmlFileShouldCatchNumberFormatException(){
        exception.expect(NumberFormatException.class);
        processFilesImpl.getOrderInformationFromFile(Path.of("src/test/testOrders/orders44.xml"));
    }
    @Test
    public void orderInformationFromXmlFileShouldCatchFileNotFound(){
        exception.expect(FileNotFoundException.class);
        processFilesImpl.getOrderInformationFromFile(Path.of("src/test/testOrders/orders77.xml"));
    }

}