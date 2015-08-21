package de.hft_stuttgart.spirit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class TextButton {
	Sprite box;
	BitmapFont font;
	boolean isActive = false;
	// schriftgröße skalieren, angepasst auf 1920x1080
	float fontScale = 0.75f * Gdx.graphics.getHeight() / 1080f;

	public enum positionA {
		Left, Right
	};

	public enum positionB {
		TOP, CENTER, BOTTOM
	};

	String text = "";

	public TextButton(TextureAtlas guiAtlas, BitmapFont font, positionA a,
			positionB b) {
		this.font = font;
		box = guiAtlas.createSprite("textbox");
		box.setSize(
				((Gdx.graphics.getHeight() / 6) / box.getHeight())
						* box.getWidth(), Gdx.graphics.getHeight() / 5);

		switch (a) {
		case Left:
			box.setX(Gdx.graphics.getWidth() / 8);
			break;
		case Right:
			box.setX(Gdx.graphics.getWidth() - box.getWidth()
					- Gdx.graphics.getWidth() / 8);
			break;
		default:
			break;

		}

		switch (b) {
		case BOTTOM:
			box.setY(2 * Gdx.graphics.getHeight() / 10);
			break;
		case CENTER:
			box.setY((int) (4.5 * Gdx.graphics.getHeight() / 10));
			break;
		case TOP:
			box.setY(7 * Gdx.graphics.getHeight() / 10);
			break;
		default:
			break;

		}
	}

	public void draw(SpriteBatch batch) {
		box.draw(batch);
		if (font.getBounds(text).width > 0.95 * box.getWidth()) {
			font.setScale(0.95f * box.getWidth()/font.getBounds(text).width);
		}
		font.setColor(1, 1, 1, 1);
		font.draw(batch, text,
				box.getX() + box.getWidth() / 2 - font.getBounds(text).width
						/ 2,
				box.getY() + box.getHeight() / 2 + font.getCapHeight() / 2);
	}

	public void setActive(boolean active) {
		isActive = active;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isButtonPressed(int x, int y) {
		return box.getBoundingRectangle().contains(x, y);
	}
}
