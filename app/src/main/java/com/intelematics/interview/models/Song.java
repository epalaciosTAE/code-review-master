package com.intelematics.interview.models;

import android.graphics.Bitmap;

import java.io.Serializable;


/**
 *
 */
public class Song implements Serializable{ // TODO this should be parcelable
	private long id;
	private String coverURL;
	private String artist;
	private String title;
	private double price;
	
	private Bitmap cover; // TODO  instead of bitmap maybe just the url its ok
	
		
	public Song() {
	}

	public Song(long id, String coverURL, String artist, String title, double price) {
		this.id = id;
		this.coverURL = coverURL;
		this.artist = artist;
		this.title = title;
		this.price = price;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getCoverURL() {
		return coverURL;
	}
	public void setCoverURL(String coverURL) {
		this.coverURL = coverURL;
	}
	public Bitmap getCover() {
		return cover;
	}
	public synchronized void setCover(Bitmap cover) {
		this.cover = cover;
	}
	
}
