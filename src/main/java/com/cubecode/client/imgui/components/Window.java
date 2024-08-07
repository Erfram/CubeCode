package com.cubecode.client.imgui.components;

import com.cubecode.client.config.CubeCodeConfig;
import com.cubecode.client.imgui.CubeImGui;
import com.cubecode.client.imgui.basic.ImGuiLoader;
import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.imgui.components.basic.Component;
import imgui.ImColor;
import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.type.ImBoolean;

import java.util.ArrayList;
import java.util.List;

public class Window {
	private String title = "default";
	private int flags = 0;
	private final List<Component> components = new ArrayList<>();
	private Runnable callback = () -> {};

	public static Window create() {
		return new Window();
	}

	public Window title(String title) {
		this.title = title;
		return this;
	}

	public Window flags(int windowFlags) {
		this.flags = windowFlags;
		return this;
	}

	public Window draw(Component... components) {
		this.components.addAll(List.of(components));
		return this;
	}

	public Window callback(Runnable callback) {
		this.callback = callback;
		return this;
	}

	public void render(View view) {
		 /* @DYAMO FIXME: check title validity, presence of `drawAction` and other possibly inconsistent properties.
		 * This is equivalent of builder's build method that verifies consistency of the built object.
		 */

		view.putVariable(this.title + view.getUniqueID(), new ImBoolean(true));
		ImBoolean close = view.getVariable(this.title + view.getUniqueID());

		CubeCodeConfig.WindowConfig windowConfig = CubeCodeConfig.getWindowConfig();

		ImGui.pushStyleColor(ImGuiCol.WindowBg, ImColor.rgba(windowConfig.bgColor[0], windowConfig.bgColor[1], windowConfig.bgColor[2], windowConfig.bgColor[3]));

		ImGui.pushStyleColor(ImGuiCol.TitleBg, ImColor.rgba(windowConfig.titleColor[0], windowConfig.titleColor[1], windowConfig.titleColor[2], windowConfig.titleColor[3]));
		ImGui.pushStyleColor(ImGuiCol.TitleBgActive, ImColor.rgba(windowConfig.titleActiveColor[0], windowConfig.titleActiveColor[1], windowConfig.titleActiveColor[2], windowConfig.titleActiveColor[3]));
		ImGui.pushStyleColor(ImGuiCol.TitleBgCollapsed, ImColor.rgba(windowConfig.titleCollapsedColor[0], windowConfig.titleCollapsedColor[1], windowConfig.titleCollapsedColor[2], windowConfig.titleCollapsedColor[3]));

		ImGui.pushStyleColor(ImGuiCol.Border, ImColor.rgba(windowConfig.borderColor[0], windowConfig.borderColor[1], windowConfig.borderColor[2], windowConfig.borderColor[3]));

		ImGui.pushStyleColor(ImGuiCol.Button, ImColor.rgba(windowConfig.buttonColor[0], windowConfig.buttonColor[1], windowConfig.buttonColor[2], windowConfig.buttonColor[3]));
		ImGui.pushStyleColor(ImGuiCol.ButtonActive, ImColor.rgba(windowConfig.buttonActiveColor[0], windowConfig.buttonActiveColor[1], windowConfig.buttonActiveColor[2], windowConfig.buttonActiveColor[3]));
		ImGui.pushStyleColor(ImGuiCol.ButtonHovered, ImColor.rgba(windowConfig.buttonHoveredColor[0], windowConfig.buttonHoveredColor[1], windowConfig.buttonHoveredColor[2], windowConfig.buttonHoveredColor[3]));

		ImGui.pushStyleColor(ImGuiCol.FrameBg, ImColor.rgba(windowConfig.frameBgColor[0], windowConfig.frameBgColor[1], windowConfig.frameBgColor[2], windowConfig.frameBgColor[3]));
		ImGui.pushStyleColor(ImGuiCol.FrameBgActive, ImColor.rgba(windowConfig.frameBgActiveColor[0], windowConfig.frameBgActiveColor[1], windowConfig.frameBgActiveColor[2], windowConfig.frameBgActiveColor[3]));
		ImGui.pushStyleColor(ImGuiCol.FrameBgHovered, ImColor.rgba(windowConfig.frameBgHoveredColor[0], windowConfig.frameBgHoveredColor[1], windowConfig.frameBgHoveredColor[2], windowConfig.frameBgHoveredColor[3]));

		ImGui.pushStyleColor(ImGuiCol.SliderGrab, ImColor.rgba(windowConfig.sliderGrabColor[0], windowConfig.sliderGrabColor[1], windowConfig.sliderGrabColor[2], windowConfig.sliderGrabColor[3]));
		ImGui.pushStyleColor(ImGuiCol.SliderGrabActive, ImColor.rgba(windowConfig.sliderGrabActiveColor[0], windowConfig.sliderGrabActiveColor[1], windowConfig.sliderGrabActiveColor[2], windowConfig.sliderGrabActiveColor[3]));

		if (ImGui.begin(this.title, close, this.flags)) {
			if (!close.get()) {
				ImGuiLoader.removeView(view);
			}

			CubeImGui.manageDocking(view);

			this.callback.run();

			for (Component component : this.components) {
				component.render(view);
			}
		}

		ImGui.end();
		ImGui.popStyleColor(13);
	}
}