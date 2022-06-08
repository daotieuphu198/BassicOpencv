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

public class img_threshold extends AppCompatActivity {
    ImageView imgthreshold;
    int i = 120;
    SeekBar seebarthreshol;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_img_threshold );
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Threshold");
        OpenCVLoader.initDebug ();
        seebarthreshol = (SeekBar)findViewById(R.id.seebarthreshol);
        imgthreshold = (ImageView)findViewById ( R.id.imgthreshold );
        imgthreshold.setImageBitmap ( BitmapHelper.getInstance ().getBitmap () );
        for (int a = 0 ; a<=100; a++){
            if(i!=120){
                seebarthreshol.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
            }else {
                convexBitmap();
            }
            i++;
        }

    }
    private void convexBitmap(){
        Bitmap bitmap = Bitmap.createBitmap ( BitmapHelper.getInstance ().getBitmap () );
        threshold(bitmap);
    }
    private void threshold(Bitmap bitmap){
        bitmap = bitmap.copy ( Bitmap.Config.ARGB_8888,true );
        Mat src = new Mat (bitmap.getHeight (),bitmap.getWidth (), CvType.CV_8UC4 );
        Mat dest = new Mat (bitmap.getHeight (),bitmap.getWidth (),CvType.CV_8UC4);
        Utils.bitmapToMat ( bitmap,src );
        Imgproc.cvtColor ( src,dest,Imgproc.COLOR_RGB2GRAY );
        Imgproc.threshold ( dest,dest,i,255,Imgproc.THRESH_BINARY);
        Bitmap imgBitmap = Bitmap.createBitmap ( dest.cols (),dest.rows (),Bitmap.Config.ARGB_8888 );
        Utils.matToBitmap(dest,imgBitmap);
        ImageView imageView = (ImageView)findViewById ( R.id.imgthreshold2);
        imageView.setImageBitmap ( imgBitmap );
    }
}