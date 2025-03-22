package com.cubecode.client.scripts;

import com.cubecode.api.scripts.ProjectManager;
import com.cubecode.api.scripts.ScriptScope;
import com.cubecode.api.scripts.code.JavaUtils;
import com.cubecode.api.scripts.code.ScriptFactory;
import com.cubecode.client.views.ide.utils.node.IdeaNode;
import com.cubecode.utils.CubeCodeException;
import com.cubecode.utils.ScriptType;
import dev.latvian.mods.rhino.*;
import dev.latvian.mods.rhino.mod.util.RemappingHelper;
import dev.latvian.mods.rhino.util.Remapper;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ClientProjectManager {
    public static final Remapper remapper = RemappingHelper.getMinecraftRemapper();
    public static final Context globalContext = Context.enter();
    public static final ScriptScope globalScope = new ScriptScope("CubeCode client global scope", globalContext);

    public static final String DEFAULT_SCRIPT = "function client(c) {\n\n}";

    private Set<ClientScript> scripts = new HashSet<>();
    private List<IdeaNode> nodes = new ArrayList<>();

    public ClientProjectManager() {
        globalContext.setRemapper(remapper);
        globalContext.setApplicationClassLoader(ProjectManager.class.getClassLoader());
        globalContext.setMaximumInterpreterStackDepth(500);
        globalScope.setParentScope(globalContext.initStandardObjects());

        globalScope.set("CubeCode", new ScriptFactory());
        globalScope.set("Java", new JavaUtils(globalContext, globalScope));
    }

    @Nullable
    public ClientScript getScript(String scriptName) {
        for (ClientScript script : this.scripts) if (script.getName().equals(scriptName)) {
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

    public void createScript(ClientScript script) {
        this.scripts.add(script);
    }

    public void createScripts(List<ClientScript> scripts) {
        this.scripts.addAll(scripts);
    }

    public void setScripts(List<ClientScript> scripts) {
        this.scripts = new HashSet<>(scripts);
    }

    public void setScriptSide(String name, ScriptType side) {
        ClientScript script = this.getScript(name);
        if (script != null) {
            script.setSide(side);
        }
    }

    public void addLibraryScript(String name, String library) {
        ClientScript script = this.getScript(name);

        if(script != null) {
            script.addLibraryScript(library);
        }
    }

    public void removeLibraryScript(String name, String library) {
        ClientScript script = this.getScript(name);

        if(script != null) {
            script.removeLibraryScript(library);
        }
    }
}