package de.hft_stuttgart.spirit;

import java.util.ArrayList;

public class Playlist {
	private ArrayList<PlaylistEntry> playlist;

	public Playlist(){
		playlist = new ArrayList<PlaylistEntry>();
	}
	
	public void addFilm(PlaylistEntry newFilm) {
		playlist.add(newFilm);
	}
	
	public void addFilmAtPosition(PlaylistEntry newFilm, int position){
		playlist.add(position, newFilm);
	}
	public boolean isNextFilmAvailable(){
		return (playlist.size() > 0);
	}
	
	public void resetPlaylist(){
		playlist = new ArrayList<PlaylistEntry>();
	}
	
	public int getRemainingPlaylistEntries(){
		return playlist.size();
	}
	
	public PlaylistEntry getNextPlaylistEntry(){
		if (isNextFilmAvailable()){
		PlaylistEntry nextFilm = playlist.get(0);
		playlist.remove(0);
		return nextFilm;
		}else{
			return null;
		}
	}
	
	public boolean isNextFilmAutostart(){
		if (isNextFilmAvailable()){
			return playlist.get(0).isAutostartEnabled();
		}else{
			return false;
		}
	}
}
