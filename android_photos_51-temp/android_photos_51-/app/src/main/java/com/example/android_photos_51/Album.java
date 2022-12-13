package com.example.android_photos_51;

import java.io.Serializable;
import java.util.ArrayList;

public class Album implements Serializable {
    private String albumName;
    public ArrayList<Photo> list = new ArrayList<Photo>();

    public Album()
    {
        //nothing to see here folks..
    }
    public void setAlbumName(String albumName)
    {
        this.albumName = albumName;
    }
    public String getAlbumName()
    {
        return albumName;
    }
    public Album(String albumName)
    {
        this.albumName = albumName;
    }
    public String toString()
    {
        return albumName;
    }
}
