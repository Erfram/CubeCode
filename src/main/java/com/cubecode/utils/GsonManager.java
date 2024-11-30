package com.cubecode.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class GsonManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private GsonManager() {}

    public static void writeJSON(File file, Object object) {
        try (FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8)) {
            GSON.toJson(object, writer);
        } catch (IOException ignored) {}
    }

    public static void writeJSON(File file, Object object, Type type) {
        try (FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8)) {
            GSON.toJson(object, type, writer);
        } catch (IOException ignored) {}
    }

    public static <T> T readJSON(File file, Class<T> clazz) {
        try (FileReader reader = new FileReader(file, StandardCharsets.UTF_8)) {
            return GSON.fromJson(reader, clazz);
        } catch (IOException ignored) {
            return null;
        }
    }

    public static <T> T readJSON(File file, Type type) {
        try (FileReader reader = new FileReader(file, StandardCharsets.UTF_8)) {
            return GSON.fromJson(reader, type);
        } catch (IOException ignored) {
            return null;
        }
    }

    public static boolean isValidJSON(File file) {
        JsonParser parser = new JsonParser();

        try{
            parser.parse(Files.readString(file.toPath()));

            return true;
        } catch(JsonSyntaxException | IOException ignored){
            return false;
        }
    }

    public static boolean isValidJSON(String json) {
        JsonParser parser = new JsonParser();

        try{
            parser.parse(json);

            return true;
        } catch(JsonSyntaxException ignored){
            return false;
        }
    }

    public static <T> T readJSON(String json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }

    public static String toJson(Object object) {
        return GSON.toJson(object);
    }
}