package com.intelematics.interview.api;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.intelematics.interview.Constants.Constants;
import com.intelematics.interview.db.DBManagerDAO;
import com.intelematics.interview.models.Result;
import com.intelematics.interview.models.Song;
import com.intelematics.interview.models.SongModel;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Eduardo on 23/12/2015.
 */
public class SongRestAdapter {

//    private Context context;
    private ISong iSong;
//    private ArrayList<Song> songs;

    public SongRestAdapter() {
//        this.context = context;
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(Constants.BASE_URL)
                .build();
        iSong = restAdapter.create(ISong.class);
    }

    public SongModel getSongs() {
        Log.i("RestAdapter", "getSongs: ");
        return iSong.getSongList();
//        iSong.getSongList(new Callback<SongModel>() {
//            @Override
//            public void success(SongModel song, Response response) {
//                // TODO download data
////                LocalBroadcastManager.getInstance(context)
////                        .sendBroadcast(new Intent(Constants.ACTION_DOWNLOAD_SUCCESS).putExtra(Constants.EXTRA_SONG, song));
//
//                songs = new ArrayList<Song>(song.getResultCount());
//                for (Result r : song.getResults()) {
//                    songs.add(new Song(r.getArtworkUrl100(), r.getArtistName(), r.getTrackName(), r.getTrackPrice()));
//                }
//                DBManagerDAO.getInstance(context).saveSongsList(songs);
//            }
//
//
//            @Override
//            public void failure(RetrofitError error) {
//                //TODO handle errors
//            }
//        });
    }
}
