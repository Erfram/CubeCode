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

    public File getFile(String name) {
        return new File(DIRECTORY, name);
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

    public String readFileToString(String name) {
        try {
            return FileUtils.readFileToString(this.getFile(name), Charset.defaultCharset());
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

    public boolean renameFile(String name, String newName) {
        File file = this.getFile(name);

        return file.renameTo(this.getFile(newName));
    }

    public File getDirectory() {
        return this.DIRECTORY;
    }
}