package com.cubecode.api.scripts;

import com.cubecode.utils.DirectoryManager;
import com.cubecode.utils.GsonManager;
import com.cubecode.utils.ScriptType;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
//TODO Разобраться что это и зачем это, чем отличается от ProjectManager, разделить функционал
public class SettingManager extends DirectoryManager {
    private File settingsFile;

    private ConcurrentHashMap<String, ScriptSetting> settings;

    public SettingManager(File dir) {
        super(dir);

        try {
            this.settingsFile = dir.toPath().resolve("settings.json").toFile();
            this.settingsFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.settings = this.jsonToSettings(GsonManager.readJSON(this.settingsFile, JsonObject.class) == null ? new JsonObject() : GsonManager.readJSON(this.settingsFile, JsonObject.class));
    }

    public void addLibrary(String scriptName, String libraryName) {
        ScriptSetting scriptSetting = this.settings.get(scriptName);

        List<String> libraries = scriptSetting.libraries;

        if (!libraries.contains(libraryName)) {
            libraries.add(libraryName);

            this.settings.remove(scriptName);
            this.settings.put(scriptName, new ScriptSetting(scriptSetting.side, libraries, scriptSetting.lastLaunchErrorLine, scriptSetting.lastLaunchErrorMessage));

            this.write();
        }
    }

    public void removeLibrary(String scriptName, String libraryName) {
        ScriptSetting scriptSetting = this.settings.get(scriptName);

        List<String> libraries = scriptSetting.libraries;

        if (libraries.contains(libraryName)) {
            libraries.remove(libraryName);

            this.settings.remove(scriptName);
            this.settings.put(scriptName, new ScriptSetting(scriptSetting.side, libraries, scriptSetting.lastLaunchErrorLine, scriptSetting.lastLaunchErrorMessage));

            this.write();
        }
    }

    public void deleteScript(String scriptName) {
        this.settings.remove(scriptName);

        this.write();
    }

    public void addScript(String scriptName, ScriptType side) {
        this.settings.put(scriptName, new ScriptSetting(side));

        this.write();
    }

    public void setScriptSide(String scriptName, ScriptType side) {
        ScriptSetting scriptSetting = this.settings.get(scriptName);

        this.settings.remove(scriptName);
        this.settings.put(scriptName, new ScriptSetting(side, scriptSetting.libraries != null ? scriptSetting.libraries : new ArrayList<>(), scriptSetting.lastLaunchErrorLine, scriptSetting.lastLaunchErrorMessage));

        this.write();
    }

    public void renameScriptName(String oldScriptPath, String newScriptName) {
        ScriptSetting scriptSetting = this.settings.get(oldScriptPath);

        this.settings.remove(oldScriptPath);

        if (oldScriptPath.contains("/")) {
            newScriptName = oldScriptPath.substring(0, oldScriptPath.lastIndexOf('/') + 1) + newScriptName;
        }

        this.settings.put(newScriptName, scriptSetting);

        this.write();
    }

    public void renameFolderName(String oldFolderPath, String newFolderName) {
        this.settings.forEach((scriptName, scriptSetting) -> {
            if (scriptName.startsWith(oldFolderPath)) {
                this.settings.remove(scriptName);
                this.settings.put(scriptName.replace(oldFolderPath, oldFolderPath.substring(0, oldFolderPath.lastIndexOf("/") + 1) + newFolderName), scriptSetting);
            }
        });

        this.write();
    }

    public void write() {
        GsonManager.writeJSON(this.settingsFile, this.settingsToJson());
    }

    public void setSettings(ConcurrentHashMap<String, ScriptSetting> settings) {
        this.settings = settings;
    }

    private JsonObject settingsToJson() {
        JsonObject jsonObject = new JsonObject();

        this.settings.forEach((scriptName, scriptSetting) -> {
            JsonObject jsonScriptSetting = new JsonObject();
            JsonArray jsonLibraries = new JsonArray();

            scriptSetting.libraries.forEach(jsonLibraries::add);

            jsonScriptSetting.addProperty("Side", scriptSetting.side.name());
            jsonScriptSetting.add("Libraries", jsonLibraries);
            jsonScriptSetting.addProperty("LastLaunchErrorLine", scriptSetting.lastLaunchErrorLine);
            jsonScriptSetting.addProperty("LastLaunchErrorMessage", scriptSetting.lastLaunchErrorMessage);

            jsonObject.add(scriptName, jsonScriptSetting);
        });

        return jsonObject;
    }

    public ConcurrentHashMap<String, ScriptSetting> jsonToSettings(JsonObject jsonObject) {
        ConcurrentHashMap<String, ScriptSetting> settings = new ConcurrentHashMap<>();

        jsonObject.keySet().forEach(key -> {
            JsonObject jsonScriptSetting = jsonObject.get(key).getAsJsonObject();
            List<String> libraries = new ArrayList<>();

            jsonScriptSetting.get("Libraries").getAsJsonArray().forEach(jsonLibrary -> {
                libraries.add(jsonLibrary.getAsString());
            });

            settings.put(key, new ScriptSetting(
                ScriptType.valueOf(jsonScriptSetting.get("Side").getAsString().toUpperCase()),
                libraries,
                jsonScriptSetting.get("LastLaunchErrorLine").getAsInt(),
                jsonScriptSetting.get("LastLaunchErrorMessage").getAsString()
            ));
        });

        return settings;
    }

    public static class ScriptSetting {
        ScriptType side;
        List<String> libraries;
        int lastLaunchErrorLine;
        String lastLaunchErrorMessage;

        public ScriptSetting(ScriptType side, List<String> libraries, int lastLaunchErrorLine, String lastLaunchErrorMessage) {
            this.side = side;
            this.libraries = libraries;
            this.lastLaunchErrorLine = lastLaunchErrorLine;
            this.lastLaunchErrorMessage = lastLaunchErrorMessage;
        }

        public ScriptSetting(ScriptType side, List<String> libraries) {
            this.side = side;
            this.libraries = libraries;
            this.lastLaunchErrorLine = 0;
            this.lastLaunchErrorMessage = "";
        }

        public ScriptSetting(ScriptType side) {
            this.side = side;
            this.libraries = new ArrayList<>();
            this.lastLaunchErrorLine = 0;
            this.lastLaunchErrorMessage = "";
        }
    }
}
