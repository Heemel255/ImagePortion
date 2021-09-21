package com.project_core.imageareapercentage;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileDescriptor;

public class ImageActivity extends AppCompatActivity {

    BitmapImageData bID;

    Uri uri;
    String imgName;

    ImageView imgView;
    TextView textViewImgname;

    Matrix matrix1;
    Matrix matrix2;

    int initialX;
    int initialY;
    int releaseX;
    int releaseY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        uri = (Uri) getIntent().getParcelableExtra("imgdata");
        imgName = (String) getIntent().getStringExtra("imgname");

        imgView = (ImageView) findViewById(R.id.imageView);
        textViewImgname = (TextView) findViewById(R.id.textView);

        try {

            bID = new BitmapImageData(getBmpData(uri));
        } catch (Exception e) {

            e.printStackTrace();
        }

        imgView.setImageBitmap(bID.getBmp());
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

                    float[] points1 = {event.getX(), event.getY()};
                    matrix1.mapPoints(points1);

                    initialX = (int) points1[0];
                    initialY = (int) points1[1];
                }
                else if (event.getAction() == MotionEvent.ACTION_MOVE) {

                    float[] points2 = {event.getX(), event.getY()};
                    matrix2.mapPoints(points2);

                    releaseX = (int) points2[0];
                    releaseY = (int) points2[1];


                    if(inBounds()) {

                        bID.drawRectToImg(initialX, initialY, releaseX, releaseY);
                        imgView.setImageBitmap(bID.getBmp());
                    }
                }

                return true;
            }
        });
    }

    //checks to see if touch went out of the bounds of the image displayed
    public boolean inBounds() {

        return initialY < bID.getBmp().getHeight() && releaseY < bID.getBmp().getHeight() &&
                initialX < bID.getBmp().getWidth() && releaseX < bID.getBmp().getWidth() &&
                initialY >= 0 && releaseY >= 0 && initialX >= 0 && releaseX >= 0;
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
