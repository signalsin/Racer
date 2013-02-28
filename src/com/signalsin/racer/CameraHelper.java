package com.signalsin.racer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
public class CameraHelper {

	private OrthographicCamera camera;
	private int screenWidth;
	private int screenHeight;
	private int viewportWidth;
	private int viewportHeight;
	private float aspect;
	
	public CameraHelper(int virtualWidth, int virtualHeight){
	    
		camera = new OrthographicCamera();
	    
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
		
		aspect = virtualWidth / virtualHeight;
		
		if (screenWidth / screenHeight >= aspect) {
			// Letterbox left and right.
			viewportHeight = virtualHeight;
			viewportWidth = viewportHeight * screenWidth / screenHeight;
		} else {
			// Letterbox above and below.
			viewportWidth = virtualWidth;
			viewportHeight = viewportWidth * screenHeight / screenWidth;
		}
		
		camera.setToOrtho(false, viewportWidth, viewportHeight);
	}
	
	
	
	public void update (float x, float y){
		camera.position.set(x, y, 0);
		camera.update();
	}
	
	public int getViewportWidth(){
		return viewportWidth;
	}
	
	public int getViewportHeight(){
		return viewportHeight;
	}
	
	public Matrix4 getCombined(){
		return camera.combined;
	}
	
}