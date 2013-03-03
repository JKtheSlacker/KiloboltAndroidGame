package com.slaxer.framework.implementation;

import java.util.ArrayList;
import java.util.List;

import android.view.MotionEvent;
import android.view.View;

import com.slaxer.framework.Input.TouchEvent;
import com.slaxer.framework.Pool;
import com.slaxer.framework.Pool.PoolObjectFactory;

public class MultiTouchHandler implements TouchHandler {
	private static final int MAX_TOUCHPOINTS = 10;
	
	boolean[] isTouched = new boolean[MAX_TOUCHPOINTS];
	int[] touchX = new int[MAX_TOUCHPOINTS];
	int[] touchY = new int[MAX_TOUCHPOINTS];
	int[] id = new int[MAX_TOUCHPOINTS];
	Pool<TouchEvent> touchEventPool;
	List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();
	List<TouchEvent> touchEventsBuffer = new ArrayList<TouchEvent>();
	float scaleX;
	float scaleY;

	public MultiTouchHandler(View view, float scaleX, float scaleY) {
		PoolObjectFactory<TouchEvent> factory = new PoolObjectFactory<TouchEvent>(){
			@Override
			public TouchEvent createObject(){
				return new TouchEvent();
			}
		};
		
		touchEventPool = new Pool<TouchEvent> (factory, 100);
		view.setOnTouchListener(this);
		
		this.scaleX = scaleX;
		this.scaleY = scaleY;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		synchronized(this){
			int action = event.getAction() & MotionEvent.ACTION_MASK;
			int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_ID_MASK) >> MotionEvent.ACTION_POINTER_ID_SHIFT;
			int pointerCount = event.getPointerCount();
			TouchEvent touchEvent;
			for(int touchEventIndex = 0; touchEventIndex < MAX_TOUCHPOINTS; touchEventIndex++){
				if(touchEventIndex >= pointerCount){
					isTouched[touchEventIndex] = false;
					id[touchEventIndex] = -1;
					continue;
				}
				int pointerId = event.getPointerId(touchEventIndex);
				if(event.getAction() != MotionEvent.ACTION_MOVE && touchEventIndex != pointerIndex){
					// if it's an up/down/cancel/out event, mask the id to see if we should process it for this touch point
					continue;
				}
				switch(action){
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_POINTER_DOWN:
					touchEvent = touchEventPool.newObject();
					touchEvent.type = TouchEvent.TOUCH_DOWN;
					touchEvent.pointer = pointerId;
					touchEvent.x = touchX[touchEventIndex] = (int)(event.getX(touchEventIndex) * scaleX);
					touchEvent.y = touchY[touchEventIndex] = (int)(event.getY(touchEventIndex)* scaleY);
					isTouched[touchEventIndex] = true;
					id[touchEventIndex] = pointerId;
					touchEventsBuffer.add(touchEvent);
					break;
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_POINTER_UP:
				case MotionEvent.ACTION_CANCEL:
					touchEvent = touchEventPool.newObject();
					touchEvent.type = TouchEvent.TOUCH_UP;
					touchEvent.pointer = pointerId;
					touchEvent.x = touchX[touchEventIndex] = (int)(event.getX(touchEventIndex) * scaleX);
					touchEvent.y = touchY[touchEventIndex] = (int)(event.getY(touchEventIndex)* scaleY);
					isTouched[touchEventIndex] = false;
					id[touchEventIndex] = -1;
					touchEventsBuffer.add(touchEvent);
					break;
				case MotionEvent.ACTION_MOVE:
					touchEvent = touchEventPool.newObject();
					touchEvent.type = TouchEvent.TOUCH_DRAGGED;
					touchEvent.pointer = pointerId;
					touchEvent.x = touchX[touchEventIndex] = (int)(event.getX(touchEventIndex) * scaleX);
					touchEvent.y = touchY[touchEventIndex] = (int)(event.getY(touchEventIndex)* scaleY);
					isTouched[touchEventIndex] = true;
					id[touchEventIndex] = pointerId;
					touchEventsBuffer.add(touchEvent);
					break;
				}
			}
			return true;
		}
	}

	@Override
	public boolean isTouchDown(int pointer) {
		synchronized(this){
			int index = getIndex(pointer);
			if(index < 0 || index >= MAX_TOUCHPOINTS)
				return false;
			else
				return isTouched[index];
		}
	}

	@Override
	public int getTouchX(int pointer) {
		synchronized(this){
			int index = getIndex(pointer);
			if(index < 0 || index >= MAX_TOUCHPOINTS)
				return 0;
			else
				return touchX[index];
		}
	}

	@Override
	public int getTouchY(int pointer) {
		synchronized(this){
			int index = getIndex(pointer);
			if(index < 0 || index >= MAX_TOUCHPOINTS)
				return 0;
			else
				return touchY[index];
		}
	}

	@Override
	public List<TouchEvent> getTouchEvents() {
		synchronized(this){
			int length = touchEvents.size();
			for(int touchEventIndex = 0; touchEventIndex < length; touchEventIndex++){
				touchEventPool.free(touchEvents.get(touchEventIndex));
			}
			touchEvents.clear();
			touchEvents.addAll(touchEventsBuffer);
			touchEventsBuffer.clear();
			return touchEvents;
		}
	}
	
	private int getIndex(int pointerId){
		for(int index = 0; index < MAX_TOUCHPOINTS; index++){
			if(id[index] == pointerId)
				return index;
		}
		// No index? Can't have a negative index!
		return -1;
	}

}
