package com.example.android_photos_51;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.view.View.OnClickListener;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android_photos_51.databinding.ActivityAlbumContentBinding;

public class AlbumContent extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    public static final String ALBUM_INDEX = "movieIndex";
    public static final String ALBUM_NAME = "movieName";
    public static final String ALBUM_DELETE = "movieDelete";
    private EditText albumName;
    private int albumIndex;
    public static GridView gridView;
    public static ImageAdapter imgAdapter;
    public static int index = 0;
    Button add,display, delete,move;
    private static final int READ_REQUEST_CODE = 42;
    final Context c = this;
    public static Album album = new Album();
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_album_content);
        gridView = findViewById(R.id.GridView);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
     //   getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        albumName = findViewById(R.id.album_name);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            albumIndex = bundle.getInt(ALBUM_INDEX);
            albumName.setText(bundle.getString(ALBUM_NAME));
        }
        add = (Button) findViewById(R.id.add);
        display = (Button) findViewById(R.id.display);
        delete = (Button) findViewById(R.id.delete);
        move = (Button) findViewById(R.id.move);

        imgAdapter = new ImageAdapter(this);
        final GridView gridView = (GridView) findViewById(R.id.GridView);
        readFromFile();

        gridView.setAdapter(imgAdapter);

        add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, READ_REQUEST_CODE);
            }
        });
        delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(index >= 0 && index< imgAdapter.getCount())
                {
                    imgAdapter.remove(index);
                    index = -1;
                    visibility(false);
                    saveToFile();
                    gridView.setAdapter(imgAdapter);

                }
                else {
                    Toast.makeText(getApplicationContext(),"Failed to Erase Photo" + index, Toast.LENGTH_SHORT).show();
                }
            }
        });
        display.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SlideShow.class);
                startActivity(intent);
            }
        });
        gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                index = position;
                if (index!= -1){
                    visibility(true);
                }
            }
        });
        move.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
            /*    builder = new AlertDialog.Builder(c);
                builder.setTitle("Select Album; ");
                builder.setItems(bundle, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int index) {
                                //somethign to do!
                            }
                        });*/
                ModifyPhotos.copy = imgAdapter.iden.get(index);
                imgAdapter.remove(index);
                gridView.setAdapter(imgAdapter);
                saveToFile();
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == READ_REQUEST_CODE && resultCode == RESULT_OK && data != null)
        {
            index++;
            Photo picture = new Photo(data.getData());
            Uri imageUri = data.getData();
            imgAdapter.add(imageUri);
            gridView.setAdapter(imgAdapter);
            album.list.add(picture);
            saveToFile();

        }
    }
    public void readFromFile()
    {
        String[] strings = {};
        imgAdapter.clear();
        try{
            FileInputStream fileInputStream = openFileInput(ModifyPhotos.pAlbumName + ".list");

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String strIn;
            ArrayList<String> tags = new ArrayList<>();

            while((strIn = bufferedReader.readLine()) != null)
            {
                if (strIn.substring(0,4).equals("Tag: "))
                {
                    if(!(tags.contains(strIn.substring(4))))
                    {
                        tags.add(strIn.substring(4));
                        imgAdapter.getPhoto(imgAdapter.getCount() - 1).addTag(strIn.substring(4));
                    }
                }
                else{
                    imgAdapter.add(Uri.parse(strIn));
                }
            }
            gridView.setAdapter(imgAdapter);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    int i;
    boolean b;
    public void saveToFile()
    {
        try {
            ArrayList<Photo> iden = imgAdapter.getPhotos();
            String str = "";
            FileOutputStream fileOutputStream = openFileOutput(ModifyPhotos.pAlbumName + ".list", MODE_PRIVATE);
            for (Photo j : iden)
            {
                ArrayList<Tag> tags = new ArrayList<>();
                for( i =0; i< j.tags.size(); i++)
                {
                    b = false;
                    Tag t = j.tags.get(i);
                    for(Tag k: tags)
                    {
                        if (t.type.equals(k.type) && t.getData().equals(k.getData()))
                        {
                            b = true;
                            j.tags.remove(i);
                            break;
                        }
                    }
                    if(!b)
                    {
                        tags.add(t);
                    }
                }
                if(str.equals(""))
                {
                    str = j.toString();
                }
                else
                {
                    str = str + "\n" + j.toString();
                }
            }
            fileOutputStream.write(str.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e)
        {
            e.printStackTrace();
        }
    }
    private void visibility(boolean vis)
    {
        move.setVisibility(vis ? View.VISIBLE: View.INVISIBLE);
        display.setVisibility(vis ? View.VISIBLE: View.INVISIBLE);
        delete.setVisibility(vis ? View.VISIBLE: View.INVISIBLE);

    }
    @Override
    protected void onResume() {
        super.onResume();
        readFromFile();
    }
}