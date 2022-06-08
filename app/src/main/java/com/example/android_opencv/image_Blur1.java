package com.example.android_opencv;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import Helper.BitmapHelper;

public class image_Blur1 extends AppCompatActivity {
    ImageView imgblur, imgblur2;
    SeekBar seekBar;
    int i=20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_blur1);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Blur");
        OpenCVLoader.initDebug();
        imgblur = (ImageView)findViewById(R.id.imgblur1);

        seekBar = (SeekBar)findViewById(R.id.seekBar);

        imgblur.setImageBitmap(BitmapHelper.getInstance().getBitmap());
        for(int a = 0; a<=100;a++){


            if (i != 20) {
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        Log.d("aaaa", "gia tri"+ progress);

                        i = progress;
                        convexBitmap();


                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        Log.d("Start", "oke");
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        Log.d("", "end");
                    }
                });
            } else {

                convexBitmap();
            }
            i=i+1;
        }

    }
    private void convexBitmap(){
        Bitmap bitmap = Bitmap.createBitmap ( BitmapHelper.getInstance ().getBitmap () );
        Blur(bitmap);
    }
    private void Blur(Bitmap bitmap){
        bitmap = bitmap.copy ( Bitmap.Config.ARGB_8888,true );
        Mat src = new Mat (bitmap.getHeight (),bitmap.getWidth (), CvType.CV_8UC4 );
        Mat dest = new Mat (bitmap.getHeight (),bitmap.getWidth (),CvType.CV_8UC4);
        Utils.bitmapToMat ( bitmap,src );
        Imgproc.blur ( src,dest,new Size(i,i));
        Bitmap img_bitmap = Bitmap.createBitmap ( dest.cols (),dest.rows (),Bitmap.Config.ARGB_8888 );
        Utils.matToBitmap ( dest,img_bitmap );
        ImageView imageView = (ImageView)findViewById(R.id.imgblur2) ;
        imageView.setImageBitmap ( img_bitmap );
    }

}