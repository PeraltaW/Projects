package com.example.android_photos_51;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Photos extends AppCompatActivity {
    private ListView listView;
    private ArrayList<String> albums;
    public static final int EDIT_ALBUM_CODE = 1;
    public static final int ADD_ALBUM_CODE = 2;
    public static final int DELETE_ALBUM_CODE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.albums_list);
        File file = new File(Photos.this.getFilesDir(), "text");
        if (!file.exists()) {
            file.mkdir();
        }
        /*try {
            File gpxfile = new File(file, "sample");
            FileWriter writer = new FileWriter(gpxfile);
            writer.append("Yello");
            writer.flush();
            writer.close();
            Toast.makeText(Photos.this, "Saved your text", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            System.out.println("ERROR");
        }*/
        /*try {
            File myObj = new File("albums.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            //System.out.println("An error occurred.");
            e.printStackTrace();
        }*/
        try {
            File gpxfile2 = new File(file, "sample");
            FileInputStream fis = new FileInputStream(gpxfile2);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(fis));
            String albumInfo = null;
            albums = new ArrayList<String>();
            while ((albumInfo = br.readLine()) != null) {
                albums.add(albumInfo);
            }
            fis.close();
        } catch (IOException e) {
            String[] albumsList = getResources().getStringArray(R.array.albums_array);
            albums = new ArrayList<>(albumsList.length);
            for (int i = 0; i < albumsList.length; i++) {
                albums.add(albumsList[i]);
            }
        }

        listView=findViewById(R.id.albums_list);
        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.album, albums));
        listView.setOnItemClickListener((p, V, pos, id) ->
                showAlbumUtil(pos));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                addAlbum();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showAlbumUtil(int pos) {
        Bundle bundle = new Bundle();
        String album = albums.get(pos);
        bundle.putInt(ModifyPhotos.ALBUM_INDEX, pos);
        bundle.putString(ModifyPhotos.ALBUM_NAME, album);
        Intent intent = new Intent(this, ModifyPhotos.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, EDIT_ALBUM_CODE);
    }

    private void addAlbum() {
        Intent intent = new Intent(this, ModifyPhotos.class);
        startActivityForResult(intent, ADD_ALBUM_CODE);
    }

    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode != RESULT_OK) {
            return;
        }
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            return;
        }
        String albumName = bundle.getString(ModifyPhotos.ALBUM_NAME);
        int index = bundle.getInt(ModifyPhotos.ALBUM_INDEX);
        int MPResult = bundle.getInt(ModifyPhotos.ALBUM_DELETE);
        if(MPResult== DELETE_ALBUM_CODE){
            albums.remove(index);
        }
        else if (requestCode == EDIT_ALBUM_CODE) {
            String s1 = albums.set(index, albumName);
        }
        else {
            albums.add(albumName);
        }
        File file = new File(Photos.this.getFilesDir(), "text");
        try {
            File gpxfile3 = new File(file, "sample");
            FileWriter writer = new FileWriter(gpxfile3);
            writer.write("");
            for(int i = 0; i< albums.size(); i++){
                writer.append(albums.get(i));
                writer.append("\n");
            }
            writer.close();
            Toast.makeText(Photos.this, "Changes Reflected", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            System.out.println("ERROR");
        }
        listView.setAdapter(
                new ArrayAdapter<>(this, R.layout.album, albums));
    }
}