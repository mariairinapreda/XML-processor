package org.example.display;


import java.util.logging.Level;
import java.util.logging.Logger;

public class Display {
    private static Display instance = null;

    private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public Display() {
    }

    public static Display getInstance() {
        return instance != null ? instance : new Display();
    }

    public void printMessage(String message) {
        logger.log(Level.INFO, message);
    }
}
