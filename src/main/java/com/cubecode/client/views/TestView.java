package com.cubecode.client.views;

import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.scripts.code.ui.components.AbstractComponent;

import java.util.*;

public class TestView extends View {
    public LinkedList<Runnable> runnables = new LinkedList<>();

    public List<AbstractComponent> components = new LinkedList<>();

    @Override
    public void init() {

    }

    @Override
    public void render() {
        this.runnables.forEach(Runnable::run);
    }

    @Override
    public void onClose() {

    }
}
