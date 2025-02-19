package com.cubecode.client.imgui.basic;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public class ImGuiFrameBuffer {
    private int FBO = 0;
    int texture = GL11.glGenTextures();
    private static int previousFrameBuffer = 0;
    private int[] previousViewport = new int[4];
    private int width = 800;
    private int height = 400;

    public ImGuiFrameBuffer() {
        init();
    }

    private void init() {
        FBO = GL30.glGenFramebuffers();
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, FBO);

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA,width,height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (java.nio.ByteBuffer) null);
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, texture, 0);

        int RBO = GL30.glGenRenderbuffers();
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, RBO);
        GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL30.GL_DEPTH_COMPONENT32,width, height);
        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, RBO);

        if(GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER) != GL30.GL_FRAMEBUFFER_COMPLETE) {
            throw new RuntimeException("Error initializing framebuffer");
        }
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
    }

    public void update(int width, int height) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (java.nio.ByteBuffer) null);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);


        bind();
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, texture, 0);
        unbind();

        if(GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER) != GL30.GL_FRAMEBUFFER_COMPLETE) {
            throw new RuntimeException("Error initializing framebuffer");
        }

        this.width = width;
        this.height = height;
    }

    public void bind() {
        GL11.glGetIntegerv(GL11.GL_VIEWPORT, previousViewport);
        previousFrameBuffer = GL30.glGetInteger(GL30.GL_FRAMEBUFFER_BINDING);
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, FBO);
        GL11.glViewport(0, 0, width, height);
    }

    public void unbind() {
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, previousFrameBuffer);
        GL11.glViewport(previousViewport[0], previousViewport[1], previousViewport[2], previousViewport[3]);
        previousFrameBuffer = 0;
    }

    public void render(int width, int height, Runnable runnable) {
        this.update(width, height);
        this.bind();
        GL30.glClearColor(1,1,1,0.2f);
        GL30.glClear(GL30.GL_COLOR_BUFFER_BIT);
        runnable.run();
        this.unbind();
    }

    public int getTexture() {
        return texture;
    }
}