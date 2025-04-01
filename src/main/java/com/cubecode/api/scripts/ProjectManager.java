package com.cubecode.api.scripts;

import com.cubecode.CubeCode;
import com.cubecode.client.views.ide.utils.node.*;
import com.cubecode.utils.*;
import com.google.gson.*;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProjectManager extends DirectoryManager {

    private static File settings;

    private List<Script> scripts = new ArrayList<>();
    private List<Script> clientScripts = new ArrayList<>();
    private List<IdeaNode> nodes = new ArrayList<>();

    public ProjectManager(File scriptsDirectory) {
        super(scriptsDirectory);

        settings = this.DIRECTORY.toPath().resolve("settings.json").toFile();

        this.loadSettings();
        this.loadScriptsAndNodes();
        this.refreshSettings();

        this.scripts.forEach((script) -> {
            if (script.getSide() == ScriptType.CLIENT) {
                this.clientScripts.add(script);
            }
        });
    }

    private void loadSettings() {
        try {
            if (settings.createNewFile() || !JsonUtils.isValid(this.readFileToString(settings.getPath()))) {
                Files.writeString(settings.toPath(), "{}");
            }
        } catch (IOException ignored) {
        }
    }

    private void loadScriptsAndNodes() {
        String settingsContent = this.readFileToString(settings.getPath());
        JsonObject jsonObject;

        if (JsonUtils.isValid(settingsContent)) {
            jsonObject = JsonParser.parseString(settingsContent).getAsJsonObject();
        } else {
            throw new RuntimeException("Invalid JSON Setting");
        }

        this.scripts = new ArrayList<>();
        this.nodes = new ArrayList<>();

        this.scanDirectory(this.getFiles().stream().toList(), this.scripts, this.nodes, jsonObject);

        GsonManager.writeJSON(settings, jsonObject);

        CubeCode.settingManager.setSettings(CubeCode.settingManager.jsonToSettings(jsonObject));
    }

    private void scanDirectory(List<File> files, List<Script> scripts, List<IdeaNode> nodes, JsonObject settingsJson) {
        files.forEach(file -> {
            String fileName = file.getName();
            if (file.isDirectory()) {
                FolderNode folderNode = new FolderNode(fileName);
                this.scanDirectory(Arrays.asList(file.listFiles()), scripts, folderNode.getChildren(), settingsJson);
                nodes.add(folderNode);
            } else {
                if (file.getName().equals("settings.json")) return;
                if (file.getName().endsWith(".log")) return;

                String scriptPath = this.getRelativePath(file);
                String scriptContent = this.readFileToString(file.getPath());
                String side = "SERVER";
                List<String> libraries = new ArrayList<>();

                if (!this.isValidSetting(scriptPath)) {
                    JsonObject setting = new JsonObject();
                    setting.addProperty("Side", side);
                    setting.add("Libraries", new JsonArray());
                    settingsJson.add(scriptPath, setting);
                } else {
                    side = settingsJson.getAsJsonObject(scriptPath).get("Side").getAsString();
                    settingsJson.getAsJsonObject(scriptPath).get("Libraries").getAsJsonArray().forEach(jsonElement -> {
                        libraries.add(jsonElement.getAsString());
                    });
                }

                Script script = new Script(scriptPath, scriptContent, ScriptType.valueOf(side.toUpperCase()), libraries);

                scripts.add(script);
                nodes.add(new ScriptNode(fileName, script, "/" + scriptPath));
            }
        });
    }

    public void refreshSettings() {
        String settingsContent = this.readFileToString(settings.getPath());
        if (JsonUtils.isValid(settingsContent)) {
            JsonObject jsonSetting = JsonParser.parseString(settingsContent).getAsJsonObject();
            for (Script script : this.scripts) {
                if (!this.isValidSetting(script.getName())) {
                    JsonObject jsonScriptSetting = new JsonObject();
                    jsonScriptSetting.addProperty("Side", ScriptType.SERVER.name());
                    jsonScriptSetting.add("Libraries", new JsonArray());

                    jsonSetting.add(script.getName(), jsonScriptSetting);
                }
            }

            GsonManager.writeJSON(settings, jsonSetting);
        } else {
            throw new RuntimeException("Invalid JSON Setting");
        }
    }

    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        return lastIndexOf == -1 ? "" : name.substring(lastIndexOf + 1);
    }

    private String getRelativePath(File file) {
        String path = file.getPath();
        return new File(this.DIRECTORY.getPath()).toURI().relativize(new File(path).toURI()).getPath();
    }

    public void writeToFile(String path, String content) {
        try {
            Files.writeString(this.DIRECTORY.toPath().resolve(path), content);
        } catch (IOException e) {
            CubeCode.LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }

        this.loadScriptsAndNodes();
    }

    public void renameFile(String path, String name) {
        Path resolve = CubeCode.projectManager.getDirectory().toPath().resolve(path);
        File file = resolve.toFile();

        File renamedFile = new File(file.getParent(), name);

        file.renameTo(renamedFile);

        this.loadScriptsAndNodes();
    }

    public void deleteFile(String path, NodeType node) {
        try {
            Path resolve = CubeCode.projectManager.getDirectory().toPath().resolve(path);
            if (node == NodeType.FOLDER) {
                FileUtils.deleteDirectory(resolve.toFile());
            } else {
                Files.delete(resolve);
            }

            this.loadScriptsAndNodes();
        } catch (IOException ignored) {
        }
    }

    public void createTxtFile(String name, String path, String content) {
        Path projectPath = CubeCode.projectManager.getDirectory().toPath();

        File file = projectPath.resolve(path.isEmpty() ? "" : path.substring(1)).resolve(name).toFile();

        try (FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8)) {
            writer.write(content);

            this.loadScriptsAndNodes();
        } catch (IOException ignored) {
        }
    }

    public void createFolder(String name, String path) {
        Path scriptsPath = CubeCode.projectManager.getDirectory().toPath();

        scriptsPath.resolve(path).resolve(name).toFile().mkdir();

        this.loadScriptsAndNodes();
    }

    public void createFolderAndScripts(FolderNode folderNode, String parentPath) {
        Path scriptsPath = CubeCode.projectManager.getDirectory().toPath();
        Path folderPath = scriptsPath.resolve(parentPath.isEmpty() ? "" : parentPath.substring(1)).resolve(folderNode.getName());

        if (folderPath.toFile().mkdir()) {
            if (parentPath.isEmpty()) {
                this.nodes.add(folderNode);
            } else {
                IdeaNode nodeByPath = NodeUtils.findNodeByPath(this.nodes, parentPath);
                ((FolderNode) nodeByPath).addChild(folderNode);
            }

            for (IdeaNode child : folderNode.getChildren()) {
                if (child instanceof FolderNode) {
                    createFolderAndScripts((FolderNode) child, folderNode.getPath());
                } else if (child instanceof ScriptNode) {
                    ScriptNode scriptNode = (ScriptNode) child;
                    this.createTxtFile(scriptNode.getScript().getName(), folderNode.getPath(), scriptNode.getScript().getCode());
                }
            }
        }
    }

    public List<Script> getScripts() {
        return this.scripts;
    }

    public List<Script> getClientScripts() {
        return this.clientScripts;
    }

    public List<IdeaNode> getNodes() {
        return this.nodes;
    }

    @Nullable
    public Script getScript(String name) {
        for (Script script : this.getScripts()) if (script.getName().equals(name)) {
            return script;
        }

        return null;
    }

    @Nullable
    public Script getClientScript(String name) {
        for (Script script : this.getClientScripts()) if (script.getName().equals(name)) {
            return script;
        }

        return null;
    }

    public void addLibraryScript(String name, String library) {
        Script script = this.getScript(name);

        if(script != null) {
            script.addLibraryScript(library);
        }
    }

    public void removeLibraryScript(String name, String library) {
        Script script = this.getScript(name);

        if(script != null) {
            script.removeLibraryScript(library);
        }
    }

    private boolean isValidSetting(String key) {
        String json = this.readFileToString(settings.getPath());
        try {
            JsonElement element = JsonParser.parseString(json);
            if (element.isJsonObject()) {
                JsonObject jsonObject = element.getAsJsonObject();
                if (jsonObject.has(key)) {
                    JsonObject paramObject = jsonObject.getAsJsonObject(key);
                    boolean isSide = false;
                    boolean isLibraries = false;

                    if (paramObject.has("Side")) {
                        String sideValue = paramObject.get("Side").getAsString();
                        isSide = "client".equalsIgnoreCase(sideValue) || "server".equalsIgnoreCase(sideValue);
                    }
                    if (paramObject.has("Libraries")) {
                        isLibraries = paramObject.get("Libraries").isJsonArray();
                    }

                    return isSide && isLibraries;
                }
            }
        } catch (JsonSyntaxException e) {
            return false;
        }
        return false;
    }
}