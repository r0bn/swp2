package de.hft_stuttgart.spirit.android;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import android.content.Context;
import de.hft_stuttgart.spirit.ArmlParser;
import de.hft_stuttgart.spirit.Poi;

public class ArmlLoader implements ArmlParser {
	XmlPullParserHandler parser;
	Context c;

	public ArmlLoader(Context c, XmlPullParserHandler parser) {
		this.parser = parser;
		this.c = c;
	}

	@Override
	public List<Poi> getPlacesFromAssets(String fileName) {
		List<Poi> places = null;
		try {
			places = parser.parse(c.getAssets().open(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return places;
	}

	@Override
	public List<Poi> getPlacesFromSdCard(String fullPath) {
		File file = new File(fullPath);
		if (file.exists()) {
			List<Poi> places = null;
			try {
				places = parser.parse(new FileInputStream(file));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			return places;
		} else {
			return null;
		}
	}

}
