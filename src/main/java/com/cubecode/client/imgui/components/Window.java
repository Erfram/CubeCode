package com.cubecode.client.imgui.components;

import com.cubecode.CubeCodeClient;
import com.cubecode.client.config.CubeCodeConfig;
import com.cubecode.client.imgui.CubeImGui;
import com.cubecode.client.imgui.basic.ImGuiLoader;
import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.imgui.components.basic.Component;
import imgui.*;
import imgui.flag.ImGuiCol;
import imgui.type.ImBoolean;

import java.util.ArrayList;
import java.util.List;

public class Window {
	private String title = "default";
	private int flags = 0;
	private final List<Component> components = new ArrayList<>();
	private Runnable callback = () -> {};
	private Runnable onExit = () -> {};

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

	public Window onExit(Runnable callback) {
		this.onExit = callback;
		return this;
	}

	public void render(View view) {
		 /* @DYAMO FIXME: check title validity, presence of `drawAction` and other possibly inconsistent properties.
		 * This is equivalent of builder's build method that verifies consistency of the built object.
		 */

		view.putVariable(this.title + view.getUniqueID(), new ImBoolean(true));
		ImBoolean close = view.getVariable(this.title + view.getUniqueID());

		if (ImGui.begin(this.title, close, this.flags)) {
			if (!close.get()) {
				this.onExit.run();
				ImGuiLoader.removeView(view);
			} else {
				CubeImGui.manageDocking(view);

				this.callback.run();

				for (Component component : this.components) {
					component.render(view);
				}
			}
		}
		ImGui.end();
	}
}