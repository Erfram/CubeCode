package com.cubecode.utils;

import java.util.List;

public interface Script {
    String getName();
    String getCode();
    ScriptType getSide();
    List<String> getLibraries();
    boolean hasLibraryScript(String scriptName);

    void setName(String name);
    void setCode(String code);
    void setSide(ScriptType side);
    void setLibraries(List<String> libraries);
    void addLibraryScript(String scriptName);
    void removeLibraryScript(String scriptName);
}