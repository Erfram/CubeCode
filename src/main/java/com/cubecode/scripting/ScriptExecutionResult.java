package com.cubecode.scripting;

public class ScriptExecutionResult {
    public boolean error = false;
    public int errorLine = -1;
    public String errorMessage = "";
    public String result = "";
    public String source = "";

    public ScriptExecutionResult(String result) {
        this.result = result;
    }

    public void setError(int errorLine, String errorMessage) {
        this.error = true;
        this.errorLine = errorLine;
        this.errorMessage = errorMessage;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
