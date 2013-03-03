package com.slaxer.robotgame;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import android.graphics.Color;
import android.graphics.Paint;

import com.slaxer.framework.Game;
import com.slaxer.framework.Graphics;
import com.slaxer.framework.Image;
import com.slaxer.framework.Input.TouchEvent;
import com.slaxer.framework.Screen;
import com.slaxer.robotgame.framework.Animation;

public class GameScreen extends Screen {
	enum GameState {
		Ready, Running, Paused, GameOver
	}

	GameState state = GameState.Ready;

	private static Background bg1, bg2;
	private static Robot robot;
	public static Heliboy hb, hb2;
	private Image currentSprite, character, character2, character3;
	private Image heliboy, heliboy2, heliboy3, heliboy4, heliboy5;
	private Animation anim, hanim;

	private ArrayList tileArray = new ArrayList();

	int livesLeft = 1;
	Paint paint, paint2;

	public GameScreen(Game game) {
		super(game);

		// Initialize game objects
		bg1 = new Background(0, 0);
		bg2 = new Background(2160, 0);
		robot = new Robot();
		hb = new Heliboy(340, 360);
		hb2 = new Heliboy(700, 360);

		character = Assets.character;
		character2 = Assets.character2;
		character3 = Assets.character3;

		heliboy = Assets.heliboy;
		heliboy2 = Assets.heliboy2;
		heliboy3 = Assets.heliboy3;
		heliboy4 = Assets.heliboy4;
		heliboy5 = Assets.heliboy5;

		anim = new Animation();
		anim.addFrame(character, 1250);
		anim.addFrame(character2, 50);
		anim.addFrame(character3, 50);
		anim.addFrame(character2, 50);

		hanim = new Animation();
		hanim.addFrame(heliboy, 100);
		hanim.addFrame(heliboy2, 100);
		hanim.addFrame(heliboy3, 100);
		hanim.addFrame(heliboy4, 100);
		hanim.addFrame(heliboy5, 100);
		hanim.addFrame(heliboy4, 100);
		hanim.addFrame(heliboy3, 100);
		hanim.addFrame(heliboy2, 100);

		currentSprite = anim.getImage();

		loadMap();

		// Define a paint object to use
		paint = new Paint();
		paint.setTextSize(100);
		paint.setTextAlign(Paint.Align.CENTER);
		paint.setAntiAlias(true);
		paint.setColor(Color.WHITE);

		paint2 = new Paint();
		paint2.setTextSize(100);
		paint2.setTextAlign(Paint.Align.CENTER);
		paint2.setAntiAlias(true);
		paint2.setColor(Color.WHITE);

	}

	private void loadMap(){
		ArrayList lines = new ArrayList();
		int width = 0;
		int height = 0;
		
		Scanner mapScanner = new Scanner(SampleGame.map);
		while(mapScanner.hasNextLine()){
			String currentLine = mapScanner.nextLine();
			
			// Out of lines? Abort! Abort!
			if(currentLine == null)
				break;
			
			// An exclamation mark indicates a comment
			// Anything else SHOULD BE a valid map layout definition
			if(!currentLine.startsWith("!")){
				lines.add(currentLine);
				// This way, we know how wide to make the map.
				width = Math.max(width, currentLine.length());
			}
			
		}
		// Number of lines should indicate our height in tiles
		height = lines.size();
		
		// The screen will be 12 tiles high.
		for(int mapYIndex = 0; mapYIndex < 12; mapYIndex++){
			String currentLine = (String) lines.get(mapYIndex);
			// Zip through the entirety of each line
			for(int mapXIndex = 0; mapXIndex < width; mapXIndex++){
				if(mapXIndex < currentLine.length()){
					char tileValue = currentLine.charAt(mapXIndex);
					Tile tile = New Tile(mapXIndex, mapYIndex, Character.getNumericValue(tileValue));
					tileArray.add(tile);
				}
				
			}
		}
	}

	@Override
	public void update(float deltaTime) {
		List touchEvents = game.getInput().getTouchEvents();

		// We have four separate update methods in this example.
		// Depending on the state of the game, we call different
		// update methods. Refer to Unit 3's code. We did a similar
		// thing without separating the update method.

		// Note that order is important here. If the game is
		// in Ready state, we should only go to Running.
		// From Running, we should only go to Paused or GameOver.

		if (state == GameState.Ready)
			updateReady(touchEvents);
		if (state == GameState.Running)
			updateRunning(touchEvents, deltaTime);
		if (state == GameState.Paused)
			updatePaused(touchEvents);
		if (state == GameState.GameOver)
			updateGameOver(touchEvents);

	}

