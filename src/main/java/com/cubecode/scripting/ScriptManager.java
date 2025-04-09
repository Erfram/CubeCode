package com.cubecode.scripting;

import com.cubecode.CubeCode;
import com.cubecode.api.scripts.Script;
import com.cubecode.client.views.ide.utils.node.FolderNode;
import com.cubecode.client.views.ide.utils.node.IdeaNode;
import com.cubecode.client.views.ide.utils.node.ScriptNode;
import com.cubecode.utils.GsonManager;
import com.cubecode.utils.manager.DirectoryManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ScriptManager extends DirectoryManager {
    protected final ConcurrentHashMap<String, Script> scripts = new ConcurrentHashMap<>();
    private final List<IdeaNode> nodes = new ArrayList<>();

    public ScriptManager(File dir) {
        super(dir);
        this.fillNodes(this.nodes);
    }

    public void fillNodes(List<IdeaNode> nodes) {
        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                FolderNode folderNode = new FolderNode(file.getName());
                nodes.add(folderNode);
                this.fillNodes(folderNode.getChildren());
            } else if (file.isFile() && file.getName().endsWith(".script")) {
                Script script = GsonManager.readJSON(file, Script.class);
                this.scripts.put(script.getUUID(), script);
                nodes.add(new ScriptNode(script));
            }
        }
    }

    public void readScript(File file) {
        Script script = GsonManager.readJSON(file, Script.class);
        if (script.getUUID().isEmpty()) {
            script.setUUID(UUID.randomUUID().toString());
        }
        this.scripts.put(script.getUUID(), script);
    }

    public void saveScript(Script script, String relativePath) {
        GsonManager.writeJSON(new File(this.directory, relativePath + script.getName()), script);
    }

    public Script getScript(String uuid) {
        return this.scripts.get(uuid);
    }

    public void putScript(Script script) {
        this.scripts.put(script.getUUID(), script);
    }

    public List<Script> getScripts() {
        return this.scripts.values().stream().toList();
    }

    public List<IdeaNode> getNodes() {
        return this.nodes;
    }

    public void handleScriptExecutionResult(ScriptExecutionResult result) {
        if (result.error) {
            CubeCode.loggerManager.error("", result.errorMessage);
        }
    }
}
