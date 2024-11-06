package com.cubecode.client.scripts;

import com.cubecode.api.files.FileManager;
import com.cubecode.api.scripts.ProjectManager;
import com.cubecode.api.scripts.ServerScript;
import com.cubecode.api.scripts.ScriptScope;
import com.cubecode.api.scripts.code.JavaScriptUtils;
import com.cubecode.api.scripts.code.JavaUtils;
import com.cubecode.client.scripts.code.ClientScriptFactory;
import com.cubecode.client.views.idea.utils.Extension;
import com.cubecode.client.views.idea.utils.node.FolderNode;
import com.cubecode.client.views.idea.utils.node.IdeaNode;
import com.cubecode.client.views.idea.utils.node.ScriptNode;
import com.cubecode.utils.CubeCodeException;
import com.cubecode.utils.DirectoryManager;
import dev.latvian.mods.rhino.*;
import dev.latvian.mods.rhino.mod.util.RemappingHelper;
import dev.latvian.mods.rhino.util.Remapper;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class ClientProjectManager extends DirectoryManager {
    public static final Remapper remapper = RemappingHelper.getMinecraftRemapper();
    public static final Context globalContext = Context.enter();
    public static final ScriptScope globalScope = new ScriptScope("CubeCode client global scope", globalContext);

    public static final String DEFAULT_SCRIPT = "function client(c) {}";

    private Set<ClientScript> scripts = new HashSet<>();
    private List<IdeaNode> nodes = new ArrayList<>();

    public ClientProjectManager(File directory) {
        super(directory);

        globalContext.setRemapper(remapper);
        globalContext.setApplicationClassLoader(ProjectManager.class.getClassLoader());
        globalContext.setMaximumInterpreterStackDepth(500);
        globalScope.setParentScope(globalContext.initStandardObjects());

        globalScope.set("CubeCode", new ClientScriptFactory());
        globalScope.set("Java", new JavaUtils(globalContext, globalScope));
        globalScope.set("JavaScript", new JavaScriptUtils(globalContext, globalScope, this.getDirectory()));

        this.updateScriptsFromFiles();
        this.updateIdeaNodesFromFiles();
    }

    public Object evaluate(Context context, ScriptScope scope, String code, String sourceName) {
        return context.evaluateString(scope, code, sourceName, 1, null);
    }

    public Object invokeFunction(Context context, Scriptable scope, String function, Object[] args) {
        Function functionObject = (Function) ScriptableObject.getProperty(scope, function, context);
        return functionObject.call(context, scope, scope, args);
    }

    public void evalCode(String code, String sourceName, @Nullable Map<String, Object> properties) throws CubeCodeException {
        Context context = Context.enter();
        ScriptableObject scope = context.initSafeStandardObjects();

        context.setRemapper(remapper);
        context.setApplicationClassLoader(ProjectManager.class.getClassLoader());

        if (properties != null) {
            for (Map.Entry<String, Object> property : properties.entrySet()) {
                ScriptableObject.putConstProperty(scope, property.getKey(), Context.javaToJS(context, property.getValue(), scope), context);
            }
        }

        try {
            context.evaluateString(scope, code, sourceName, 1, null);
        } catch (EvaluatorException | EcmaError e) {
            String errorType = (e instanceof EvaluatorException) ? "SyntaxError" : "EcmaError";
            String details = e.details().replaceFirst("TypeError: ", "");
            throw new CubeCodeException(errorType + ": " + details, sourceName);
        } catch (Exception e) {
            throw new CubeCodeException(e.getClass().getSimpleName() + ": " + e.getLocalizedMessage(), sourceName);
        }
    }

    @Nullable
    public ClientScript getScript(String scriptName) {
        for (ClientScript script : this.scripts) if (script.name.equals(scriptName)) {
            return script;
        }

        return null;
    }

    public List<ClientScript> getScripts() {
        return this.scripts.stream().toList();
    }

    public List<IdeaNode> getNodes() {
        return this.nodes;
    }

    public void updateIdeaNodesFromFiles() {
        List<IdeaNode> newIdeaNodes = new ArrayList<>();
        scanDirectory(this.getFiles(), newIdeaNodes);
        this.nodes = newIdeaNodes;
    }

    private void scanDirectory(Collection<File> files, List<IdeaNode> ideaNodes) {
        for (File file : files) {
            String fileName = file.getName();

            if (file.isDirectory()) {
                FolderNode folderNode = new FolderNode(fileName);
                scanDirectory(Arrays.asList(file.listFiles()), folderNode.getChildren());
                ideaNodes.add(folderNode);
            } else if (file.getName().endsWith(".js")) {
                String relativePath = getRelativePath(file);
                String scriptContent = readFileToString(file.getPath());

                ideaNodes.add(new ScriptNode(new ServerScript(fileName, scriptContent), null, "/"+relativePath));
            }
        }
    }

    public void updateScriptsFromFiles() {
        Set<ClientScript> newScripts = new HashSet<>();
        scanDirectory(this.getFiles(), newScripts);
        this.scripts = newScripts;
    }

    private void scanDirectory(Collection<File> files, Set<ClientScript> scripts) {
        for (File file : files) {
            if (file.isDirectory()) {
                scanDirectory(Arrays.asList(file.listFiles()), scripts);
            } else {
                if (file.getName().endsWith(".js")) {
                    String relativePath = getRelativePath(file);
                    scripts.add(new ClientScript(relativePath, this.readFileToString(file.getPath())));
                }
            }
        }
    }

    private String getRelativePath(File file) {
        String path = file.getPath();
        return new File(this.DIRECTORY.getPath()).toURI().relativize(new File(path).toURI()).getPath();
    }

    private boolean isValidScriptFile(File file) {
        String extension = getFileExtension(file);
        return Extension.containsName(extension);
    }

    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        return lastIndexOf == -1 ? "" : name.substring(lastIndexOf + 1);
    }

    public void createScript(ClientScript script) {
        Path projectPath = this.DIRECTORY.toPath();

        File file = projectPath.resolve(script.name).toFile();

        FileManager.writeToFile(file.getPath(), script.code);

        this.updateScriptsFromFiles();
        this.updateIdeaNodesFromFiles();
    }

    public void createScripts(List<ClientScript> scripts) {
        Path projectPath = this.DIRECTORY.toPath();

        for (ClientScript script : scripts) {
            File file = projectPath.resolve(script.name).toFile();

            FileManager.writeToFile(file.getPath(), script.code);
        }

        this.updateScriptsFromFiles();
        this.updateIdeaNodesFromFiles();
    }
}