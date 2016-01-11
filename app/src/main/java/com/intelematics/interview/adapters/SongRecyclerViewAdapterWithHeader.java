package com.intelematics.interview.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.intelematics.interview.R;
import com.intelematics.interview.SongListActivity;
import com.intelematics.interview.models.Song;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * TODO the header is not sticky
 * Created by Eduardo on 26/12/2015.
 */
public class SongRecyclerViewAdapterWithHeader extends RecyclerView.Adapter<SongRecyclerViewAdapterWithHeader.ViewHolder>
        implements Filterable {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private Context context;
    private int currentType, position;
    private ArrayList<Song> filteredSongsList, songsList;

    public SongRecyclerViewAdapterWithHeader(Context context, ArrayList<Song> songs) {
        this.context = context;
        this.songsList = songs;
        this.filteredSongsList = songs;
    }

    @Override
    public SongRecyclerViewAdapterWithHeader.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        currentType = viewType;
        View view = null;
        if (currentType == TYPE_ITEM) {
            view = inflater.inflate(R.layout.song_list_row, parent, false);
//            return new ViewHolder(view, currentType);
        } else if (currentType == TYPE_HEADER) {
            view = inflater.inflate(R.layout.song_list_header, parent, false);
        }
        return new ViewHolder(view, currentType);
//        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(final SongRecyclerViewAdapterWithHeader.ViewHolder holder, final int position) {
        if (holder.type == TYPE_ITEM) { // or == 1
            final Song song = filteredSongsList.get(position - 1);
            holder.songName.setText(song.getTitle());
            holder.songArtist.setText(song.getArtist());
            holder.songPrice.setText(getStringBuilder(song).toString());
            Picasso.with(context).load(song.getCoverURL()).resize(60, 60).centerCrop()
                    .placeholder(R.drawable.img_cover).error(R.drawable.img_cover)
                    .into(holder.albumCover);
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    setPosition(position); // set the position to find the song
                    return false;
                }
            });
        }
    }

    @NonNull
    private StringBuilder getStringBuilder(Song song) {
        StringBuilder builder = new StringBuilder();
        builder.append("$");
        builder.append(String.valueOf(song.getPrice()));
        return builder;
    }

    @Override
    public int getItemCount() {
        return filteredSongsList.size() + 1;
    }

    /**
     * TODO this method is REALLY IMPORTANT if you want to have the RecyclerView with header, footer or both
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public int getPosition() {
        return position - 1;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void updateList(ArrayList<Song> songs) {
        currentType = 1;
        this.songsList = songs;
        this.filteredSongsList.clear();
        this.filteredSongsList.addAll(songs);
        this.notifyDataSetChanged();
//        notifyItemChanged(getPosition());

    }

    public void updateList(ArrayList<Song> songs, Editable sequence) {
        currentType = 1;
        this.songsList = songs;
        this.filteredSongsList.clear();
        this.filteredSongsList.addAll(songs);
        this.getFilter().filter(sequence);
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredSongsList = (ArrayList<Song>) results.values;
                notifyDataSetChanged();
//                notifyItemChanged(getPosition());
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<Song> filteredSongs = new ArrayList<Song>();

                constraint = constraint.toString().toLowerCase();
                for (int i = 0; i < songsList.size(); i++) {
                    Song song = songsList.get(i);
                    if (song.getArtist().toLowerCase().contains(constraint.toString())
                            || song.getTitle().toLowerCase().contains(constraint.toString()))  {
                        filteredSongs.add(song);
                    }
                }
                results.count = filteredSongs.size();
                results.values = filteredSongs;

                return results;
            }
        };
        return filter;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{

        private int type;
        private ImageView albumCover;
        private TextView songName, songPrice, songArtist;
//        private ProgressBar progressBar;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            if(viewType == TYPE_ITEM) {
                albumCover = (ImageView)itemView.findViewById(R.id.album_cover);
                songName = (TextView)itemView.findViewById(R.id.song_title);
                songArtist = (TextView)itemView.findViewById(R.id.song_artist);
                songPrice = (TextView)itemView.findViewById(R.id.song_price);
//                progressBar = (ProgressBar)itemView.findViewById(R.id.progress_bar);
                itemView.setOnCreateContextMenuListener(this);
                type= TYPE_ITEM;  // item
            }
            else{
                type = TYPE_HEADER; // header
            }
        }

        /**
         * TODO: Create the Context menu from the adapter in orde to implement it in a RecyclerView.
         * The response of the action is in the Activity
         * @param menu
         * @param v
         * @param menuInfo
         */
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(Menu.NONE, R.id.context_menu_delete, Menu.NONE, "Delete ");
            menu.setHeaderTitle("Delete " + songName.getText().toString() + " from songs list?");
        }




    }

}
