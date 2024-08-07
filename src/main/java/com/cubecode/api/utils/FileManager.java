package com.cubecode.api.utils;

import com.cubecode.CubeCode;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class FileManager {
    public static void writeJsonToFile(String filePath, String json) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
