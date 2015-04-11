package de.hft_stuttgart.spirit;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class FadeEffect {

	boolean isActive = false;
	final int anzahl = 21; // anzahl frames
	private int texturInterval = 1000; // zeit in der alle texturen 1 mal
										// dargestellt werden
	private long soundInterval = 2000;
	private long lastSound = 0;

	Sound sound;
	Sprite[] fade = new Sprite[anzahl];
	Texture[] fadeTexture = new Texture[anzahl];
	int current = 0;
	
	public FadeEffect() {
		sound = Gdx.audio.newSound(Gdx.files
				.internal("prel_hit_laser_bw02.1256.wav"));
		for (int i = 0;i<anzahl;i++){
			fadeTexture[i] = new Texture(Gdx.files.internal("beam-"+i+".png"));
			fade[i] = new Sprite(fadeTexture[i]);
		}
	}

	public void start() {
		isActive = true;
	}

	public void stop() {
		isActive = false;
	}

	private void sound(){
		if (System.currentTimeMillis() - lastSound > soundInterval) {
			sound.play();
			lastSound = System.currentTimeMillis();
		}
	}
	public void draw(SpriteBatch batch, Rectangle targetRectangle) {
		if (isActive) {
			current = getCurrent();
			fade[current].setSize(targetRectangle.width, targetRectangle.height);
			fade[current].setPosition(targetRectangle.x, targetRectangle.y);
			fade[current].draw(batch);
			sound();
		}
	}

	private int getCurrent() {
		long zeit = System.currentTimeMillis() % texturInterval;
		for (int i = 0; i < anzahl; i++) {
			if (zeit < (i + 1) * ((float) texturInterval / (float) anzahl)) {
				return i;
			}
		}
		return anzahl - 1;
	}

	public void dispose() {
		sound.dispose();
		for (int i = 0; i < anzahl; i++) {
			fadeTexture[i].dispose();
		}
	}
}
