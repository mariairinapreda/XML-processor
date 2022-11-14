package org.example.model;

public class Constants {

    public static final String validFilename = "orders##" + Constants.XML_EXTENSION;
    public static final String INPUT_PATH = "./input";
    public static final String OUTPUT_FOLDER = "src/main/output/";
    public static final String validFilenameStartsWith = validFilename.split("##")[0];
    public static final String validFilenameEndsWith = validFilename.split("##")[1];
    public static final String XML_EXTENSION = ".xml";

}
