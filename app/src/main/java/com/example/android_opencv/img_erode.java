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

public class img_erode extends AppCompatActivity {
    ImageView imgerode;
    SeekBar seebarerode;
    int i = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_img_erode );
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Erode");
        OpenCVLoader.initDebug ();
        seebarerode = (SeekBar)findViewById(R.id.seebarerode);

        imgerode = (ImageView)findViewById(R.id.imgerode);
        imgerode.setImageBitmap ( BitmapHelper.getInstance ().getBitmap () );
        for(int a = 0; a<=100; a++){
            if (i!=5){
                seebarerode.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
            i= i+1;
        }

    }
    private void convexBitmap(){
        Bitmap bitmap = Bitmap.createBitmap ( BitmapHelper.getInstance ().getBitmap () );
        Erode(bitmap);
    }
    private void Erode(Bitmap bitmap){
        bitmap = bitmap.copy ( Bitmap.Config.ARGB_8888,true );
        Mat src = new Mat (bitmap.getHeight (),bitmap.getWidth (), CvType.CV_8UC4 );
        Mat dest = new Mat (bitmap.getHeight (),bitmap.getWidth (), CvType.CV_8UC4 );
        Utils.bitmapToMat(bitmap,src);
        Imgproc.cvtColor ( src,dest,Imgproc.COLOR_RGB2GRAY );
        Imgproc.threshold ( dest,dest,100,255,Imgproc.THRESH_BINARY );
        Mat kernelErode = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size (i, i));
        Imgproc.erode ( dest,dest,kernelErode );
        Bitmap img_bitmap = Bitmap.createBitmap ( dest.cols (),dest.rows (),Bitmap.Config.ARGB_8888 );
        Utils.matToBitmap ( dest,img_bitmap );
        ImageView imageView = (ImageView)findViewById ( R.id.imgerode2 );
        imageView.setImageBitmap ( img_bitmap );
    }
}