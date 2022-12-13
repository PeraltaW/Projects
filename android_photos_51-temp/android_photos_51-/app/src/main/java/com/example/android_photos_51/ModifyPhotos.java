package com.example.android_photos_51;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.content.Intent;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import android.widget.EditText;
import android.widget.GridView;

public class ModifyPhotos extends AppCompatActivity {


    public static final String ALBUM_INDEX = "movieIndex";
    public static final String ALBUM_NAME = "movieName";
    public static final String ALBUM_DELETE = "movieDelete";
    private int albumIndex;
    private EditText albumName;
    public static String pAlbumName;
    public static GridView gridView;
    public static Photo copy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_modify_photos);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        albumName = findViewById(R.id.album_name);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            albumIndex = bundle.getInt(ALBUM_INDEX);
            albumName.setText(bundle.getString(ALBUM_NAME));
            pAlbumName=bundle.getString(ALBUM_NAME);
        }
    }

    public void cancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void save(View view) {
        String userInput = albumName.getText().toString();
        if (userInput == null || userInput.length() == 0) {
            Bundle bundle = new Bundle();
            bundle.putString(AlbumDialogFragment.MESSAGE_KEY,
                    "Album name cannot be left blank");
            DialogFragment newFragment = new AlbumDialogFragment();
            newFragment.setArguments(bundle);
            newFragment.show(getSupportFragmentManager(), "badfields");
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putInt(ALBUM_INDEX, albumIndex);
        bundle.putString(ALBUM_NAME,userInput);
        bundle.putInt(ALBUM_DELETE, 1);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
        finish(); // pops activity from the call stack, returns to parent
    }

    public void delete(View view) {
        if(pAlbumName==null){
            return;
        }
        String name = albumName.getText().toString();
        Bundle bundle = new Bundle();
        bundle.putInt(ALBUM_INDEX, albumIndex);
        bundle.putString(ALBUM_NAME,name);
        bundle.putInt(ALBUM_DELETE, 3);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
        finish();
    }

    public void open(View view) {
        if(pAlbumName==null){
            return;
        }
        Intent intent = new Intent(this, AlbumContent.class);
        startActivityForResult(intent, 2);
    }
}