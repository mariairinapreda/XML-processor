package org.example.service;

import org.example.model.Constants;
import org.example.display.Display;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.Map;

public class Watcher {
    private Display display;

    public Watcher(Display display) {
        this.display = display;
    }


    /**description: monitor() registers all files that are added in the input folder
     */
    public void monitor() {
        try (WatchService service = FileSystems.getDefault().newWatchService()) {
            Map<WatchKey, Path> keyMap = new HashMap<>();
            Path path = Paths.get(Constants.INPUT_PATH);
            keyMap.put(path.register(service, StandardWatchEventKinds.ENTRY_CREATE), path);
            WatchKey watchKey;
            do {
                watchKey = service.take();
                for (WatchEvent<?> event : watchKey.pollEvents()) {
                    Path eventPath = (Path) event.context();
                    FilesImpl processFiles = FilesImpl.getInstance(display);
                    processFiles.processObjectsFromFile(eventPath, path);
                }
            } while (watchKey.reset());
        } catch (FileNotFoundException | InterruptedException e) {
            display.printMessage(e.getMessage());
        } catch (IOException e) {
            display.printMessage(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
