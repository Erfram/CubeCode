package com.cubecode.api.scripts;

import com.cubecode.utils.DirectoryManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

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

        try {
            Files.writeString(this.logger.toPath(), message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void info(String source, String info) {
        String message = "[&6" + source + "&r]" + ": " + info;

        try {
            Files.writeString(this.logger.toPath(), message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void warn(String source, String warn) {
        String message = "[&c" + source + "&r]" + ": " + warn;

        try {
            Files.writeString(this.logger.toPath(), message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
