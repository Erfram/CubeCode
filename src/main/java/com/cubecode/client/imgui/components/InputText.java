package com.cubecode.client.imgui.components;

import com.cubecode.client.imgui.CubeImGui;
import com.cubecode.client.imgui.basic.View;

import java.util.function.Consumer;

public class InputText implements Component {
	private String label;
	private Consumer<String> inputTextAction;

	public static InputText create() {
		return new InputText();
	}

	public InputText label(String title) {
		this.label = title;
		return this;
	}

	public InputText onInputTextAction(Consumer<String> inputTextAction) {
		this.inputTextAction = inputTextAction;
		return this;
	}

	@Override
	public void render(View view) {
		CubeImGui.inputText(view, this.label, inputTextAction);
	}

}
