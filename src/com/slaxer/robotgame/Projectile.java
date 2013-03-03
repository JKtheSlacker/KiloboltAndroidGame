package com.slaxer.robotgame;

import java.awt.Rectangle;

public class Projectile {
	private int x, y, speedX;
	private boolean visible;
	private Rectangle collRect;

	public Projectile(int startX, int startY) {
		x = startX;
		y = startY;
		speedX = 7;
		visible = true;
		
		collRect = new Rectangle(0,0,0,0);
	}
	
	public void update(){
		x += speedX;
		collRect.setBounds(x,y,10,5);
		if (x > 800){
			visible = false;
			collRect = null;
		}
		if (x < 800){
			checkCollision();
		}
	}
	
	private void checkCollision(){
		if(collRect.intersects(StartingClass.hb.collRect)){
			visible = false;
			
			if (StartingClass.hb.health > 0)
				StartingClass.hb.health -= 1;
			if (StartingClass.hb.health == 0){
				StartingClass.hb.setCenterX(-100);
				StartingClass.score += 5;
			}
		}
		if(collRect.intersects(StartingClass.hb2.collRect)){
			visible = false;
			
			if (StartingClass.hb2.health > 0)
				StartingClass.hb2.health -= 1;
			if (StartingClass.hb2.health == 0){
				StartingClass.hb2.setCenterX(-100);
				StartingClass.score += 5;
			}

		}
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getSpeedX() {
		return speedX;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setSpeedX(int speedX) {
		this.speedX = speedX;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

}
