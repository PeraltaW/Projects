package com.example.android_photos_51;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class Tag extends AppCompatActivity implements Serializable {
    public String type;
    private String data;
    private CheckBox locat, person;
    private EditText tData;
    private int itype = -1;
    private Button add, cancel;
    private RadioGroup group;

    public Tag(String type, String data) {
        this.type = type;
        this.data = data;

    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public String toString() {
        return type + "=" + data;
    }
    public void saveToFile() {
        try {
            ArrayList<Photo> edin = AlbumContent.imgAdapter.getPhotos();
            String str = "";
            FileOutputStream fileOutputStream = openFileOutput(ModifyPhotos.pAlbumName + ".list", MODE_PRIVATE);
            for (Photo j : edin) {
                if (str.length() == 0) {
                    str = j.getUri().toString();
                } else {
                    str = str + "\n" + j.getUri().toString();
                }
                for (Tag t : j.tags) {
                    str = str + "\nTag: " + t.toString();
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
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newtag);

        locat = (CheckBox) findViewById(R.id.locat);
        person = (CheckBox) findViewById(R.id.person);
        tData = (EditText) findViewById(R.id.tData);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        add = (Button) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> tags = new ArrayList<>();
                for (Tag t : AlbumContent.imgAdapter.iden.get(SlideShow.index).tags) {
                    if (!(tags.contains(t.toString()))) {
                        tags.add(t.toString());

                    }
                }
                String userInput = tData.getText().toString();
               // itype = group.getCheckedRadioButtonId()
                if(person.isChecked()){
                if (userInput == null || userInput.length() == 0) {
                    //switch (itype) {
                    // case 2131165249:
                    if (tags.contains("Person=" + userInput)) {
                        Toast.makeText(getApplicationContext(), "Tag already exists", Toast.LENGTH_SHORT).show();
                    } else {
                        AlbumContent.imgAdapter.iden.get(SlideShow.index).addTag("Person=" + tData.getText().toString());
                        SlideShow.gridView.setAdapter(SlideShow.tagAdap);
                        saveToFile();
                        finish();
                    }
                }}
                if(locat.isChecked())
                {

                      //  case 2131165275:
                    if (userInput == null || userInput.length() == 0) {
                        if (tags.contains("Location=" + userInput)) {
                            Toast.makeText(getApplicationContext(), "Tag already exists", Toast.LENGTH_SHORT).show();
                        } else {
                            AlbumContent.imgAdapter.iden.get(SlideShow.index).addTag("Location=" + tData.getText().toString());
                            SlideShow.gridView.setAdapter(SlideShow.tagAdap);
                            saveToFile();
                            finish();
                        }
                    }

                    }

            }
        });
    }

}
