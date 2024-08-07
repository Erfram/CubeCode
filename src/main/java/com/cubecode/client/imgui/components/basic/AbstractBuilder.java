package com.cubecode.client.imgui.components.basic;

import com.cubecode.utils.CubeCodeException;

public class AbstractBuilder<Bld> {
    protected final CommonProperties commonProperties;

    public AbstractBuilder(CommonProperties commonProperties) {
        this.commonProperties = commonProperties;
    }

    public AbstractBuilder() {
        this.commonProperties = new CommonProperties();
    }

    public Bld w(float width) {
        this.commonProperties.width = width;
        return self();
    }

    public Bld h(float height) {
        this.commonProperties.height = height;
        return self();
    }

    public Bld wh(float width, float height) {
        this.commonProperties.width = width;
        this.commonProperties.height = height;
        return self();
    }

    public Bld rw(float relativeWidth) {
        this.commonProperties.rw = relativeWidth;
        return self();
    }

    public Bld rh(float relativeHeight) {
        this.commonProperties.rh = relativeHeight;
        return self();
    }

    public Bld rwh(float relativeWidth, float relativeHeight) {
        this.commonProperties.rw = relativeWidth;
        this.commonProperties.rh = relativeHeight;
        return self();
    }

    public Bld x(float x) {
        this.commonProperties.x = x;
        return self();
    }

    public Bld y(float y) {
        this.commonProperties.y = y;
        return self();
    }

    public Bld xy(float x, float y) {
        this.commonProperties.x = x;
        this.commonProperties.y = y;
        return self();
    }

    public Bld rx(float rx) {
        this.commonProperties.rx = rx;
        return self();
    }

    public Bld ry(float ry) {
        this.commonProperties.ry = ry;
        return self();
    }

    public Bld rxy(float rx, float ry) {
        this.commonProperties.rx = rx;
        this.commonProperties.ry = ry;
        return self();
    }

    private Bld self() {
        return (Bld) this;
    }
}