package com.example.android_photos_51;

import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.net.Uri;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {

    public static ArrayList<Photo> iden = new ArrayList<>();
    private Context context;

    public ImageAdapter(Context c)
    {
        context = c;
    }
    public Object getItem(int index)
    {
        return null;
    }
    public long getItemId(int index)
    {
        return 0;
    }
    public int getCount()
    {
        return iden.size();
    }
    public Photo getPhoto(int index)
    {
        return iden.get(index);
    }
    public void add(Uri add)
    {
        iden.add(new Photo(add));
    }

    public View getView(int index, View convertView, ViewGroup parent)
    {
    ImageView imageView;
    if(convertView == null)
    {
        imageView = new ImageView(context);
        imageView.setLayoutParams(new GridView.LayoutParams(150, 150));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(8, 8, 8, 8);
    }
    else
    {
        imageView = (ImageView) convertView;
    }
    //to refresh image set up pass null before
    imageView.setImageURI(null);
    imageView.setImageURI(iden.get(index).getUri());
    return imageView;
    }
    public void remove(int index) {
        iden.remove(index);
    }
    public void add(Photo add)
    {
        iden.add(add);
    }
    public ArrayList<Photo> getPhotos() {
        return iden;
    }
    public void clear() {
        iden.clear();
    }
}
