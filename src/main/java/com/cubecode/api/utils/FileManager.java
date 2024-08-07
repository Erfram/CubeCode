package com.cubecode.api.utils;

import java.io.FileWriter;
import java.io.IOException;

public class FileManager {
    public static void writeJsonToFile(String filePath, String json) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
