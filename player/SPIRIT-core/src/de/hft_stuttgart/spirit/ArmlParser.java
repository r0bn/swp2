package de.hft_stuttgart.spirit;

import java.util.List;

public interface ArmlParser {

	List<Poi> getPlacesFromAssets(String fileName);
	List<Poi> getPlacesFromSdCard(String fullPath);
	
	
}
