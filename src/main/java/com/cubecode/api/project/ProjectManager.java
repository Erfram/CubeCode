package com.cubecode.api.project;

import com.cubecode.api.project.nodes.FileNode;
import com.cubecode.api.project.nodes.FolderNode;
import com.cubecode.api.project.nodes.IDENode;
import com.cubecode.api.project.nodes.ScriptNode;
import com.cubecode.api.project.scripts.Script;
import com.cubecode.api.project.scripts.ScriptExecutor;
import com.cubecode.utils.DirectoryManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ProjectManager extends DirectoryManager {
    public ScriptExecutor executor;

    List<Script> scripts = new ArrayList<>();
    List<IDENode> nodes = new ArrayList<>();

    public ProjectManager(File projectDirectory) {
        super(projectDirectory);

        this.executor = new ScriptExecutor();

        this.loadNodesAndScripts();
    }

    public void loadNodesAndScripts() {
        this.nodes = new ArrayList<>();
        this.scripts = new ArrayList<>();
        this.scanDirectory(this.getFiles(), this.nodes);
    }

    private void scanDirectory(Set<File> files, List<IDENode> nodes) {
        files.forEach(file -> {
            String fileName = file.getName();

            if (file.isDirectory()) {
                FolderNode folderNode = new FolderNode(fileName);
                this.scanDirectory(Set.of(file.listFiles()), folderNode.getChildren());
                nodes.add(folderNode);
            } else {
                String fileExtension = this.getFileExtension(file);
                String filePath = this.getRelativePath(file);

                if (!fileExtension.equals("js")) {
                    FileNode fileNode = new FileNode(fileName, fileExtension, "/"+filePath);
                    nodes.add(fileNode);
                } else {
                    String scriptCode = this.readFile(filePath);
                    Script.Type scriptSide = Script.Type.SERVER;

                    Script script = new Script(filePath, scriptCode, scriptSide);

                    this.scripts.add(script);
                    nodes.add(new ScriptNode(fileName, script, "/" + filePath));
                }
            }
        });
    }

    public List<IDENode> getNodes() {
        return this.nodes;
    }

    public Script getScript(String name) {
        for (Script script : this.scripts) {
            if (script.getName().equals(name)) {
                return script;
            }
        }

        return null;
    }

    public void setScriptCode(String path, String code) {
        this.writeFile(path, code);
        this.loadNodesAndScripts();
    }

    public void createScript(String path, String code) {
        this.writeFile(path, code);
        this.loadNodesAndScripts();
    }

    public void createScript(String path) {
        this.writeFile(path, ScriptExecutor.DEFAULT_SCRIPT);
        this.loadNodesAndScripts();
    }

    public void createFolder(String path) {
        super.createDirectory(path);
        this.loadNodesAndScripts();
    }
}