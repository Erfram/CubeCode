package com.cubecode.api.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public abstract class GSONManager {
    protected final transient File FILE;
    protected final transient Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public GSONManager(File file) {
        this.FILE = file;

        try {
            this.FILE.createNewFile();
        } catch (IOException ignored) {

        }
    }

    public File getFile() {
        return this.FILE;
    }

    public void writeJSON(Object clazz) {
        try (FileWriter writer = new FileWriter(this.FILE)) {
            this.GSON.toJson(clazz, writer);
        } catch (IOException ignored) {}
    }

    public Object readJSON(Class<Object> clazz) {
        try (FileReader reader = new FileReader(this.FILE)) {
            return this.GSON.fromJson(reader, clazz);
        } catch (IOException ignored) {
            return null;
        }
    }
}
