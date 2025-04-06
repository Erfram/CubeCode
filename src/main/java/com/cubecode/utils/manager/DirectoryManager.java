package com.cubecode.utils.manager;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public abstract class DirectoryManager {
    protected final File directory;

    public DirectoryManager(File directory) {
        this.directory = directory;
        this.directory.mkdirs();
    }

    public File getFile(String name) {
        return new File(directory, name);
    }

    public Set<File> getFiles(boolean recursively) {
        Set<File> files = new HashSet<>();
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.isFile()) {
                files.add(file);
            } else if (recursively && file.isDirectory()) {
                files.addAll(getFiles(true));
            }
        }
        return files;
    }

    public String readFileToString(String path) {
        try {
            return Files.readString(Path.of(path), StandardCharsets.UTF_8);
        } catch (IOException ignored) {
            return "";
        }
    }

    public File getDirectory() {
        return this.directory;
    }
}