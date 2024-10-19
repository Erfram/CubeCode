package com.cubecode.api.scripts;

import com.cubecode.CubeCode;
import com.cubecode.api.files.FileManager;
import com.cubecode.api.scripts.code.JavaScriptUtils;
import com.cubecode.api.scripts.code.JavaUtils;
import com.cubecode.api.scripts.code.ScriptFactory;
import com.cubecode.api.utils.DirectoryManager;
import com.cubecode.client.views.idea.utils.Extension;
import com.cubecode.client.views.idea.utils.node.*;
import com.cubecode.utils.CubeCodeException;
import dev.latvian.mods.rhino.*;
import dev.latvian.mods.rhino.mod.util.RemappingHelper;
import dev.latvian.mods.rhino.util.Remapper;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ProjectManager extends DirectoryManager {
    public static final Remapper remapper = RemappingHelper.getMinecraftRemapper();
    public static final Context globalContext = Context.enter();
    static final public ScriptScope globalScope = new ScriptScope("CubeCode global scope", globalContext);

    public static final String DEFAULT_SCRIPT = "function main(c) {\n    c.server.send(\"Hello World!\", true)\n}";

    private Set<Script> scripts = new HashSet<>();
    private List<IdeaNode> nodes = new ArrayList<>();

    public ProjectManager(File scriptsDirectory) {
        super(scriptsDirectory);

        globalContext.setRemapper(remapper);
        globalContext.setApplicationClassLoader(ProjectManager.class.getClassLoader());
        globalScope.setParentScope(globalContext.initStandardObjects());

        globalScope.set("CubeCode", new ScriptFactory());
        globalScope.set("Java", new JavaUtils(globalContext, globalScope));
        globalScope.set("JavaScript", new JavaScriptUtils(globalContext, globalScope, this.getDirectory()));

        this.updateScriptsFromFiles();
        this.updateIdeaNodesFromFiles();

        //this.nodes = NodeUtils.scriptsToIdeaNodes(this.scripts.stream().toList());
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

    public void saveScript(ScriptNode scriptNode) {
        String path = CubeCode.projectManager.getDirectory().toPath().resolve(scriptNode.getPath().substring(1)).toString();
        FileManager.writeJsonToFile(path, scriptNode.getScript().code);
    }

    public void renameFile(String path, String name) {
        Path resolve = CubeCode.projectManager.getDirectory().toPath().resolve(path);
        File file = resolve.toFile();

        File renamedFile = new File(file.getParent(), name);

        file.renameTo(renamedFile);
    }

    public void deleteFile(String path, NodeType node) {
        try {
            Path resolve = CubeCode.projectManager.getDirectory().toPath().resolve(path);
            if (node == NodeType.FOLDER) {
                FileUtils.deleteDirectory(resolve.toFile());
            } else {
                Files.delete(resolve);
            }

            this.nodes.remove(NodeUtils.findNodeByPath(this.nodes, path));
        } catch (IOException ignored) {
        }
    }

    public void createScript(String name, String path, String content) {
        Path scriptsPath = CubeCode.projectManager.getDirectory().toPath();

        File scriptFile = scriptsPath.resolve(path.isEmpty() ? "" : path.substring(1)).resolve(name).toFile();

        try (FileWriter writer = new FileWriter(scriptFile)) {
            ScriptNode scriptNode = new ScriptNode(new Script(name, content), Extension.JAVASCRIPT);

            if (path.isEmpty()) {
                this.nodes.add(scriptNode);
            } else {
                IdeaNode nodeByPath = NodeUtils.findNodeByPath(this.nodes, path);

                ((FolderNode) nodeByPath).addChild(scriptNode);
            }

            writer.write(content);
        } catch (IOException ignored) {
        }
    }

    public void createFolder(String name, String path) {
        Path scriptsPath = CubeCode.projectManager.getDirectory().toPath();

        if (scriptsPath.resolve(path).resolve(name).toFile().mkdir()) {
            if (path.isEmpty()) {
                this.nodes.add(new FolderNode(name));
            } else {
                IdeaNode nodeByPath = NodeUtils.findNodeByPath(this.nodes, path);

                ((FolderNode) nodeByPath).addChild(new FolderNode(name));
            }
        }
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
                    createScript(scriptNode.getScript().name, folderNode.getPath(), scriptNode.getScript().code);
                }
            }
        }
    }

    public void updateScriptFromFile(String scriptName) {
        this.getFiles().forEach(file -> {
            if (file.getName().equals(scriptName)) {
                this.getScript(scriptName).code = this.readFileToString(file.getName());
            }
        });
    }

    public void updateScriptsFromFiles() {
        Set<Script> newScripts = new HashSet<>();
        scanDirectory(this.getFiles(), newScripts);
        this.scripts = newScripts;
    }

    private void scanDirectory(Collection<File> files, Set<Script> scripts) {
        for (File file : files) {
            if (file.isDirectory()) {
                scanDirectory(Arrays.asList(file.listFiles()), scripts);
            } else {
                if (isValidScriptFile(file)) {
                    String relativePath = getRelativePath(file);
                    scripts.add(new Script(relativePath, this.readFileToString(file.getPath())));
                }
            }
        }
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
            } else if (isValidScriptFile(file)) {
                String relativePath = getRelativePath(file);
                String scriptContent = readFileToString(file.getPath());

                ideaNodes.add(new ScriptNode(new Script(fileName, scriptContent), Extension.JAVASCRIPT, "/"+relativePath));
            }
        }
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

    private String getRelativePath(File file) {
        String path = file.getPath();
        String base = CubeCode.cubeCodeDirectory.getPath() + "\\project";
        return new File(base).toURI().relativize(new File(path).toURI()).getPath();
    }

    public Script getScript(String scriptName) {
        return this.scripts.stream().filter(script -> script.name.equals(scriptName)).findFirst().orElse(null);
    }

    public List<Script> getScripts() {
        return this.scripts.stream().toList();
    }

    public List<IdeaNode> getNodes() {
        return this.nodes;
    }
}