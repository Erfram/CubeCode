package com.cubecode.utils;

import com.cubecode.CubeCode;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class Documentation {
    public static class Chapter {
        public String description;
        public String script;
        public List<Method> methods;

        public Chapter(String description, String script, List<Method> methods) {
            this.description = description;
            this.script = script;
            this.methods = methods;
        }
    }

    public static class Method {
        public String name;
        public String description;
        public String script;
        public List<Argemunt> arguments;
        public String returnType;

        public Method(String name, String description, String script, List<Argemunt> arguments, String returnType) {
            this.name = name;
            this.description = description;
            this.script = script;
            this.arguments = arguments;
            this.returnType = returnType;
        }
    }

    public static class Argemunt {
        public String name;
        public String type;

        public Argemunt(String name, String type) {
            this.name = name;
            this.type = type;
        }
    }

    public static Map<String, Chapter> parseDocs() {
        InputStream inputStream;
        try {
            inputStream = MinecraftClient.getInstance().getResourceManager().getResource(new Identifier(CubeCode.MOD_ID, "docs.json")).get().getInputStream();
        } catch (Exception ignored) {
            return null;
        }

        Gson gson = new Gson();

        JsonObject docs = gson.fromJson(new InputStreamReader(inputStream), JsonObject.class);

        JsonObject chapters = docs.getAsJsonObject("chapters");

        Map<String, Chapter> sortedMethods = new HashMap<>();

        for (String chapterName : chapters.keySet()) {
            JsonArray methodsArray = chapters.getAsJsonObject(chapterName).getAsJsonArray("methods");
            List<Documentation.Method> methodList = new ArrayList<>();

            for (JsonElement jsonElement : methodsArray) {
                JsonObject methodObject = jsonElement.getAsJsonObject();

                List<Documentation.Argemunt> arguments = new ArrayList<>();

                methodObject.getAsJsonArray("arguments").forEach(element -> {
                    arguments.add(new Documentation.Argemunt(element.getAsJsonObject().get("name").getAsString(), element.getAsJsonObject().get("type").getAsString()));
                });

                methodList.add(new Documentation.Method(
                        methodObject.get("name").getAsString(),
                        methodObject.get("description").getAsString(),
                        methodObject.get("script").getAsString(),
                        arguments,
                        methodObject.get("returnType").getAsString()
                ));
            }

            methodList.sort(Comparator.comparing(method -> method.name));

            Chapter chapter = new Chapter(
                    chapters.getAsJsonObject(chapterName).get("description").getAsString(),
                    chapters.getAsJsonObject(chapterName).get("script").getAsString(),
                    methodList);
            sortedMethods.put(chapterName, chapter);
        }

        return sortedMethods;
    }
}
