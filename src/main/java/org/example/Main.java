package org.example;


import org.example.display.Display;
import org.example.service.Watcher;


public class Main {

    public static void main(String[] args) {
        Watcher checkForNewFiles = new Watcher(Display.getInstance());
        checkForNewFiles.monitor();
    }
}