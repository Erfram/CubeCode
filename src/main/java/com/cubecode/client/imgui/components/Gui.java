package com.cubecode.client.imgui.components;

import com.cubecode.client.imgui.CubeImGui;
import com.cubecode.client.imgui.basic.View;

public class Gui {
	private String title;
	private Runnable drawAction;

	public static Gui create() {
		return new Gui();
	}

	public Gui title(String title) {
		this.title = title;
		return this;
	}

	public Gui draw(Runnable drawAction) {
		this.drawAction = drawAction;
		return this;
	}

	public void render(View view) {
		/* FIXME: check title validity, presence of `drawAction` and other possibly inconsistent properties.
		 * This is equivalent of builder's build method that verifies consistency of the built object.
		 */

		CubeImGui.begin(view, this.title, close -> {
			drawAction.run();
		});
	}

	public static void test(View guiView) {
		Gui.create()
				.title("My cool GUI")
				.draw(() -> {
//				Box.create()
//					.width(100)
//					.draw(() -> {
//						InputText.create()
//							.label("")
//							.render(guiView);
//					})
//					.render(guiView);
					InputText.create()
							.label("")
							.render(guiView);
				})
				.render(guiView);
	}

}