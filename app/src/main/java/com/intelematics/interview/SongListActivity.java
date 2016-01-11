package com.intelematics.interview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import com.intelematics.interview.adapters.SongListArrayAdapter;
import com.intelematics.interview.adapters.SongRecyclerViewAdapterWithHeader;
import com.intelematics.interview.db.DBManager;
import com.intelematics.interview.listener.OnUpdateSongsListener;
import com.intelematics.interview.models.Song;
import com.intelematics.interview.tasks.DownloadSongListTask;
import com.intelematics.interview.tasks.ReadDBSongListTask;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/**
 *
 */
public class SongListActivity extends Activity implements  OnUpdateSongsListener{
//	private DBManager dbManager; // TODO Data base shouldnt being called in UI main thread
	
//	private View headerView;
//	private ListView listView; // TODO change to RecyclerView
//	private SongListArrayAdapter listAdapter;
	private SongRecyclerViewAdapterWithHeader listAdapter;
	private ArrayList<Song> songList;
	private ProgressDialog loadDialog;
	private EditText searchBox;
	private OnUpdateSongsListener songsListener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_song_list);

//		dbManager = new DBManager(this);
		
//		headerView = getLayoutInflater().inflate(R.layout.song_list_header, null, true);
		
		songList = new ArrayList<Song>();
//		listView = (ListView) findViewById(R.id.list_view);
//		listAdapter = new SongListArrayAdapter(this, songList, dbManager); /// TODO passing data base instance to adapter... NO

		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list_view);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		songsListener = (OnUpdateSongsListener) SongListActivity.this;
		listAdapter = new SongRecyclerViewAdapterWithHeader(getApplicationContext(), songList); // TODO
		recyclerView.setAdapter(listAdapter);
//		listView.addHeaderView(headerView);
//		listView.setAdapter(listAdapter);
		registerForContextMenu(recyclerView);
		
		searchBox = (EditText)findViewById(R.id.search_box);
		searchBox.addTextChangedListener(new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence sequence, int arg1, int arg2, int arg3) {
        	listAdapter.getFilter().filter(sequence);
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) { }

        @Override
        public void afterTextChanged(Editable arg0) {}
		});
		
		loadDialog = new ProgressDialog(this);
	}
	
	
	@Override
	protected void onStart() {
		super.onStart();
		displaySelectionDialog();
	}
	
	
	public void displaySelectionDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you wish to load the local copy?")
               .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   retrieveSongListFromDB();
                   }
               })
               .setNegativeButton("Download", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   retrieveSongList();
                   }
               });

		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	public void retrieveSongList(){
		loadDialog = ProgressDialog.show(this, "", "Loading. Please wait...", true, true);
//		listAdapter.clear();
		DownloadSongListTask fetchTask = new DownloadSongListTask(this, songsListener);
		fetchTask.execute();
	}
	
	public void retrieveSongListFromDB(){
		loadDialog = ProgressDialog.show(this, "", "Loading. Please wait...", true, true);
//		listAdapter.clear();
		ReadDBSongListTask fetchTask = new ReadDBSongListTask(this, songsListener);
		fetchTask.execute();
	}
	
	public void updateSongList(ArrayList<Song> list){
		songList = list;
		listAdapter.updateList(songList);
		sortListByArtist();
		loadDialog.dismiss();
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.song_list, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if(item.getTitle(). equals("Reload")) {
            displaySelectionDialog();
            return true;
        } else if(item.getTitle(). equals("Order by artist")) {
            sortListByArtist();
            return true;
        } else if(item.getTitle(). equals("Order by title")) {
				sortListByTitle();
				return true;
        } else if(item.getTitle(). equals("Order by price")) {
            sortListByPrice();
            return true;
        } else if(item.getTitle(). equals("Revert current order")) {
            reverserCurrentSort();
            return true;
        }
				return super.onMenuItemSelected(featureId, item);
	}
	
//	@Override
//	public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenuInfo menuInfo) {
//		super.onCreateContextMenu(contextMenu, view, menuInfo);
//
//		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
//		if(info.targetView == headerView){
//			return;
//		}
//
//		getMenuInflater().inflate(R.menu.song_list_context_menu, contextMenu);
//		String songTitle = ((TextView)info.targetView.findViewById(R.id.song_title)).getText().toString();
//		contextMenu.setHeaderTitle("Delete " + songTitle + " from songs list?");
//	}
//
//
	@Override
	public boolean onContextItemSelected(MenuItem item) {
//	    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	    switch (item.getItemId()) {
	        case R.id.context_menu_delete:
	            deleteSong(listAdapter.getPosition());
	            return true;

	        default:
	            return super.onContextItemSelected(item);
	    }
	}
	
	public void deleteSong(long viewID){
		songList.remove((int) viewID);
		listAdapter.updateList(songList);
	}

	public void sortListByArtist(){
		Collections.sort(songList, new Comparator<Song>() {
		    public int compare(Song song0, Song song1) {
		        return song0.getArtist().compareTo(song1.getArtist());
		    }
		});
		listAdapter.updateList(songList, searchBox.getText());
	}
	
	public void sortListByTitle(){
		Collections.sort(songList, new Comparator<Song>() {
		    public int compare(Song song0, Song song1) {
		        return song0.getTitle().compareTo(song1.getTitle());
		    }
		});
		listAdapter.updateList(songList, searchBox.getText());
	}
	
	public void sortListByPrice(){
		Collections.sort(songList, new Comparator<Song>() {
		    public int compare(Song song0, Song song1) {
		    	int priceCompare = ((Double)song0.getPrice()).compareTo((Double)song1.getPrice());
		    	if(priceCompare != 0){
		    		return priceCompare;
		    	} else{
		    		return song0.getTitle().compareTo(song1.getTitle());
		    	}
		    }
		});
		listAdapter.updateList(songList, searchBox.getText());
	}
	
	public void reverserCurrentSort(){
		Collections.reverse(songList);
		listAdapter.updateList(songList, searchBox.getText());
	}


	@Override
	public void update(ArrayList<Song> songs) {
		updateSongList(songs);
	}
}
