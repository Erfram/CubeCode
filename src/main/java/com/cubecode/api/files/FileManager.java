package com.cubecode.api.files;

import com.cubecode.api.utils.DirectoryManager;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class FileManager extends DirectoryManager {
    public FileManager(File file) {
        super(file);
    }

    public static void writeJsonToFile(String filePath, String json) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Set<File> getFiles() {
        Set<File> fileSet = super.getFiles();
        Set<File> files = new HashSet<>();

        for (File file : fileSet) {
            if (file.isDirectory()) {
                this.collectFiles(files, Set.of(Objects.requireNonNull(file.listFiles())));
            } else {
                files.add(file);
            }
        }

        return files;
    }

    private void collectFiles(Set<File> files, Set<File> fileSet) {
        for (File file : fileSet) {
            if (file.isDirectory()) {
                this.collectFiles(files, Set.of(Objects.requireNonNull(file.listFiles())));
            } else {
                files.add(file);
            }
        }
    }
}
