package com.slaxer.robotgame;

import android.graphics.Rect;

import com.slaxer.framework.Image;

public class Tile {
	private int tileX, tileY, speedX;
	public int type;
	public Image tileImage;
	private Rect collRect;

	private Robot robot = GameScreen.getRobot();
	private Background bg = GameScreen.getBg1();

	public Tile(int x, int y, int typeInt) {
		// Why 40? Each tile occupies 40 pixels.
		tileX = x * 40;
		tileY = y * 40;

		type = typeInt;

		collRect = new Rect();

		if (type == 5)
			tileImage = Assets.tiledirt;
		else if (type == 8)
			tileImage = Assets.tilegrassTop;
		else if (type == 4)
			tileImage = Assets.tilegrassLeft;
		else if (type == 6)
			tileImage = Assets.tilegrassRight;
		else if (type == 2)
			tileImage = Assets.tilegrassBottom;
		else
			type = 0;
	}

	// No more parallax tiles.
	public void update() {
		speedX = bg.getSpeedX() * 5;
		tileX += speedX;
		collRect.set(tileX, tileY, 40, 40);

		if (Rect.intersects(collRect, Robot.robotAreaRect) && type != 0) {
			checkVerticalCollision(Robot.upperCollRect, Robot.lowerCollRect);
			checkSideCollision(Robot.leftCollRect, Robot.rightCollRect,
					Robot.leftFootCollRect, robot.rightFootCollRect);
		}
	}

	public int getTileX() {
		return tileX;
	}

	public int getTileY() {
		return tileY;
	}

	public Image getTileImage() {
		return tileImage;
	}

	public void setTileX(int tileX) {
		this.tileX = tileX;
	}

	public void setTileY(int tileY) {
		this.tileY = tileY;
	}

	public void setTileImage(Image tileImage) {
		this.tileImage = tileImage;
	}

	// This prevents us from dropping through platforms or the ground.
	public void checkVerticalCollision(Rect robotUpperCollRect,
			Rect robotLowerCollRect) {
		if (Rect.intersects(collRect, robotUpperCollRect)) {
			// DEBUG Fix this later
		}
		// Stop moving if the robot runs into a dirt tile
		if (Rect.intersects(collRect, robotLowerCollRect) && type == 8) {
			robot.setJumped(false);
			robot.setSpeedY(0);
			robot.setCenterY(tileY - 63);
		}
	}

	// This prevents us from running through tiles.
	public void checkSideCollision(Rect robotLeftCollRect,
			Rect robotRightCollRect, Rect robotLeftFootCollRect,
			Rect robotRightFootCollRect) {
		// Don't check side collisions with dirt, grass on the bottom of the
		// tile, or empty tiles
		if (type != 5 && type != 2 && type != 0) {
			// Check if we've hit on the left
			if (Rect.intersects(collRect, robotLeftCollRect)) {
				// Move the bot outside of the collision rectangle and stop it.
				robot.setCenterX(tileX + 102);
				robot.setSpeedX(0);
				// Check if we've hit on the left foot
			} else if (Rect.intersects(collRect, robotLeftFootCollRect)) {
				// Move the bot outside of the collision rectangle and stop it.
				robot.setCenterX(tileX + 85);
				robot.setSpeedX(0);
			}

			// Check if we've hit on the right
			if (Rect.intersects(collRect, robotRightCollRect)) {
				// Move the bot outside of the collision rectangle and stop it.
				robot.setCenterX(tileX - 62);
				robot.setSpeedX(0);
				// Check if we've hit on the right foot
			} else if (Rect.intersects(collRect, robotRightFootCollRect)) {
				// Move the bot outside of the collision rectangle and stop it.
				robot.setCenterX(tileX - 45);
				robot.setSpeedX(0);
			}
		}
	}
}
