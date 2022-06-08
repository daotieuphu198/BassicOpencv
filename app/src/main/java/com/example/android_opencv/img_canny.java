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
import org.opencv.imgproc.Imgproc;

import Helper.BitmapHelper;

public class img_canny extends AppCompatActivity {
    ImageView imgcanny;
    int i = 80;
    SeekBar seebarcanny;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_img_canny );
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Canny");
        seebarcanny = (SeekBar)findViewById(R.id.seebarcanny);

        OpenCVLoader.initDebug ();
        imgcanny = (ImageView)findViewById ( R.id.imgcanny );
        imgcanny.setImageBitmap ( BitmapHelper.getInstance ().getBitmap () );
        for(int a = 0 ; a<=100 ; a++) {
            if (i != 80) {
                seebarcanny.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        i = progress;
                        convexBitmap();
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
            } else {
                convexBitmap();
            }
            i = i+1;
        }

    }
    private void convexBitmap(){
        Bitmap bitmap = Bitmap.createBitmap ( BitmapHelper.getInstance ().getBitmap () );
        canny(bitmap);
    }
    private void canny(Bitmap bitmap){
        bitmap = bitmap.copy ( Bitmap.Config.ARGB_8888,true );
        Mat src = new Mat (bitmap.getHeight (),bitmap.getWidth (), CvType.CV_8UC4 );
        Mat dest = new Mat (bitmap.getHeight (),bitmap.getWidth (),CvType.CV_8UC4);
        Utils.bitmapToMat ( bitmap,src );
        Imgproc.Canny ( src,dest,i,i);
        Bitmap imgBitmap = Bitmap.createBitmap ( dest.cols (),dest.rows (),Bitmap.Config.ARGB_8888 );
        Utils.matToBitmap(dest,imgBitmap);
        ImageView imageView = (ImageView)findViewById ( R.id.imgcanny2);
        imageView.setImageBitmap ( imgBitmap );
    }
}