package com.intelematics.interview.api;

import com.intelematics.interview.Constants.Constants;
import com.intelematics.interview.models.Song;
import com.intelematics.interview.models.SongModel;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by Eduardo on 23/12/2015.
 */
public interface ISong {

    @GET(Constants.URL_END_POINT)
    public SongModel getSongList ();
//    public void getSongList (Callback<SongModel> response);
}
