package com.cubecode.client.scripts;

import com.cubecode.api.files.FileManager;
import com.cubecode.utils.DirectoryManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClientLoggerManager extends DirectoryManager {
    public File logger;

    public ClientLoggerManager(File dir) {
        super(dir);

        this.logger = dir.toPath().resolve("Logger.log").toFile();

        try {
            this.logger.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public String[] getLogs() {
        String[] logs;
        try {
            logs = Files.readString(this.logger.toPath()).split("\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return logs;
    }

    public void error(String source, String error) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = now.format(formatter);

        String message = String.format("&9[%s]&r &c[Client/ERROR] &3(%s)&r %s", formattedTime, source, error);

        List<String> logs = new ArrayList<>(List.of(this.getLogs()));
        logs.add(message);

        FileManager.writeToFile(this.logger.getPath(), String.join("\n", logs));
    }

    public void info(String source, String info) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = now.format(formatter);

        String message = String.format("&9[%s]&r &3[Client/INFO] &3(%s)&r %s", formattedTime, source, info);

        List<String> logs = new ArrayList<>(List.of(this.getLogs()));
        logs.add(message);

        FileManager.writeToFile(this.logger.getPath(), String.join("\n", logs));
    }

    public void warn(String source, String warn) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = now.format(formatter);

        String message = String.format("&9[%s]&r &6[Client/WARN] &3(%s)&r %s", formattedTime, source, warn);

        List<String> logs = new ArrayList<>(List.of(this.getLogs()));
        logs.add(message);

        FileManager.writeToFile(this.logger.getPath(), String.join("\n", logs));
    }
}
