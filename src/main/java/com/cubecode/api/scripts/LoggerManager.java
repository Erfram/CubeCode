package com.cubecode.api.scripts;

import com.cubecode.utils.DirectoryManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

//TODO: Сделать работующие логи для клиента и сервера (сейчас работает только локальный сервер)
public class LoggerManager extends DirectoryManager {
    public File logger;

    public LoggerManager(File dir) {
        super(dir);

        this.logger = dir.toPath().resolve("Logger.log").toFile();

        try {
            this.logger.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addLine(String string) {
        try {
            Files.writeString(this.logger.toPath(), string, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void error(String source, String error) {
        String message = "[&4" + source + "&r]" + ": " + error;
        this.addLine(message + "\n");
    }

    public void info(String source, String info) {
        String message = "[&6" + source + "&r]" + ": " + info;
        this.addLine(message + "\n");
    }

    public void warning(String source, String warn) {
        String message = "[&c" + source + "&r]" + ": " + warn;
        this.addLine(message + "\n");
    }

    public void debug(String source, String debug) {
        String message = "[&b" + source + "&r]" + ": " + debug;
        this.addLine(message + "\n");
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
}
