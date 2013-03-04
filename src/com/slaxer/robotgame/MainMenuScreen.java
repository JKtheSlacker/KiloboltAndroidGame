package com.slaxer.robotgame;

import java.util.List;

import com.slaxer.framework.Game;
import com.slaxer.framework.Graphics;
import com.slaxer.framework.Input.TouchEvent;
import com.slaxer.framework.Screen;

public class MainMenuScreen extends Screen {

	public MainMenuScreen(Game game) {
		super(game);
	}

	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

		int touchEventsListSize = touchEvents.size();
		for (int touchEventIndex = 0; touchEventIndex < touchEventsListSize; touchEventIndex++) {
			TouchEvent event = touchEvents.get(touchEventIndex);
			if (event.type == TouchEvent.TOUCH_UP) {
				if (inBounds(event, 50, 350, 250, 450)) {
					game.setScreen(new GameScreen(game));
				}
			}
		}

	}

	private boolean inBounds(TouchEvent event, int x, int y, int width,
			int height) {
		if (event.x > x && event.x < x + width - 1 && event.y > y
				&& event.y < y + height - 1)
			return true;
		else
			return false;
	}

	@Override
	public void paint(float deltaTime) {
		Graphics g = game.getGraphics();
		g.drawImage(Assets.menu, 0, 0);

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void backButton() {
		// The user seems to want to quit.
		android.os.Process.killProcess(android.os.Process.myPid());

	}

}
