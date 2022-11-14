package org.example.validation;

import org.example.display.Display;
import org.example.model.Constants;
import org.example.model.Order;
import org.example.model.OrderInformation;
import org.example.model.Product;

import java.util.List;

import static org.example.model.Constants.validFilename;
import static org.example.model.Constants.validFilenameEndsWith;
import static org.example.model.Constants.validFilenameStartsWith;

public class Validation {
    private Display display;
    private static Validation instance = null;

    private Validation(Display display) {
        this.display = display;
    }

    public static Validation getInstance(Display display) {
        return instance != null ? instance : new Validation(display);
    }

    public Boolean isFileNameCorrect(String filename) {
        if (isFilenameEmpty(filename)) return false;
        if (!isLengthValid(filename)) return false;
        if (!hasPrefix(filename)) return false;
        if (!hasSuffix(filename)) return false;
        String numbers = filename.split(validFilenameStartsWith)[1].split(validFilenameEndsWith)[0];
        return isAllDigits(numbers);
    }

    public boolean isFilenameEmpty(String filename) {
        if (filename.isEmpty()) {
            display.printMessage("Filename is empty");
            return true;
        }
        return false;
    }

    public boolean isLengthValid(String filename) {
        if (filename.length() != validFilename.length()) {
            display.printMessage("Filename length is invalid");
            return false;
        }
        return true;
    }

    public boolean hasPrefix(String filename) {
        if (Constants.validFilename.startsWith(filename.substring(0, Constants.validFilenameStartsWith.length()))) {
            return true;
        }
        display.printMessage("Filename does not start with orders");
        return false;
    }

    public boolean hasSuffix(String filename) {
        if (validFilename.endsWith(filename.substring(filename.length() - validFilenameEndsWith.length()))) {
            return true;
        }
        display.printMessage("Filename does not end with .xml");
        return false;
    }

    public boolean isInputValid(OrderInformation orderInformation) {
        if (orderInformation == null) {
            display.printMessage("This XML file could not be processed");
            return false;
        }
        return true;
    }

    public boolean areProductsValid(List<Product> products) {
        if (products == null) {
            display.printMessage("This XML file could not be processed");
            return false;
        }
        return true;
    }

    public boolean areOrdersValid(List<Order> orders) {
        if (orders == null) {
            display.printMessage("This XML file could not be processed");
            return false;
        }
        display.printMessage("Orders are created");
        return true;
    }

    public boolean isProductValid(Product product) {
        return product != null;
    }

    private static boolean isAllDigits(String numbers) {
        return numbers.chars().allMatch(Character::isDigit);
    }
}
