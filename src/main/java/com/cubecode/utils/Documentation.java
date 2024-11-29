package com.cubecode.utils;

import com.cubecode.CubeCode;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.api.EnvType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class Documentation {
    public static class Chapter {
        public String name;
        public String description;
        public String script;
        public List<Method> methods;

        public Chapter(String name, String description, String script, List<Method> methods) {
            this.name = name;
            this.description = description;
            this.script = script;
            this.methods = methods;
        }
    }

    public static class Method {
        public String name;
        public String description;
        public String script;
        public List<Argument> arguments;
        public String returnType;

        public Method(String name, String description, String script, List<Argument> arguments, String returnType) {
            this.name = name;
            this.description = description;
            this.script = script;
            this.arguments = arguments;
            this.returnType = returnType;
        }
    }

    public static class Argument {
        public String name;
        public String type;

        public Argument(String name, String type) {
            this.name = name;
            this.type = type;
        }
    }

    public static Map<EnvType, List<Chapter>> parseDocs() {
        InputStream inputStream;
        try {
            inputStream = MinecraftClient.getInstance().getResourceManager().getResource(new Identifier(CubeCode.MOD_ID, "docs.json")).get().getInputStream();
        } catch (Exception ignored) {
            return null;
        }

        Gson gson = new Gson();

        JsonObject docs = gson.fromJson(new InputStreamReader(inputStream), JsonObject.class);

        Map<EnvType, List<Chapter>> parseDocs = new HashMap<>();

        for (String sideName : docs.keySet()) {
            JsonObject sideChapters = docs.get(sideName).getAsJsonObject();
            List<Chapter> chapters = new ArrayList<>();

            for (String chapterName : sideChapters.keySet()) {
                List<Method> methods = new ArrayList<>();

                JsonArray chapterMethods = sideChapters.get(chapterName).getAsJsonObject().get("methods").getAsJsonArray();

                for (JsonElement method : chapterMethods) {
                    List<Argument> arguments = new ArrayList<>();

                    JsonArray methodArguments = method.getAsJsonObject().get("arguments").getAsJsonArray();

                    for (JsonElement argument : methodArguments) {
                        arguments.add(new Argument(
                                argument.getAsJsonObject().get("name").getAsString(),
                                argument.getAsJsonObject().get("type").getAsString()
                        ));
                    }

                    methods.add(new Method(
                            method.getAsJsonObject().get("name").getAsString(),
                            method.getAsJsonObject().get("description").getAsString(),
                            method.getAsJsonObject().get("script").getAsString(),
                            arguments,
                            method.getAsJsonObject().get("returnType").getAsString()
                    ));
                }

                chapters.add(new Chapter(
                        chapterName,
                        sideChapters.get(chapterName).getAsJsonObject().get("description").getAsString(),
                        sideChapters.get(chapterName).getAsJsonObject().get("script").getAsString(),
                        methods
                ));
            }

            parseDocs.put(EnvType.valueOf(sideName.toUpperCase()), chapters);
        }

        return parseDocs;
    }
}
