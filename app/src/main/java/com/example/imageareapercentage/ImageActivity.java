package com.example.imageareapercentage;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileDescriptor;

public class ImageActivity extends AppCompatActivity {

    BitmapImageData bid;

    Uri uri;
    String imgName;

    ImageView imgView;
    TextView textViewImgname;

    int initialX;
    int initialY;
    int releaseX;
    int releaseY;

    Matrix matrix1;
    Matrix matrix2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        uri = (Uri) getIntent().getParcelableExtra("imgdata");
        imgName = (String) getIntent().getStringExtra("imgname");

        imgView = (ImageView) findViewById(R.id.imageView);
        textViewImgname = (TextView) findViewById(R.id.textView);

        try {

            bid = new BitmapImageData(getBmpData(uri));
        } catch (Exception e) {

            e.printStackTrace();
        }

        imgView.setImageBitmap(bid.getBmp());
        textViewImgname.setText(imgName);

        //matrix classes are needed to get the exact area pressed by user
        matrix1 = new Matrix();
        matrix2 = new Matrix();

        imgView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                imgView.getImageMatrix().invert(matrix1);
                imgView.getImageMatrix().invert(matrix2);

                if(event.getAction() == MotionEvent.ACTION_DOWN) {

                    //get initial x and y pressed
                    float[] points1 = {event.getX(),event.getY()};
                    matrix1.mapPoints(points1);

                    initialX = (int) points1[0];
                    initialY = (int) points1[1];


                } else if (event.getAction() == MotionEvent.ACTION_UP) {

                    //get x and y pressed after release
                    float[] points2 = {event.getX(),event.getY()};
                    matrix2.mapPoints(points2);

                    releaseX = (int) points2[0];
                    releaseY = (int) points2[1];

                    if(initialY > bid.getBmp().getHeight() || releaseY > bid.getBmp().getHeight()) {

                        textViewImgname.setText("Touch went out of picture bounds.");
                    }
                    else {


                        bid.drawRectToImg(initialX, initialY, releaseX, releaseY);
                        imgView.setImageBitmap(bid.getBmp());
                    }
                }

                return true;
            }
        });
    }

    //this is needed to create the bitmap from the passed uri
    public Bitmap getBmpData(Uri uri) throws Exception {

        ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }
}
