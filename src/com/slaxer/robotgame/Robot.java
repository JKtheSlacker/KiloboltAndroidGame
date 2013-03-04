package com.slaxer.robotgame;

import java.util.ArrayList;

import android.graphics.Rect;

public class Robot {
	final int JUMPSPEED = -15;
	final int MOVESPEED = 5;

	private int centerX = 100;
	private int centerY = 377;
	private boolean jumped = false;
	private boolean movingLeft = false;
	private boolean movingRight = false;
	private boolean ducked = false;
	private boolean readyToFire = true;

	private static Background bg1 = GameScreen.getBg1();
	private static Background bg2 = GameScreen.getBg2();

	private int speedX = 0;
	private int speedY = 0;
	public static Rect upperCollRect = new Rect(0, 0, 0, 0);
	public static Rect lowerCollRect = new Rect(0, 0, 0, 0);
	public static Rect leftCollRect = new Rect(0, 0, 0, 0);
	public static Rect rightCollRect = new Rect(0, 0, 0, 0);
	public static Rect leftFootCollRect = new Rect(0,0,0,0);
	public static Rect rightFootCollRect = new Rect(0,0,0,0);
	// Use this to limit the number of tiles we have to do collision detection
	// for.
	public static Rect robotAreaRect = new Rect(0, 0, 0, 0);

	private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();

	public void update() {
		// Moves character or Scrolls Background accordingly
		// If the character is located in the leftmost 150 pixels of the screen
		// then we want him to be able to move freely. If he's in the rightmost
		// part of the screen, then the background should move instead of the
		// character.
		// And of course, if he's not moving, then there's really no need to do
		// anything.
		if (speedX < 0)
			centerX += speedX;
		if (speedX == 0 || speedX < 0) {
			bg1.setSpeedX(0);
			bg2.setSpeedX(0);
		}
		if (centerX <= 200 && speedX > 0)
			centerX += speedX;
		if (speedX > 0 && centerX > 200) {
			// The very background should scroll very slowly.
			bg1.setSpeedX(-MOVESPEED / 5);
			bg2.setSpeedX(-MOVESPEED / 5);
		}

		// Updates Y position
		// The ground arbitrarily lives at 440 pixels below the top of the
		// screen.
		// So, at bottom, our character's center should be 382 pixels below the
		// top of the screen. This check ensures that he stays above ground at
		// all
		// times, and that he falls appropriately due to gravity.
		centerY += speedY;

		// Handles Jumping
		speedY += 1;

		if (speedY > 3) {
			jumped = true;
		}

		// Prevents going beyond X coordinate of 0
		if (centerX + speedX <= 60)
			centerX = 61;

		// Set up the collision Rects
		upperCollRect.set(centerX - 34, centerY - 63, centerX + 34, centerY);
		lowerCollRect.set(upperCollRect.left, upperCollRect.top + 63, upperCollRect.left + 68, upperCollRect.top + 128);
		leftCollRect.set(upperCollRect.left - 26, upperCollRect.top + 32, upperCollRect.left, upperCollRect.top + 52);
		rightCollRect.set(upperCollRect.left + 68, upperCollRect.top + 32, upperCollRect.left + 94, upperCollRect.top + 52);
		leftFootCollRect.set(centerX - 50, centerY + 20, centerX, centerY + 35);
		rightFootCollRect.set(centerX, centerY + 20, centerX + 50, centerY + 35);
		robotAreaRect.set(centerX - 110, centerY - 110, centerX + 70, centerY + 70);
	}

	public void moveRight() {
		if (!ducked)
			speedX = MOVESPEED;
	}

	public void moveLeft() {
		if (!ducked)
			speedX = -MOVESPEED;
	}

	public void stopRight() {
		setMovingRight(false);
		stop();
	}

	public void stopLeft() {
		setMovingLeft(false);
		stop();
	}

	private void stop() {
		if (!isMovingRight() && !isMovingLeft())
			speedX = 0;
		if (!isMovingRight() && isMovingLeft())
			moveLeft();
		if (isMovingRight() && !isMovingLeft())
			moveRight();
	}

	// Begin jumping if we're not already jumping
	// screen coordinates use quadrant IV!
	public void jump() {
		if (!jumped) {
			speedY = JUMPSPEED;
			jumped = true;
		}
	}

	// Make a new bullet where the gun is, and add it to the list of
	// projectiles.
	public void shoot() {
		if (readyToFire) {
			Projectile p = new Projectile(centerX + 50, centerY - 25);
			projectiles.add(p);
		}
	}

	public int getCenterX() {
		return centerX;
	}

	public int getCenterY() {
		return centerY;
	}

	public boolean isJumped() {
		return jumped;
	}

	public int getSpeedX() {
		return speedX;
	}

	public int getSpeedY() {
		return speedY;
	}

	public void setCenterX(int centerX) {
		this.centerX = centerX;
	}

	public void setCenterY(int centerY) {
		this.centerY = centerY;
	}

	public void setJumped(boolean jumped) {
		this.jumped = jumped;
	}

	public void setSpeedX(int speedX) {
		this.speedX = speedX;
	}

	public void setSpeedY(int speedY) {
		this.speedY = speedY;
	}

	public boolean isMovingLeft() {
		return movingLeft;
	}

	public boolean isMovingRight() {
		return movingRight;
	}

	public boolean isDucked() {
		return ducked;
	}

	public void setDucked(boolean ducked) {
		this.ducked = ducked;
	}

	public void setMovingLeft(boolean movingLeft) {
		this.movingLeft = movingLeft;
	}

	public void setMovingRight(boolean movingRight) {
		this.movingRight = movingRight;
	}

	public ArrayList<Projectile> getProjectiles() {
		return projectiles;
	}

	public boolean isReadyToFire() {
		return readyToFire;
	}

	public void setReadyToFire(boolean readyToFire) {
		this.readyToFire = readyToFire;
	}

}
