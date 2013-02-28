package com.signalsin.racer.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.signalsin.racer.CameraHelper;
import com.signalsin.racer.RacerGame;
import com.signalsin.racer.car.BoxProp;
import com.signalsin.racer.car.Car;
import com.signalsin.racer.resources.ImageCache;

public class GameScreen implements Screen {
	/**
	 * The time the last frame was rendered, used for throttling framerate
	 */
	private long lastRender;
	
	RacerGame game;
	
	//private OrthographicCamera camera;
	
	private SpriteBatch spriteBatch;
	
	CameraHelper camera;
	
	private Sprite playerSprite;
	private TextureRegion playerTexture;
	
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
	
	private static final int VIRTUAL_WIDTH = 480;
	private static final int VIRTUAL_HEIGHT = 320;
	
	private float worldWidth;
	private float worldHeight;
	private static int PIXELS_PER_METER=16;      //how many pixels in a meter
	
	Car car;
	
    // constructor to keep a reference to the main Game class
    public GameScreen(RacerGame RacerGame){
            this.game = RacerGame;
    }
	
	public void onCreate() {		
		
		//load texture files
		ImageCache.load("cars.atlas");
		
		//Box2d World init
		world = new World(new Vector2(0.0f, 0.0f), true);	
	    
	    this.car = new Car(world, 1, 2,
	    		new Vector2(10, 10), (float) Math.PI, 60, 15, 25, 100);
		
	    //create the camera	    
	    camera = new CameraHelper(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
	    
	    /**
	     * Now that we have the height/width of the viewport,
	     * we can set the height/width of the world.
	     **/
		worldWidth = camera.getViewportWidth() / PIXELS_PER_METER;
		worldHeight = camera.getViewportHeight() / PIXELS_PER_METER;
	    
	    //create the sprite for the player car
		playerTexture = ImageCache.getTexture("playerCar");
		playerSprite = new Sprite(playerTexture);
		playerSprite.setPosition(PIXELS_PER_METER * car.body.getPosition().x - playerTexture.getRegionWidth() / 2,
				PIXELS_PER_METER * car.body.getPosition().y - playerTexture.getRegionHeight() / 2 );
	    
		
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
	    BoxProp wall3 = new BoxProp(world,  worldWidth, 1, new Vector2 (worldWidth/2,worldHeight + 100));//top
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
	    camera.update(car.body.getPosition().x * PIXELS_PER_METER, car.body.getPosition().y * PIXELS_PER_METER);
	    
		spriteBatch.setProjectionMatrix(camera.getCombined());
		
		if (Gdx.input.isTouched()){
			car.setAccelerate(Car.ACC_ACCELERATE);
		}
		else{
			car.setAccelerate(Car.ACC_NONE);
		}
		
		if (Gdx.input.getAccelerometerY() < -2.5){
			car.setSteer(Car.STEER_HARD_LEFT);
		}
		else if (Gdx.input.getAccelerometerY() < -1){
			car.setSteer(Car.STEER_LEFT);
		}
		else if (Gdx.input.getAccelerometerY() > 2.5){
			car.setSteer(Car.STEER_HARD_RIGHT);
		}
		else if (Gdx.input.getAccelerometerY() > 1){
			car.setSteer(Car.STEER_RIGHT);
		}
		else {
			car.setSteer(Car.STEER_NONE);
		}
		
		car.update(Gdx.app.getGraphics().getDeltaTime());
		
		/**
		 * Have box2d update the positions and velocities (and etc) of all
		 * tracked objects. The second and third argument specify the number of
		 * iterations of velocity and position tests to perform -- higher is
		 * more accurate but is also slower.
		 */
		world.step(Gdx.app.getGraphics().getDeltaTime(), 3, 3);
		
		world.clearForces();
		
		//draw the sprites
		spriteBatch.begin();
		
		playerSprite.setPosition(PIXELS_PER_METER * car.body.getPosition().x - playerTexture.getRegionWidth() / 2,
				PIXELS_PER_METER * car.body.getPosition().y - playerTexture.getRegionHeight() / 2 );
		playerSprite.setRotation((MathUtils.radiansToDegrees * car.body.getAngle()));
		
		playerSprite.draw(spriteBatch);
		
		spriteBatch.end();
		
		
		/**
		 * Draw this last, so we can see the collision boundaries on top of the
		 * sprites and map.
		 */
		debugRenderer.render(world, camera.getCombined().scale(PIXELS_PER_METER,PIXELS_PER_METER,PIXELS_PER_METER));
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
