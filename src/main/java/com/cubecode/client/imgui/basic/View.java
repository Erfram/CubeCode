package com.cubecode.client.imgui.basic;

import java.util.UUID;

public abstract class View {

    private boolean isInit;
    protected final UUID uniqueID;
    protected Theme theme;

    public View(Theme theme) {
        this.uniqueID = UUID.randomUUID();
        this.theme = theme;
    }

    /**
     * @return optional parameter for rendering profiling
     */
    protected String getName() {
        return String.format("%s##%s", this.getClass().getSimpleName(), uniqueID);
    }

    /**
     * @return the styling for {@link View#render()}
     */
    public Theme getTheme() {
        return theme;
    }

    /**
     * Wrapper {@link View#init()} for runtime initialization
     */
    protected final void loop() {
        if (!isInit) {
            this.init();
            this.isInit = true;
        }

        this.render();
    }

    /**
     * Initialization, such as window positions and other states
     */
    public void init() {

    }

    /**
     * Stores the render implementation for the current task
     */
    public void render() {

    }

}