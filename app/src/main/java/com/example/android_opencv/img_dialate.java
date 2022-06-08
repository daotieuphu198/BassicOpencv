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

public class img_dialate extends AppCompatActivity {
    ImageView imgdialate;
    int k = 3;
    SeekBar seebardilate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_img_dialate );
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Dialate");
        OpenCVLoader.initDebug ();
        seebardilate = (SeekBar)findViewById(R.id.seebardilate);

        imgdialate = (ImageView)findViewById ( R.id.imgdialate );
        imgdialate.setImageBitmap ( BitmapHelper.getInstance ().getBitmap () );

        for (int a =0 ; a<=100; a++){
            if(k!=3){
                seebardilate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        k = progress;
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
        k = k +1;
        }


    }
    private void convexBitmap(){
        Bitmap bitmap = Bitmap.createBitmap ( BitmapHelper.getInstance ().getBitmap () );
        Dialate(bitmap);
    }
    private void Dialate(Bitmap bitmap){
        bitmap = bitmap.copy ( Bitmap.Config.ARGB_8888,true );
        Mat src = new Mat (bitmap.getHeight (),bitmap.getWidth (), CvType.CV_8UC4 );
        Mat dest = new Mat (bitmap.getHeight (),bitmap.getWidth (), CvType.CV_8UC4 );
        Utils.bitmapToMat(bitmap,src);
        Imgproc.cvtColor(src, dest, Imgproc.COLOR_BGR2GRAY);
        Imgproc.threshold(dest, dest, 100, 255, Imgproc.THRESH_BINARY);
        Mat kernelDilate = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size (k, k));
        Imgproc.dilate(dest, dest, kernelDilate);
        Bitmap img_bitmap = Bitmap.createBitmap ( dest.cols (),dest.rows (),Bitmap.Config.ARGB_8888 );
        Utils.matToBitmap ( dest,img_bitmap );
        ImageView imageView = (ImageView)findViewById ( R.id.imgdialate2 );
        imageView.setImageBitmap ( img_bitmap );
    }
}