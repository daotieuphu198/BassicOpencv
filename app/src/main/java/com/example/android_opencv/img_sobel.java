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
import org.opencv.imgproc.Imgproc;

import Helper.BitmapHelper;

public class img_sobel extends AppCompatActivity {
    ImageView imgsobel;
    int i = 1;
    SeekBar seebarsobel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_img_sobel );
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("SoBel");
        OpenCVLoader.initDebug ();
        seebarsobel = (SeekBar)findViewById(R.id.seebarsobel);
        imgsobel = (ImageView)findViewById ( R.id.imgsobel );
        imgsobel.setImageBitmap ( BitmapHelper.getInstance ().getBitmap () );
        convexBitmap();
        for (int a = 0; a<=100;a++){
            if (i!=1){
                seebarsobel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        Log.d("aaa","giatri" + progress);
                        switch (progress){
                            case 0:
                                i = 1;
                                break;
                            case 1:
                                i=2;
                                break;
                            default:
                                break;
                        }

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
            i++;
        }
    }
    private void convexBitmap(){
        Bitmap bitmap = Bitmap.createBitmap ( BitmapHelper.getInstance ().getBitmap () );
        sobel(bitmap);
    }
    private void sobel(Bitmap bitmap){
        bitmap = bitmap.copy ( Bitmap.Config.ARGB_8888,true );
        Mat src = new Mat (bitmap.getHeight (),bitmap.getWidth (), CvType.CV_8UC4 );
        Mat dest = new Mat (bitmap.getHeight (),bitmap.getWidth (),CvType.CV_8UC4);
        Utils.bitmapToMat ( bitmap,src );
        Imgproc.Sobel ( src,dest,-1,i,i );
        Bitmap imgBitmap = Bitmap.createBitmap ( dest.cols (),dest.rows (),Bitmap.Config.ARGB_8888 );
        Utils.matToBitmap(dest,imgBitmap);
        ImageView imageView = (ImageView)findViewById ( R.id.imgsobel2);

        imageView.setImageBitmap ( imgBitmap );
    }
}