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

    protected File getFile(String path) {
        for (File file : this.getFiles()) if (file.getPath().equals(path)) {
            return file;
        }

        return null;
    }

    protected Set<File> getFiles() {
        return Set.of(Objects.requireNonNull(this.directory.listFiles()));
    }

    protected boolean createFile(String path) {
        try {
            return this.directory.toPath().resolve(path).toFile().createNewFile();
        } catch (IOException ignored) {
        }

        return false;
    }

    protected boolean createDirectory(String path) {
        return this.directory.toPath().resolve(path).toFile().mkdirs();
    }

    protected boolean deleteFile(String path) {
        return this.getFile(path).delete();
    }

    protected boolean existFile(String path) {
        return this.getFile(path).exists();
    }

    protected String readFile(String path) {
        try {
            return Files.readString(this.directory.toPath().resolve(path), StandardCharsets.UTF_8);
        } catch (IOException ignored) {
            return "";
        }
    }

    protected void writeFile(String path, String content) {
        try {
            Files.writeString(this.directory.toPath().resolve(path), content);
        } catch (IOException ignored) {
        }
    }

    protected String getRelativePath(File file) {
        String path = file.getPath();
        return new File(this.directory.getPath()).toURI().relativize(new File(path).toURI()).getPath();
    }

    protected String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        return lastIndexOf == -1 ? "" : name.substring(lastIndexOf + 1);
    }

    protected File getDirectory() {
        return this.directory;
    }
}
