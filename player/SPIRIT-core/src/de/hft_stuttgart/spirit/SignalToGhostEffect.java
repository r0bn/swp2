package de.hft_stuttgart.spirit;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;

public class SignalToGhostEffect {
	Sprite effect;
	long effectStart = 0;
	long effectTime = 0;

	public SignalToGhostEffect(TextureAtlas guiAtlas) {
		effect = guiAtlas.createSprite("schein_uebergabegeist_blau");
		effect.setSize(512, 512);
		effect.setPosition(500, 500);
	}

	public void startEffect(long effectTime) {
		this.effectTime = effectTime;
		effectStart = System.currentTimeMillis();
	}

	public void draw(SpriteBatch batch, Rectangle targetRectangle) {
		if (System.currentTimeMillis() < effectStart + effectTime) {
			effect.setSize(
					targetRectangle.width
							* (1f - (System.currentTimeMillis() - effectStart)
									/ (float)effectTime), targetRectangle.height
							* (1f - (System.currentTimeMillis() - effectStart)
									/ (float)effectTime));
			effect.setPosition(targetRectangle.x + targetRectangle.width / 2
					- effect.getWidth() / 2, targetRectangle.y
					+ targetRectangle.height / 2 - effect.getHeight() / 2);
			effect.draw(batch);
		}
	}

}
