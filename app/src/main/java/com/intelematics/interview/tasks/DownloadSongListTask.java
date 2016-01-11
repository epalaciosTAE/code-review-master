package com.intelematics.interview.tasks;

import java.util.ArrayList;
import java.util.List;

import com.intelematics.interview.R;
import com.intelematics.interview.SongListActivity;
import com.intelematics.interview.api.SongRestAdapter;
import com.intelematics.interview.db.DBManager;
import com.intelematics.interview.db.DBManagerDAO;
import com.intelematics.interview.db.SongManager;
import com.intelematics.interview.listener.OnUpdateSongsListener;
import com.intelematics.interview.models.Result;
import com.intelematics.interview.models.Song;
import com.intelematics.interview.net.ConnectionManager;
import com.intelematics.interview.util.JsonParser;

import android.content.Context;
import android.os.AsyncTask;

/**
 *
 */
public class DownloadSongListTask extends AsyncTask<Void, Void, ArrayList<Song>> { // TODO return a list to onpostExeute(), Dont pass any list in constructor, interface to get result in activity
//	private DBManager dbManager;
//	private SongListActivity activity;
	private OnUpdateSongsListener songsListener;
	private Context context;

//	private ConnectionManager connectionManager;
	
	public DownloadSongListTask(Context context, OnUpdateSongsListener songsListener) {
//		this.activity = activity;
		this.context = context;
		this.songsListener = songsListener;
//		this.dbManager = dbManager;
//		songList = new ArrayList<Song>();
	}
	
	@Override
	protected ArrayList<Song> doInBackground(Void... params) {
//		JsonParser parser = new JsonParser();

        // Rock version of the app
//        connectionManager = new ConnectionManager(activity, "https://itunes.apple.com/search?term=rock&amp;media=music&amp;entity=song&amp;limit=50");

        // Pop version of the app
        //connectionManager = new ConnectionManager(activity, https://itunes.apple.com/search?term=popk&amp;media=music&amp;entity=song&amp;limit=50);

        // Classic version of the app
        //connectionManager = new ConnectionManager(activity, https://itunes.apple.com/search?term=classick&amp;media=music&amp;entity=song&amp;limit=50);


//		songList = parser.parseSongList(connectionManager.requestJson());
//		connectionManager.closeConnection();
		
//		SongManager songManager = new SongManager(activity, dbManager);
//		songManager.saveSongsList(songList);

		SongRestAdapter restAdapter = new SongRestAdapter();
//		restAdapter.getSongs();
		List<Result> results = restAdapter.getSongs().getResults();
		ArrayList<Song> songList = new ArrayList<>(results.size());
		for (Result r : results) {
			songList.add(new Song(r.getTrackId(), r.getArtworkUrl100(), r.getArtistName(), r.getTrackName(), r.getTrackPrice()));
			}
		DBManagerDAO.getInstance(context).saveSongsList(songList);
//		DBManagerDAO.getInstance(activity.getApplicationContext()).saveSongsList(songList);
		
		return songList;
	}

    protected void onPostExecute(ArrayList<Song> result) {
//    	activity.updateSongList(result);
//		activity.updateSongList(songList);
		songsListener.update(result);
    }


	@Override
	protected void onCancelled() {
		super.onCancelled();
		songsListener = null;
	}
}
