package com.example.imageareapercentage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.EditText;
import android.content.Intent;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final int READ_REQUEST_CODE = 42;

    String imgName;
    EditText imageLocText;

    Uri uriToPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageLocText = (EditText) findViewById(R.id.editTextImgLoc);
        imageLocText.setEnabled(false);

    }

    public void selectImgEvent(View view) {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");

        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    public void openImgViewEvent(View view) {

        if(this.uriToPass != null){

            Intent intent = new Intent(this, ImageActivity.class);
            intent.putExtra("imgdata", uriToPass);
            intent.putExtra("imgname", imgName);

            startActivity(intent);
        }
    }

    //this method is called after the user selects an image
    //here, set the image name id the edittext, and set the uri that is passed
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            Uri uri = null;

            if (resultData != null) {

                uri = resultData.getData();

                //following to lines set the name of the image in then textbox
                setImgNameEditText(uri);
                imageLocText.setText(imgName, TextView.BufferType.NORMAL);

                this.uriToPass = uri;
            }
        }
    }

    public void setImgNameEditText(Uri uri) {

        Cursor cursor = MainActivity.this.getContentResolver().query(uri, null, null, null, null, null);

        try {
            if (cursor != null && cursor.moveToFirst()) {

                imgName = cursor.getString( cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            }
        } catch(Exception e){
            e.printStackTrace();
        } finally {
            cursor.close();
        }
    }





}
