package com.example.android_photos_51;

import android.net.Uri;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

    public class Photo  implements Serializable {
    private Uri uri;

    public ArrayList<Tag> tags = new ArrayList<Tag>();

    public Photo (Uri uri)

{
    this.uri = uri;
}
    public Uri getUri() {
        return uri;
    }

    public void addTag(String tag){
        String data = tag.substring(tag.indexOf("=")+1);
        tag = tag.substring(0,tag.indexOf("="));
        tags.add(new Tag(tag, data));
        return;
    }
    public void addTag(String type, String data){
      tags.add(new Tag(type, data));
        return;
    }
    public String toString()
    {
        String str = uri.toString();
        for ( Tag i : tags)
        {
            str = str + "\nTag " + i;
        }
        return str;
    }
}
