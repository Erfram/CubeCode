package com.cubecode.api.scripts.code;

import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.ScriptableObject;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JavaScriptUtils {
    Context context;
    ScriptableObject scope;
    String projectPath;

    public JavaScriptUtils(Context context, ScriptableObject scope, File directory) {
        this.context = context;
        this.scope = scope;
        this.projectPath = directory.getPath();
    }

    public void loadScript(String scriptPath) {
        try {
            URL url = new URL(scriptPath);

            this.context.evaluateString(this.scope, new String(url.openStream().readAllBytes(), StandardCharsets.UTF_8), scriptPath, 1, null);
        } catch (MalformedURLException malformedURLException) {
            try {
                String script = Files.readString(Paths.get(this.projectPath + "/" + scriptPath));
                this.context.evaluateString(this.scope, script, scriptPath, 1, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException ignored) {
        }
    }
}