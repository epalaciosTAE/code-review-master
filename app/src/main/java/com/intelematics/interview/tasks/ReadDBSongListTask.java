package com.intelematics.interview.tasks;

import java.util.ArrayList;

import com.intelematics.interview.SongListActivity;
import com.intelematics.interview.db.DBManager;
import com.intelematics.interview.db.DBManagerDAO;
import com.intelematics.interview.db.SongManager;
import com.intelematics.interview.listener.OnUpdateSongsListener;
import com.intelematics.interview.models.Song;

import android.content.Context;
import android.os.AsyncTask;

/**
 *
 */
public class ReadDBSongListTask extends AsyncTask<Void, Void, ArrayList<Song>> {
	//	private DBManager dbManager;
//	private SongListActivity activity;
	private Context context;
	private OnUpdateSongsListener songsListener;
//	private ArrayList<Song> songList;
	

	public ReadDBSongListTask(Context context, OnUpdateSongsListener songsListener) {
//		this.activity = activity;
//		this.dbManager = dbManager;
//		songList = new ArrayList<Song>();
		this.context = context;
		this.songsListener = songsListener;
	}

	
	@Override
	protected ArrayList<Song> doInBackground(Void... params) {
//		SongManager songManager = new SongManager(activity, dbManager);
//		songList = songManager.getSongsList();

//		songList = DBManagerDAO.getInstance(activity.getApplicationContext()).getSongsList();
		
		return DBManagerDAO.getInstance(context).getSongsList();
	}

    protected void onPostExecute(ArrayList<Song> result) {
//    	activity.updateSongList(result);
		songsListener.update(result);
    }

	@Override
	protected void onCancelled() {
		super.onCancelled();
		songsListener = null;
	}


}
