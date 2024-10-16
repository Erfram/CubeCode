package com.cubecode.api.utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

public abstract class DirectoryManager {
    protected final File DIRECTORY;

    public DirectoryManager(File directory) {
        this.DIRECTORY = directory;
        this.DIRECTORY.mkdirs();
    }

    public DirectoryManager() {
        this.DIRECTORY = null;
    }

    public File getFile(String path) {
        for (File file : this.getFiles()) if (file.getPath().equals(path)) {
            return file;
        }

        return null;
    }

    public Set<File> getFiles() {
        return Set.of(Objects.requireNonNull(this.DIRECTORY.listFiles()));
    }

    public List<String> readFilesToString(Collection<File> files) {
        try {
            List<String> strings = new ArrayList<>();
            for (File file : files) {
                strings.add(FileUtils.readFileToString(file, Charset.defaultCharset()));
            }
            return strings;
        } catch (IOException ignored) {
            return new ArrayList<>();
        }
    }

    public List<String> readFilesToString() {
        try {
            List<String> strings = new ArrayList<>();
            for (File file : this.getFiles()) {
                strings.add(FileUtils.readFileToString(file, Charset.defaultCharset()));
            }
            return strings;
        } catch (IOException ignored) {
            return new ArrayList<>();
        }
    }

    public String readFileToString(String path) {
        try {
            return FileUtils.readFileToString(new File(path), Charset.defaultCharset());
        } catch (IOException ignored) {
            return "";
        }
    }

    public boolean deleteFile(String name) {
        return this.getFile(name).delete();
    }

    public boolean existFile(String name) {
        return this.getFile(name).exists();
    }

    public File getDirectory() {
        return this.DIRECTORY;
    }
}