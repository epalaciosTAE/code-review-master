package com.intelematics.interview.db;

import com.intelematics.interview.models.Song;

import java.util.ArrayList;

/**
 * Created by Eduardo on 24/12/2015.
 */
public interface ManagerDAO {

    public ArrayList<Song> getSongsList();
    public void deleteSongsList();
    public void saveSongsList(ArrayList<Song> list);
    public void saveSong(Song song);
}
