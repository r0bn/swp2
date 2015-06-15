package de.hft_stuttgart.spirit;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Sonar {
	BitmapFont font;
	Sprite bg;
	Sprite point;
	Sprite sonar;
	Sprite scan;
	float scantime = 1250f;
	GeoTools geoTools;
	float angleToGhost = 0;
	float distanceToGhost = 0;
	float maxDistance = 500;
	String textAnzeige = "";

	public Sonar(TextureAtlas sonarAtlas, BitmapFont font, GeoTools geoTools) {
		this.font = font;
		this.geoTools = geoTools;
		/*
		 * hintergrund -> wird auf vollbild gestreckt
		 */
		bg = sonarAtlas.createSprite("hg_musterplatine");
		bg.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		/*
		 * Geisterpunkt
		 */
		point = sonarAtlas.createSprite("sonar_punkt");
		point.setSize(0.05f * Gdx.graphics.getHeight(),
				0.05f * Gdx.graphics.getHeight());
		/*
		 * Sonargrafik -> quadratisch -> Skalierung mit Bildschirmhöhe
		 * Darstellung zentriert
		 */
		sonar = sonarAtlas.createSprite("sonar_scheibe");
		sonar.setSize(Gdx.graphics.getHeight(), Gdx.graphics.getHeight());
		sonar.setX((Gdx.graphics.getWidth() - Gdx.graphics.getHeight()) / 2f);
		/*
		 * Scangrafik
		 */
		scan = sonarAtlas.createSprite("sonar_schein3");

	}

	public void draw(SpriteBatch batch, ArrayList<GhostLocation> ghosts) {
		// erst der hintergrund
		bg.draw(batch);
		// dann die sonargrafik
		sonar.draw(batch);

		// dann alle geisterpunkte
		for (GhostLocation ghost : ghosts) {
			angleToGhost = (float) Math.toRadians(-geoTools
					.getBearingToLocationDegrees(ghost.location));
			distanceToGhost = geoTools.getDistance(ghost.location);
			// System.out.println(ghost.location.name + ": " + angleToGhost
			// + "°, " + distanceToGhost + "m");
			float x = (float) Math.cos(angleToGhost);
			float y = (float) Math.sin(angleToGhost);
			if (distanceToGhost <= maxDistance) {
				x *= ((float) distanceToGhost / (float) maxDistance);
				y *= ((float) distanceToGhost / (float) maxDistance);
			}
			// jetzt x und y auf die auflösung unrechnen
			x *= (Gdx.graphics.getHeight() / 2f);
			y *= (Gdx.graphics.getHeight() / 2f);
			// berechnet: abstand vom zentrum!
			x += (Gdx.graphics.getWidth() / 2f);
			y += (Gdx.graphics.getHeight() / 2f);
			point.setPosition(x - point.getWidth() / 2, y - point.getHeight()
					/ 2);
			point.draw(batch);
			// text zu den geistern
			font.setScale(1.0f);
			font.setScale(0.032f * Gdx.graphics.getHeight()
					/ font.getCapHeight());
			font.setColor(0.0f, 1.0f, 0.0f, 1.0f);
			textAnzeige = "";
			if (ghost.showNameInRadar) {
				textAnzeige = ghost.location.name + ", ";
			}
			textAnzeige += (int)distanceToGhost + "m";
			if (point.getY() > Gdx.graphics.getHeight() / 2) {
				font.draw(
						batch,
						textAnzeige,
						point.getX() - 0.5f * font.getBounds(textAnzeige).width,
						point.getY());
			} else {
				font.draw(
						batch,
						textAnzeige,
						point.getX() - 0.5f * font.getBounds(textAnzeige).width,
						point.getY()+2 * font.getCapHeight() + point.getHeight()/2);
			}
		}

		//
		// und noch die scananimation
		float scale = (System.currentTimeMillis() % (int) scantime) / scantime;
		scan.setSize(scale * Gdx.graphics.getHeight(),
				scale * Gdx.graphics.getHeight());
		scan.setPosition((Gdx.graphics.getWidth() - scan.getWidth()) / 2f,
				(Gdx.graphics.getHeight() - scan.getHeight()) / 2f);
		scan.draw(batch);
	}
}
