package com.signalsin.racer.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.signalsin.racer.RacerGame;
import com.signalsin.racer.car.BoxProp;
import com.signalsin.racer.car.Car;

public class GameScreen implements Screen {
	/**
	 * The time the last frame was rendered, used for throttling framerate
	 */
	private long lastRender;
	
	RacerGame game;
	
	public static final int STEER_NONE=0;
	public static final int STEER_RIGHT=1;
	public static final int STEER_LEFT=2;

	public static final int ACC_NONE=0;
	public static final int ACC_ACCELERATE=1;
	public static final int ACC_BRAKE=2;
	
	private OrthographicCamera camera;
	private SpriteBatch spriteBatch;
	/**
	 * This is the main box2d "container" object. All bodies will be loaded in
	 * this object and will be simulated through calls to this object.
	 */
	private World world;
	/**
	 * This box2d debug renderer comes from libgdx test code. It draws lines
	 * over all collision boundaries, so it is immensely useful for verifying
	 * that the world collisions are as you expect them to be. It is, however,
	 * slow, so only use it for testing.
	 */
	private Box2DDebugRenderer debugRenderer;	

	private int screenWidth;
	private int screenHeight;	
	private float worldWidth;
	private float worldHeight;
	private static int PIXELS_PER_METER=15;      //how many pixels in a meter
	
	Car car;
	
	
    // constructor to keep a reference to the main Game class
    public GameScreen(RacerGame RacerGame){
            this.game = RacerGame;
    }
	
	public void onCreate() {		
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
		worldWidth = screenWidth / PIXELS_PER_METER;
		worldHeight = screenHeight / PIXELS_PER_METER;

		//Box2d World init
		world = new World(new Vector2(0.0f, 0.0f), true);	
	    
	    this.car = new Car(world, 2, 4,
	    		new Vector2(10, 10), (float) Math.PI, 60, 20, 60);
		
	    camera = new OrthographicCamera();
	    camera.setToOrtho(false, screenWidth, screenHeight);
	    spriteBatch = new SpriteBatch();		
										
		debugRenderer = new Box2DDebugRenderer();

	    Vector2 center = new Vector2(worldWidth/2, worldHeight/2);
	    
	    //pen in the center
	    BoxProp pen1 = new BoxProp(world, 1, 6, new Vector2 (center.x-3,center.y));
	    BoxProp pen2 = new BoxProp(world, 1, 6, new Vector2 (center.x+3,center.y));
	    BoxProp pen3 = new BoxProp(world, 5, 1, new Vector2 (center.x,center.y+2.5f));
	    
	    //outer walls
		BoxProp wall1 = new BoxProp(world, worldWidth, 1, new Vector2 (worldWidth/2,0.5f)); //bottom
	    BoxProp wall2 = new BoxProp(world, 1, worldHeight-2, new Vector2 (0.5f, worldHeight/2));//left
	    BoxProp wall3 = new BoxProp(world,  worldWidth, 1, new Vector2 (worldWidth/2,worldHeight-0.5f));//top
	    BoxProp wall4 = new BoxProp(world, 1, worldHeight-2, new Vector2 (worldWidth-0.5f, worldHeight/2));	  //right  
	}

	@Override
	public void dispose() {
		spriteBatch.dispose();
	}

	public void render(float delta) {	
	    Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

	    // tell the camera to update its matrices.
	    camera.update();
	    
		spriteBatch.setProjectionMatrix(camera.combined);

		if (Gdx.input.isKeyPressed(Input.Keys.DPAD_UP))
			car.accelerate = ACC_ACCELERATE;
		else if (Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN))
			car.accelerate = ACC_BRAKE;
		else
			car.accelerate = ACC_NONE;
		
		if (Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT))
			car.steer = STEER_LEFT;
		else if (Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT))
			car.steer = STEER_RIGHT;
		else
			car.steer = STEER_NONE;
		
		car.update(Gdx.app.getGraphics().getDeltaTime());
		
		/**
		 * Have box2d update the positions and velocities (and etc) of all
		 * tracked objects. The second and third argument specify the number of
		 * iterations of velocity and position tests to perform -- higher is
		 * more accurate but is also slower.
		 */
		world.step(Gdx.app.getGraphics().getDeltaTime(), 3, 3);
		
		world.clearForces();
		
		/**
		 * Draw this last, so we can see the collision boundaries on top of the
		 * sprites and map.
		 */
		debugRenderer.render(world, camera.combined.scale(PIXELS_PER_METER,PIXELS_PER_METER,PIXELS_PER_METER));
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

}