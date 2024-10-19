package com.cubecode.client.imgui.basic;

public class WindowData {
    private float[] position;
    private float[] size;
    private boolean collapsed;

    public WindowData(float[] position, float[] size, boolean collapsed) {
        this.position = position;
        this.size = size;
        this.collapsed = collapsed;
    }

    public WindowData(float positionX, float positionY, float sizeX, float sizeY, boolean collapsed) {
        this.position = new float[]{positionX, positionY};
        this.size = new float[]{sizeX, sizeY};
        this.collapsed = collapsed;
    }

    public float[] getPosition() {
        return position;
    }

    public float[] getSize() {
        return size;
    }

    public boolean isCollapsed() {
        return collapsed;
    }
}