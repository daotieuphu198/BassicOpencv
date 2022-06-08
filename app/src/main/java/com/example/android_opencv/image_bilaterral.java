package com.example.android_opencv;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import Helper.BitmapHelper;

public class image_bilaterral extends AppCompatActivity {
    ImageView imgbilaterral;
    int g = 3;
    SeekBar seebarbila;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_image_bilaterral );
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Bilateral");
        OpenCVLoader.initDebug ();
        seebarbila = (SeekBar)findViewById(R.id.Seebarbila);

        imgbilaterral = (ImageView)findViewById ( R.id.imgbilaterral );
        imgbilaterral.setImageBitmap ( BitmapHelper.getInstance ().getBitmap () );
        for(int a=0;a<=100;a++){
            if(g!=3){
                seebarbila.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        g = progress;
                        convexBitmap();
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
            }else {
                convexBitmap();
            }
            g = g+1;
        }

    }
    private void convexBitmap(){
        Bitmap bitmap = Bitmap.createBitmap ( BitmapHelper.getInstance ().getBitmap () );
        Bilateral(bitmap);
    }
    private void Bilateral(Bitmap bitmap){
        bitmap = bitmap.copy ( Bitmap.Config.ARGB_8888,true );
        Mat src = new Mat (bitmap.getHeight (),bitmap.getWidth (), CvType.CV_8UC4 );
        Mat dest = new Mat (bitmap.getHeight (),bitmap.getWidth (), CvType.CV_8UC4 );
        Utils.bitmapToMat ( bitmap,src );
        Imgproc.cvtColor ( src,dest,Imgproc.COLOR_RGB2GRAY );
        Imgproc.threshold ( dest,dest,100,255,Imgproc.THRESH_BINARY );
        Mat nicedialter = Imgproc.getStructuringElement ( Imgproc.MORPH_RECT,new Size (g,g) );
        Imgproc.dilate ( dest,dest,nicedialter );
        Bitmap img_Bitmap = Bitmap.createBitmap ( dest.cols (),dest.rows (),Bitmap.Config.ARGB_8888 );

        Utils.matToBitmap ( dest,img_Bitmap );
        ImageView imageView = (ImageView)findViewById ( R.id.imgbilaterral2 );
        imageView.setImageBitmap ( img_Bitmap );

    }
}