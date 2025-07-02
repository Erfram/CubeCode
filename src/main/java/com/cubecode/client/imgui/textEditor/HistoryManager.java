package com.cubecode.client.imgui.textEditor;

import java.util.List;
import java.util.Stack;

public class HistoryManager {
    public Stack<History> undoStack;
    public Stack<History> redoStack;

    public HistoryManager() {
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
    }

    public void addHistory(List<String> lines, CubeTextEditor2.Position cursorPos, CubeTextEditor2.Selection selection) {
        this.undoStack.add(new History(lines, cursorPos, selection));
    }

    public History undo() {
        if (!this.undoStack.empty()) {
            History history = this.undoStack.pop();
            this.redoStack.push(history);
            return history;
        }
        return null;
    }

    public History redo() {
        if (!this.redoStack.empty()) {
            History history = this.redoStack.pop();
            this.undoStack.push(history);
            return history;
        }
        return null;
    }

    public static class History {
        public List<String> lines;
        public CubeTextEditor2.Position cursorPos;
        public CubeTextEditor2.Selection selection;

        public History(List<String> lines, CubeTextEditor2.Position cursorPos, CubeTextEditor2.Selection selection) {
            this.lines = lines;
            this.cursorPos = cursorPos;
            this.selection = selection;
        }
    }
}
