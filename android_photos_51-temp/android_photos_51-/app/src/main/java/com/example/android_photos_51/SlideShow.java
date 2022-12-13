package com.example.android_photos_51;

import static java.security.AccessController.getContext;

import android.content.pm.PackageManager;
import android.os.Bundle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.os.Messenger;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class SlideShow extends AppCompatActivity {

    public static int index;
    private int indexTag = -1;
    public static GridView gridView;
    public static ArrayAdapter tagAdap;
    public ImageView imgView;
    public static Button add;
    private Button next, delete, prev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slides_show);
        index = AlbumContent.index;

        tagAdap = new ArrayAdapter<Tag>(this, android.R.layout.simple_list_item_1, AlbumContent.imgAdapter.iden.get(index).tags);
        gridView = (GridView) findViewById(R.id.gridView);
        imgView = (ImageView) findViewById(R.id.imageView);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterViw, View view, int pos, long id) {
                indexTag = pos;
            }
        });
        add = (Button) findViewById(R.id.tagAdd);
        delete = (Button) findViewById(R.id.tagDel);
        next = (Button) findViewById(R.id.next);
        prev = (Button) findViewById(R.id.prev);
        if (AlbumContent.imgAdapter.iden.get(index).tags.size() == 0) {
            delete.setVisibility(View.INVISIBLE);
        } else {
            delete.setVisibility(View.VISIBLE);
        }
        if (AlbumContent.imgAdapter.getCount() == 1) {
            //prev.setVisibility(View.INVISIBLE);
            //next.setVisibility(View.INVISIBLE);
        } else if (index == 0) {
            prev.setVisibility(View.INVISIBLE);
        } else if (index == AlbumContent.imgAdapter.getCount() - 1) {
            next.setVisibility(View.INVISIBLE);
        }
        try {
            InputStream pictureInputStream = getContentResolver().openInputStream(AlbumContent.imgAdapter.iden.get(index).getUri());
            Bitmap crntPic = BitmapFactory.decodeStream(pictureInputStream);
            imgView.setImageBitmap(crntPic);
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (index < AlbumContent.imgAdapter.getCount() - 1) {
                    index++;
                    if (AlbumContent.imgAdapter.iden.get(index).tags.size() == 0) {
                        delete.setVisibility(View.INVISIBLE);
                    } else {
                        delete.setVisibility(View.VISIBLE);
                    }

                    if (prev.getVisibility() == View.INVISIBLE) {
                        prev.setVisibility(View.VISIBLE);
                    }
                    if (index == AlbumContent.imgAdapter.getCount() - 1) {
                        next.setVisibility(View.INVISIBLE);
                    }
                    try {

                        InputStream pictureInputStream = getContentResolver().openInputStream(AlbumContent.imgAdapter.iden.get(index).getUri());
                        Bitmap currPic = BitmapFactory.decodeStream(pictureInputStream);
                        imgView.setImageBitmap(currPic);

                        tagAdap = new ArrayAdapter<Tag>(getApplicationContext(), android.R.layout.simple_list_item_1, AlbumContent.imgAdapter.iden.get(index).tags);

                        gridView.setAdapter(tagAdap);
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(index > 0)
                {
                    index--;
                    if(AlbumContent.imgAdapter.iden.get(index).tags.size() ==0)
                    {
                        delete.setVisibility(View.INVISIBLE);
                    }else
                    {
                        delete.setVisibility(View.VISIBLE);
                    }
                    if (next.getVisibility() == View.INVISIBLE)
                    {
                        next.setVisibility(View.VISIBLE);
                    }
                    if (index ==0)
                    {
                        prev.setVisibility(View.INVISIBLE);
                    }
                    try{
                        InputStream pictureInputStream = getContentResolver().openInputStream(AlbumContent.imgAdapter.iden.get(index).getUri());
                        Bitmap crntPic = BitmapFactory.decodeStream(pictureInputStream);
                        imgView.setImageBitmap(crntPic);

                        tagAdap = new ArrayAdapter<Tag>(getApplicationContext(), android.R.layout.simple_list_item_1, AlbumContent.imgAdapter.iden.get(index).tags);

                        gridView.setAdapter(tagAdap);
                    }
                    catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
       /* add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NewTag.class);
                startActivity(intent);
            }
        });*/

       /* delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (indexTag != -1) {
                    AlbumContent.imgAdapter.iden.get(index).tags.remove(indexTag);
                    tagAdap = new ArrayAdapter<Tag>(getApplicationContext(), android.R.layout.simple_list_item_1, AlbumContent.imgAdapter.iden.get(index).tags);

                    gridView.setAdapter(tagAdap);

                    saveTofile();
                }

                if (AlbumContent.imgAdapter.iden.get(index).tags.size() == 0) {
                    delete.setVisibility(View.INVISIBLE);
                }
            }
        });*/

    }
    public void saveTofile() {
        try {
            ArrayList<Photo> iden = AlbumContent.imgAdapter.getPhotos();
            ArrayList<Tag> tags = new ArrayList<>();

            String str = "";
            FileOutputStream fileOutputStream = openFileOutput(ModifyPhotos.pAlbumName + ".list", MODE_PRIVATE);
            for (Photo j : iden) {
                if (str.equals("")) {
                    str = j.toString();
                } else {
                    str = str + "\n" + j.toString();
                }
            }
            fileOutputStream.write(str.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    protected void onResume() {
        super.onResume();

        if (AlbumContent.imgAdapter.iden.get(index).tags.size() == 0) {
            delete.setVisibility(View.INVISIBLE);
        } else {

            delete.setVisibility(View.VISIBLE);
        }
    }
}