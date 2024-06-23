package com.cubecode.client.editor;

import imgui.ImGui;
import org.treesitter.*;

import java.util.Timer;
import java.util.TimerTask;

public class CodeEditor {

    private String fullCode;
    private TSTree beforeTree;
    private TimerTask currentTask = null;
    private final Timer timer = new Timer();

    /**
     * debounce timeout
     */
    private final long delay = 175;

    public void render() {
        ImGui.text("Hello World!");
    }

    private void debounce() {
        if (currentTask != null) {
            currentTask.cancel();
        }

        currentTask = new TimerTask() {
            @Override
            public void run() {
                handleEditorChanged();
            }
        };
        timer.schedule(currentTask, delay);
    }

    private void handleEditorChanged() {
        TSParser parser = new TSParser();
        parser.setLanguage(new TreeSitterJavascript());

        TSTree tree = parser.parseString(beforeTree, fullCode);
        beforeTree = tree;

        TSNode rootNode = tree.getRootNode();
        TSNode arrayNode = rootNode.getNamedChild(0);
        TSNode numberNode = arrayNode.getNamedChild(0);

    }

}
