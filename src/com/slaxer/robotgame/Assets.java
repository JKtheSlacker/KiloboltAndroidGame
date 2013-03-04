package com.slaxer.robotgame;

import com.slaxer.framework.Image;
import com.slaxer.framework.Music;
import com.slaxer.framework.Sound;

public class Assets {

	public static Image menu, splash, background;
	public static Image character, character2, character3, characterJump,
			characterDown;
	public static Image heliboy, heliboy2, heliboy3, heliboy4, heliboy5;
	public static Image tiledirt, tilegrassTop, tilegrassBottom, tilegrassLeft,
			tilegrassRight;
	public static Image button;
	public static Sound click;
	public static Music theme;

	public static void load(SampleGame sampleGame) {
		theme = sampleGame.getAudio().createMusic("menutheme.mp3");
		theme.setLooping(true);
		theme.setVolume(0.85f);
		theme.play();
	}

}
