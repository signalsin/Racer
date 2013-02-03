package com.signalsin.racer.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ImageCache {  
	  
	//private static Texture sheet;
	private static TextureAtlas atlas; 
	
	//load the texture pack file from TexturePacker
	//I use the TexturePacker GUI to sort that out
	//has a parameter so that you can specify different texture packs on load
    public static void load (String pack) {  
        String textureFile = "data/" + pack;  
        atlas = new TextureAtlas(Gdx.files.internal(textureFile), Gdx.files.internal("data"));  
    } 
    
    
    public static TextureRegion getTexture (String name) {  
        return atlas.findRegion(name);  
    }  
      
    public static TextureRegion getFrame (String name, int index) {  
        return atlas.findRegion(name, index);  
    }
    
    public static void dispose(){
    	atlas.dispose();
    }

}