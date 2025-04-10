package com.cubecode.api.scripts;

import com.cubecode.utils.manager.DirectoryManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

//TODO: Сделать работующие логи для клиента и сервера (сейчас работает только локальный сервер)
//TODO: Сделать логгирование всех ошибок мода в отдельной вкладке LoggerView
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
        String message = "&7[&4ERROR&7] [" + source + "&7]" + ": &r" + error;
        this.addLine(message + "\n");
    }

    public void info(String source, String info) {
        String message = "&7[&fINFO&7] [" + source + "&7]" + ": &r" + info;
        this.addLine(message + "\n");
    }

    public void warning(String source, String warn) {
        String message = "&7[&cWARN&7] [" + source + "&7]" + ": &r" + warn;
        this.addLine(message + "\n");
    }

    public void debug(String source, String debug) {
        String message = "&7[&bDEBUG&7] [" + source + "&7]" + ": &r" + debug;
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
