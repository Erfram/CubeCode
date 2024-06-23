package com.cubecode.utils;

public class CubeCodeException extends Exception {

    public final String sourceName;

    public CubeCodeException(String message, String sourceName) {
        super(message);
        this.sourceName = sourceName;
    }

    public CubeCodeException(String message) {
        super(message);
        this.sourceName = "";
    }

}
