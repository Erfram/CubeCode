package com.cubecode.api.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;

public class GsonManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private GsonManager() {}

    public static void writeJSON(File file, Object object) {
        try (FileWriter writer = new FileWriter(file)) {
            GSON.toJson(object, writer);
        } catch (IOException ignored) {}
    }

    public static void writeJSON(File file, Object object, Type type) {
        try (FileWriter writer = new FileWriter(file)) {
            GSON.toJson(object, type, writer);
        } catch (IOException ignored) {}
    }

    public static <T> T readJSON(File file, Class<T> clazz) {
        try (FileReader reader = new FileReader(file)) {
            return GSON.fromJson(reader, clazz);
        } catch (IOException ignored) {
            return null;
        }
    }

    public static <T> T readJSON(File file, Type type) {
        try (FileReader reader = new FileReader(file)) {
            return GSON.fromJson(reader, type);
        } catch (IOException ignored) {
            return null;
        }
    }

    public static <T> T readJSON(String json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }
}