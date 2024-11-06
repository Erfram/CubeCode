package com.cubecode.utils;

public interface Script {
    String getName();
    String getCode();
    ScriptSide getSide();

    void setName(String name);
    void setCode(String code);
    void setSide(ScriptSide side);
}
