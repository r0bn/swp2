package de.hft_stuttgart.spirit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.particles.values.WeightMeshSpawnShapeValue;

public class Gui {
	Sprite button;
	Sprite drehrad;
	Sprite auswahlrahmen;
	Sprite skip;
	Sprite start;
	Sprite stopp;
	Sprite sonar;
	Sprite auge;
	Sprite magequip;

	public String infoText = "";
	public String buttonText = "";
	public String statusText = "";

	BitmapFont font;

	// TODO: SPIRIT Gui...
	int winkel_base = 135;
	double dist; // abstand zum zentrum

	// schriftgröße skalieren, angepasst auf 1920x1080
	float fontScale = 0.75f * Gdx.graphics.getHeight() / 1080f;
	float buttonFontScale;
	float infoFontScale;

	public Gui(TextureAtlas guiAtlas, BitmapFont font) {
		this.font = font;
		drehrad = guiAtlas.createSprite("drehrad_linien_kreis");
		float scale = (Gdx.graphics.getWidth() / 1.4f) / drehrad.getHeight();
		drehrad.setSize(drehrad.getWidth() * scale, drehrad.getHeight() * scale);
		drehrad.setCenter(Gdx.graphics.getWidth(), 0);

		dist = 0.42 * drehrad.getWidth();

		button = guiAtlas.createSprite("button_eckig2");
		scale = (drehrad.getWidth() / 2) / button.getWidth();
		button.setSize(drehrad.getWidth() / 2,
				1.2f * scale * button.getHeight());
		button.setPosition(Gdx.graphics.getWidth() - button.getWidth(), 0);

		auswahlrahmen = guiAtlas.createSprite("auwahlrahmen");
		scale = (drehrad.getWidth() / 5f) / auswahlrahmen.getWidth();
		auswahlrahmen.setSize(auswahlrahmen.getWidth() * scale,
				auswahlrahmen.getHeight() * scale);
		auswahlrahmen.setCenter(
				(float) (Gdx.graphics.getWidth() + dist
						* Math.cos(Math.toRadians(winkel_base))),
				(float) (dist * Math.sin(Math.toRadians(winkel_base))));

		skip = guiAtlas.createSprite("skip");
		scale = (drehrad.getWidth() / 6) / skip.getWidth();
		skip.setSize(scale * skip.getWidth(), scale * skip.getHeight());

		start = guiAtlas.createSprite("start");
		scale = (drehrad.getWidth() / 6) / start.getWidth();
		start.setSize(scale * start.getWidth(), scale * start.getHeight());

		stopp = guiAtlas.createSprite("stopp");
		scale = (drehrad.getWidth() / 6) / stopp.getWidth();
		stopp.setSize(scale * stopp.getWidth(), scale * stopp.getHeight());

		sonar = guiAtlas.createSprite("sonar");
		scale = (drehrad.getWidth() / 6) / sonar.getWidth();
		sonar.setSize(scale * sonar.getWidth(), scale * sonar.getHeight());

		auge = guiAtlas.createSprite("auge");
		scale = (drehrad.getWidth() / 6) / auge.getWidth();
		auge.setSize(scale * auge.getWidth(), scale * auge.getHeight());

		magequip = guiAtlas.createSprite("magequip");
		magequip.setSize(2 * Gdx.graphics.getWidth(), button.getHeight());
		magequip.setPosition(-magequip.getWidth() / 2, 0);

	}

	public void draw(SpriteBatch batch) {
		radDrehen();

//		drehrad.draw(batch);
//
//		skip.draw(batch);
//		start.draw(batch);
//		stopp.draw(batch);
//		sonar.draw(batch);
//		auge.draw(batch);
//		auswahlrahmen.draw(batch);
		if (!buttonText.isEmpty()) {
			button.draw(batch);
		}
		if (!infoText.isEmpty()) {
			magequip.draw(batch);
		}

		if ((font.getBounds(buttonText).width + 2 * font.getSpaceWidth()) > button
				.getWidth()) {
			buttonFontScale = fontScale
					* (button.getWidth() / (font.getBounds(buttonText).width + 2 * font
							.getSpaceWidth()));
		} else {
			buttonFontScale = fontScale;
		}
		font.setScale(buttonFontScale);
		if (!buttonText.isEmpty()) {
			font.setColor(0.0f, 0.0f, 0.0f, 1.0f);
			font.draw(
					batch,
					buttonText,
					Gdx.graphics.getWidth() - font.getBounds(buttonText).width
							- font.getSpaceWidth(),
					(magequip.getHeight() - font.getCapHeight()) / 2
							+ font.getCapHeight());
		}

		if ((font.getBounds(infoText).width + 2 * font.getSpaceWidth()) > (Gdx.graphics
				.getWidth() - button.getWidth())) {
			infoFontScale = fontScale
					* ((Gdx.graphics.getWidth() - button.getWidth()) / (font
							.getBounds(infoText).width + 2 * font
							.getSpaceWidth()));
		} else {
			infoFontScale = fontScale;
		}
		font.setScale(infoFontScale);
		if (!infoText.isEmpty()) {
			font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
			font.draw(
					batch,
					infoText,
					font.getSpaceWidth(),
					(magequip.getHeight() - font.getCapHeight()) / 2
							+ font.getCapHeight());

		}

		font.setScale(fontScale);
		if (!statusText.isEmpty() && (System.currentTimeMillis() % 1000 < 500)) {
			font.setColor(0.0f, 0.88f, 1.0f, 1.0f);
			font.draw(batch, statusText,
					Gdx.graphics.getWidth() - font.getBounds(statusText).width
							- font.getSpaceWidth(), Gdx.graphics.getHeight()
							- font.getSpaceWidth());

		}
	}

	private void radDrehen() {
		winkel_base = (int) ((360f / 4000f) * (System.currentTimeMillis() % 4000));
		skip.setCenter(
				(float) (Gdx.graphics.getWidth() + dist
						* Math.cos(Math.toRadians(winkel_base))),
				(float) (dist * Math.sin(Math.toRadians(winkel_base))));
		winkel_base += 72;
		start.setCenter(
				(float) (Gdx.graphics.getWidth() + dist
						* Math.cos(Math.toRadians(winkel_base))),
				(float) (dist * Math.sin(Math.toRadians(winkel_base))));
		winkel_base += 72;
		stopp.setCenter(
				(float) (Gdx.graphics.getWidth() + dist
						* Math.cos(Math.toRadians(winkel_base))),
				(float) (dist * Math.sin(Math.toRadians(winkel_base))));
		winkel_base += 72;
		sonar.setCenter(
				(float) (Gdx.graphics.getWidth() + dist
						* Math.cos(Math.toRadians(winkel_base))),
				(float) (dist * Math.sin(Math.toRadians(winkel_base))));
		winkel_base += 72;
		auge.setCenter(
				(float) (Gdx.graphics.getWidth() + dist
						* Math.cos(Math.toRadians(winkel_base))),
				(float) (dist * Math.sin(Math.toRadians(winkel_base))));

	}

	protected boolean isWeiterButtonTouched() {
		if (!buttonText.isEmpty()) {
			if (button.getBoundingRectangle().contains(Gdx.input.getX(),
					Gdx.graphics.getHeight() - Gdx.input.getY())) {
				return true;
			}
		}
		return false;
	}
}
