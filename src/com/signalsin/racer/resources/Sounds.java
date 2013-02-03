package com.signalsin.racer.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class Sounds {
	
	public static Sound key;
	
	public static void load(){
		
		//load the sounds into the variables
		key = loadSound("key.ogg");
		
	}
	
    private static Sound loadSound (String filename) { 
    	//load the sound into the game
        return Gdx.audio.newSound(Gdx.files.internal("data/" + filename));  
    }
      
    public static void play (Sound sound) {  
        sound.play(1);  
    }
    
    public static void dispose(){
    	key.dispose();
    }

}