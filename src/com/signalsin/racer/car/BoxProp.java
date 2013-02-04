package com.signalsin.racer.car;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class BoxProp {
	public float width, height;
	public Body body;

	public BoxProp(World world, float width, float height, Vector2 position) {
		super();
		this.width = width;
		this.height = height;
	    
		//initialize body 
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(position);
		bodyDef.angle = 0;
		bodyDef.fixedRotation = true;
		this.body = world.createBody(bodyDef);
	    
	    //initialize shape
		FixtureDef fixtureDef = new FixtureDef();
		PolygonShape boxShape = new PolygonShape();
		boxShape.setAsBox(this.width / 2, this.height / 2);
		fixtureDef.shape=boxShape;
		fixtureDef.restitution=0.7f; //positively bouncy!
	    this.body.createFixture(fixtureDef);
	    boxShape.dispose();
	}
}
