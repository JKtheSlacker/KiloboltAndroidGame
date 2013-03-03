package com.slaxer.robotgame;

import java.awt.Rectangle;

public class Enemy {
	private int power, speedX, centerX, centerY;
	private Background bg = StartingClass.getBg1();
	private Robot robot = StartingClass.getRobot();
	public int health = 5;
	
	private int movementSpeed;
	
	public Rectangle collRect = new Rectangle(0,0,0,0);
	
	//Behavioral methods
	public void update(){
		follow();
		centerX += speedX;
		speedX = bg.getSpeedX() * 5 + movementSpeed;
		collRect.setBounds(centerX - 25, centerY - 25, 50, 60);
		
		if (collRect.intersects(Robot.robotAreaRectangle))
			checkCollision();
	}
	
	private void checkCollision(){
		if (collRect.intersects(Robot.upperCollRect) || collRect.intersects(Robot.lowerCollRect) || collRect.intersects(Robot.leftCollRect) || collRect.intersects(Robot.rightCollRect))
			// DEBUG Put real code here
			System.out.println("collision");
	}
	
	public void follow(){
		if (centerX < -95 || centerX > 810)
			movementSpeed = 0;
		else if (Math.abs(robot.getCenterX() - centerX) < 5)
			movementSpeed = 0;
		else{
			if(robot.getCenterX() >= centerX)
				movementSpeed = 1;
			else
				movementSpeed = -1;
		}
	}
	
	public void die(){
		
	}
	
	public void attack(){
		
	}

	public int getPower() {
		return power;
	}

	public int getSpeedX() {
		return speedX;
	}

	public int getCenterX() {
		return centerX;
	}

	public int getCenterY() {
		return centerY;
	}

	public Background getBg() {
		return bg;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public void setSpeedX(int speedX) {
		this.speedX = speedX;
	}

	public void setCenterX(int centerX) {
		this.centerX = centerX;
	}

	public void setCenterY(int centerY) {
		this.centerY = centerY;
	}

	public void setBg(Background bg) {
		this.bg = bg;
	}

}
