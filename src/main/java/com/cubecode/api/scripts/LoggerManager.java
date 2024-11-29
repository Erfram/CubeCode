package com.cubecode.api.scripts;

import com.cubecode.api.files.FileManager;
import com.cubecode.utils.DirectoryManager;

import java.io.File;
import java.io.IOException;

public class LoggerManager extends DirectoryManager {
    File logger;

    public LoggerManager(File dir) {
        super(dir);

        this.logger = dir.toPath().resolve("Logger.log").toFile();

        try {
            this.logger.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public void error(String source, String error) {
        String message = "[&4" + source + "&r]" + ": " + error;

        FileManager.writeToFile(
            this.logger.getPath(),
            message
        );
    }

    public void info(String source, String info) {
        String message = "[&6" + source + "&r]" + ": " + info;

        FileManager.writeToFile(
            this.logger.getPath(),
            message
        );
    }

    public void warn(String source, String warn) {
        String message = "[&c" + source + "&r]" + ": " + warn;

        FileManager.writeToFile(
            this.logger.getPath(),
            message
        );
    }
}
