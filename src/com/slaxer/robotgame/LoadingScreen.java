package com.slaxer.robotgame;

import com.slaxer.framework.Game;
import com.slaxer.framework.Graphics;
import com.slaxer.framework.Graphics.ImageFormat;
import com.slaxer.framework.Screen;

public class LoadingScreen extends Screen {

	public LoadingScreen(Game game) {
		super(game);
	}

	@Override
	public void update(float deltaTime) {
		Graphics g = game.getGraphics();
		Assets.menu = g.newImage("menu.png", ImageFormat.RGB565);
		Assets.background = g.newImage("background.png", ImageFormat.RGB565);
		Assets.character = g.newImage("character.png", ImageFormat.ARGB4444);
		Assets.character2 = g.newImage("character2.png", ImageFormat.ARGB4444);
		Assets.character3 = g.newImage("character3.png", ImageFormat.ARGB4444);
		Assets.characterDown = g.newImage("characterDown.png", ImageFormat.ARGB4444);
		Assets.characterJump = g.newImage("characterJump.png", ImageFormat.ARGB4444);
		
		Assets.heliboy = g.newImage("heliboy.png", ImageFormat.ARGB4444);
		Assets.heliboy2 = g.newImage("heliboy2.png", ImageFormat.ARGB4444);
		Assets.heliboy3 = g.newImage("heliboy3.png", ImageFormat.ARGB4444);
		Assets.heliboy4 = g.newImage("heliboy4.png", ImageFormat.ARGB4444);
		Assets.heliboy5 = g.newImage("heliboy5.png", ImageFormat.ARGB4444);
		
		Assets.tiledirt = g.newImage("tiledirt.png", ImageFormat.RGB565);
		Assets.tilegrassTop = g.newImage("tilegrassTop.png", ImageFormat.RGB565);
		Assets.tilegrassBottom = g.newImage("tilegrassBot.png", ImageFormat.RGB565);
		Assets.tilegrassLeft = g.newImage("tilegrassLeft.png", ImageFormat.RGB565);
		Assets.tilegrassRight = g.newImage("tilegrassRight.png", ImageFormat.RGB565);
		
		Assets.button = g.newImage("button.png", ImageFormat.RGB565);
		
		game.setScreen(new MainMenuScreen(game));
	}

	@Override
	public void paint(float deltaTime) {
		Graphics g = game.getGraphics();
		g.drawImage(Assets.splash, 0, 0);
		
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
		// TODO Auto-generated method stub
		
	}

}
