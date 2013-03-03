package com.slaxer.robotgame;

import java.awt.Image;
import java.awt.Rectangle;

public class Tile {
	private int tileX, tileY, speedX, type;
	public Image tileImage;
	private Rectangle collRect;

	private Robot robot = StartingClass.getRobot();
	private Background bg = StartingClass.getBg1();

	public Tile(int x, int y, int typeInt) {
		// Why 40? Each tile occupies 40 pixels.
		tileX = x * 40;
		tileY = y * 40;

		type = typeInt;
		
		collRect = new Rectangle();

		if (type == 5)
			tileImage = StartingClass.tiledirt;
		else if (type == 8)
			tileImage = StartingClass.tilegrassTop;
		else if (type == 4)
			tileImage = StartingClass.tilegrassLeft;
		else if (type == 6)
			tileImage = StartingClass.tilegrassRight;
		else if (type == 2)
			tileImage = StartingClass.tilegrassBot;
		else
			type = 0;
	}

	// No more parallax tiles.
	public void update() {
		speedX = bg.getSpeedX() * 5;
		tileX += speedX;
		collRect.setBounds(tileX, tileY, 40, 40);
		
		if(collRect.intersects(Robot.robotAreaRectangle) && type != 0){
			checkVerticalCollision(Robot.upperCollRect,Robot.lowerCollRect);
			checkSideCollision(Robot.leftCollRect, Robot.rightCollRect, Robot.leftFootCollRect, robot.rightFootCollRect);
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
	public void checkVerticalCollision(Rectangle robotUpperCollRect, Rectangle robotLowerCollRect){
		if(robotUpperCollRect.intersects(collRect)){
			// DEBUG Fix this later
		}
		// Stop moving if the robot runs into a dirt tile
		if(robotLowerCollRect.intersects(collRect) && type == 8){
			robot.setJumped(false);
			robot.setSpeedY(0);
			robot.setCenterY(tileY - 63);
		}
	}
	
	// This prevents us from running through tiles.
	public void checkSideCollision(Rectangle robotLeftCollRect, Rectangle robotRightCollRect, Rectangle robotLeftFootCollRect, Rectangle robotRightFootCollRect){
		// Don't check side collisions with dirt, grass on the bottom of the tile, or empty tiles
		if(type != 5 && type != 2 && type != 0){
			// Check if we've hit on the left 
			if(robotLeftCollRect.intersects(collRect)){
				// Move the bot outside of the collision rectangle and stop it.
				robot.setCenterX(tileX + 102);
				robot.setSpeedX(0);
			// Check if we've hit on the left foot
			} else if (robotLeftFootCollRect.intersects(collRect)){
				// Move the bot outside of the collision rectangle and stop it.
				robot.setCenterX(tileX + 85);
				robot.setSpeedX(0);
			}
			
			// Check if we've hit on the right
			if(robotRightCollRect.intersects(collRect)){
				// Move the bot outside of the collision rectangle and stop it.
				robot.setCenterX(tileX - 62);
				robot.setSpeedX(0);
			// Check if we've hit on the right foot
			} else if (robotRightFootCollRect.intersects(collRect)){
				// Move the bot outside of the collision rectangle and stop it.
				robot.setCenterX(tileX - 45);
				robot.setSpeedX(0);
			}
		}
	}
}