	private void updateReady(List touchEvents) {

		// This example starts with a "Ready" screen.
		// When the user touches the screen, the game
		// begins. state now becomes GameState.Running.
		// Now the updateRunning() method will be called!

		if (touchEvents.size() > 0)
			state = GameState.Running;

	}

	private void updateRunning(List touchEvents, float deltaTime) {

		// This is identical to the update() method from Unit 2/3.

		// 1. All touch input is handled here:
		int touchEventsSize = touchEvents.size();
		for (int currentTouchEventIndex = 0; currentTouchEventIndex < touchEventsSize; currentTouchEventIndex++) {
			TouchEvent currentEvent = (TouchEvent) touchEvents
					.get(currentTouchEventIndex);
			if (currentEvent.type == TouchEvent.TOUCH_DOWN) {
				// Up button pressed
				if (inBounds(currentEvent, 0, 285, 65, 65)) {
					robot.jump();
					currentSprite = anim.getImage();
					robot.setDucked(false);
				}

				// Fire button pressed
				else if (inBounds(currentEvent, 0, 350, 65, 65)) {
					if (!robot.isDucked() && !robot.isJumped()
							&& robot.isReadyToFire()) {
						robot.shoot();
					}
				}

				// Down button pressed
				else if (inBounds(currentEvent, 0, 415, 65, 65)
						&& !robot.isJumped()) {
					currentSprite = Assets.characterDown;
					robot.setDucked(true);
					robot.setSpeedX(0);

				}

				if (currentEvent.x > 400) {
					// Move right
					robot.moveRight();
					robot.setMovingRight(true);
				}
			}

			if (currentEvent.type == TouchEvent.TOUCH_UP) {
				// Stop crouching
				if (inBounds(currentEvent, 0, 415, 65, 65)) {
					currentSprite = anim.getImage();
					robot.setDucked(false);
				}

				// Pause button pressed
				if (inBounds(currentEvent, 0, 0, 35, 35))
					pause();

				if (currentEvent.x > 400) {
					// Stop moving right
					robot.stopRight();
				}
			}

			// 2. Check miscellaneous events like death:

			if (livesLeft == 0)
				state = GameState.GameOver;

			// 3. Call individual update methods here.
			// This is where all the game updates happen.
			// For example, robot.update();
			robot.update();
			if (robot.isJumped())
				currentSprite = Assets.characterJump;
			else if (!robot.isJumped() && !robot.isDucked())
				currentSprite = anim.getImage();

			ArrayList projectiles = robot.getProjectiles();
			for (int projectileIndex = 0; projectileIndex < projectiles.size(); projectileIndex++) {
				Projectile projectile = (Projectile) projectiles
						.get(projectileIndex);
				if (projectile.isVisible())
					projectile.update();
				else
					projectiles.remove(projectileIndex);
			}

			updateTiles();
			hb.update();
			hb2.update();
			bg1.update();
			bg2.update();
			animate();

			// We have fallen to our death.
			if (robot.getCenterY() > 500) {
				state = GameState.GameOver;
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

	private void updatePaused(List touchEvents) {
		int touchEventsSize = touchEvents.size();
		for (int touchEventIndex = 0; touchEventIndex < touchEventsSize; touchEventIndex++) {
			TouchEvent currentEvent = (TouchEvent) touchEvents
					.get(touchEventIndex);
			// The user picks up their finger
			if (currentEvent.type == TouchEvent.TOUCH_UP)
				// Anywhere on the top of the screen except in the upper left
				// corner
				if (inBounds(currentEvent, 0, 0, 800, 240))
					if (!inBounds(currentEvent, 0, 0, 35, 35))
						// unPause
						resume();
			// If anywhere in the lower half of the screen
			if (inBounds(currentEvent, 0, 240, 800, 240)) {
				// Clean up and go to menu.
				nullify();
				goToMenu();
			}
		}

	}

	private void updateGameOver(List touchEvents) {
		int touchEventsSize = touchEvents.size();
		for (int touchEventIndex = 0; touchEventIndex < touchEventsSize; touchEventIndex++) {
			TouchEvent currentEvent = (TouchEvent) touchEvents(touchEventIndex);
			// If the user touches anywhere in the screen
			// while the game over screen is shown,
			// clean up and return to the menu (on a new game.)
			if (currentEvent.type == TouchEvent.TOUCH_DOWN) {
				if (inBounds(currentEvent, 0, 0, 800, 480)) {
					nullify();
					game.setScreen(new MainMenuScreen(game));
					return;
				}
			}
		}

	}

	private void updateTiles() {
		for (int tileIndex = 0; tileIndex < tileArray.size(); tileIndex++) {
			Tile tile = (Tile) tileArray.get(tileIndex);
			tile.update();
		}
	}

	@Override
	public void paint(float deltaTime) {
		Graphics g = game.getGraphics();

		// We start in Z-order: Background first, then map tiles:
		g.drawImage(Assets.background, bg1.getBgX(), bg1.getBgY());
		g.drawImage(Assets.background, bg2.getBgX(), bg2.getBgY());
		paintTiles(g);

		// Now we want the bullets:
		ArrayList projectiles = robot.getProjectiles();
		for (int projectileIndex = 0; projectileIndex < projectiles.size(); projectileIndex++) {
			Projectile projectile = (Projectile) projectiles
					.get(projectileIndex);
			g.drawRect(projectile.getX(), projectile.getY(), 10, 5,
					Color.YELLOW);
		}

		// Now game elements:

		g.drawImage(currentSprite, robot.getCenterX() - 61,
				robot.getCenterY() - 63);
		g.drawImage(hanim.getImage(), hb.getCenterX() - 48,
				hb.getCenterY() - 48);
		g.drawImage(hanim.getImage(), hb2.getCenterX() - 48,
				hb2.getCenterY() - 48);

		// And now, we overlay the UI:
		if (state == GameState.Ready)
			drawReadyUI();
		if (state == GameState.Running)
			drawRunningUI();
		if (state == GameState.Paused)
			drawPausedUI();
		if (state == GameState.GameOver)
			drawGameOverUI();

	}

	private void paintTiles(Graphics g) {
		for (int tileIndex = 0; tileIndex < tileArray.size(); tileIndex++) {
			Tile tile = (Tile) tileArray.get(tileIndex);
			if (tile.type != 0) {
				g.drawImage(tile.getTileImage(), tile.getTileX(),
						tile.getTileY());
			}
		}

	}

	public void animate() {
		anim.update(10);
		hanim.update(50);
	}

	private void nullify() {
		// Set all variables to null. We'll recreate
		// them in the constructor.
		paint = null;
		bg1 = null;
		bg2 = null;

		robot = null;

		hb = null;
		hb2 = null;

		currentSprite = null;
		character = null;
		character2 = null;
		character3 = null;

		heliboy = null;
		heliboy2 = null;
		heliboy3 = null;
		heliboy4 = null;
		heliboy5 = null;

		anim = null;
		hanim = null;

		// Call the garbage collector to clean up our memory.
		System.gc();
	}

	private void drawReadyUI() {
		Graphics g = game.getGraphics();

		// Darken the screen
		g.drawARGB(155, 0, 0, 0);
		g.drawString("Tap to Start.", 400, 240, paint);

	}

	private void drawRunningUI() {
		Graphics g = game.getGraphics();
		g.drawImage(Assets.button, 0, 285, 0, 0, 65, 65);
		g.drawImage(Assets.button, 0, 350, 0, 65, 65, 65);
		g.drawImage(Assets.button, 0, 415, 0, 130, 65, 65);
		g.drawImage(Assets.button, 0, 0, 0, 195, 35, 35);
	}

	private void drawPausedUI() {
		Graphics g = game.getGraphics();
		// Darken the screen to display the Paused screen.
		g.drawARGB(155, 0, 0, 0);
		g.drawString("Resume", 400, 165, paint2);
		g.drawString("Menu", 400, 360, paint2);

	}

	private void drawGameOverUI() {
		Graphics g = game.getGraphics();
		g.drawRect(0, 0, 1281, 801, Color.BLACK);
		g.drawString("GAME OVER", 400, 240, paint2);
		g.drawString("Tap to return", 400, 290, paint);

	}

	@Override
	public void pause() {
		// No sense pausing if we weren't already running
		if(state == GameState.Running)
			state = GameState.Paused;

	}

	@Override
	public void resume() {
		// This is how we return from pause
		if(state == GameState.Paused)
			state = GameState.Running;

	}

	@Override
	public void dispose() {

	}

	@Override
	public void backButton() {
		// Back button means pause. Well, if we can pause, it does.
		pause();

	}
	
	private void goToMenu(){
		game.setScreen(new MainMenuScreen(game));
	}
	
	public static Background getBg1(){
		return bg1;
	}
	
	public static Background getBg2(){
		return bg2;
	}
	
	public static Robot getRobot(){
		return robot;
	}

}
