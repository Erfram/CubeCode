package com.cubecode.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Set;

public class DirectoryManager {
    protected final File directory;

    public DirectoryManager(File directory) {
        this.directory = directory;
        this.directory.mkdirs();
    }

    public DirectoryManager() {
        this.directory = null;
    }

    public File getFile(String path) {
        for (File file : this.getFiles()) if (file.getPath().equals(path)) {
            return file;
        }

        return null;
    }

    public Set<File> getFiles() {
        return Set.of(Objects.requireNonNull(this.directory.listFiles()));
    }

    public boolean createFile(String path) {
        try {
            return this.getFile(path).createNewFile();
        } catch (IOException ignored) {
        }

        return false;
    }

    public boolean deleteFile(String path) {
        return this.getFile(path).delete();
    }

    public boolean existFile(String path) {
        return this.getFile(path).exists();
    }

    public String readFile(String path) {
        try {
            return Files.readString(this.directory.toPath().resolve(path), StandardCharsets.UTF_8);
        } catch (IOException ignored) {
            return "";
        }
    }

    public void writeFile(String path, String content) {
        try {
            Files.writeString(this.directory.toPath().resolve(path), content);
        } catch (IOException ignored) {
        }
    }

    public File getDirectory() {
        return this.directory;
    }
}
