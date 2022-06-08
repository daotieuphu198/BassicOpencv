package com.example.android_opencv;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import Helper.BitmapHelper;

public class img_laplacian extends AppCompatActivity {
    ImageView imglaplacian;
    int kernel_size = 3;
    int scale = 1;
    int delta = 0;
    int ddepth = CvType.CV_16S;
    int i = 1;
    SeekBar seebarlaplacian;
    Mat src, src_gray = new Mat(), dst = new Mat();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_img_laplacian );
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("LaplacianOperator");
        OpenCVLoader.initDebug ();
        seebarlaplacian = (SeekBar)findViewById(R.id.seebarlaplatian);
        imglaplacian = (ImageView) findViewById(R.id.imglaplacian);
        imglaplacian.setImageBitmap ( BitmapHelper.getInstance ().getBitmap () );
        for (int a = 0 ;a <=100; a++ ){
            if(i!=1){
                seebarlaplacian.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
            i =i +1;
        }

    }
    private void convexBitmap(){
        Bitmap bitmap = Bitmap.createBitmap ( BitmapHelper.getInstance ().getBitmap () );
        laplacian(bitmap);
    }
    private void laplacian(Bitmap bitmap){
        bitmap = bitmap.copy ( Bitmap.Config.ARGB_8888,true );
        Mat src = new Mat (bitmap.getHeight (),bitmap.getWidth (), CvType.CV_8UC4 );

        Utils.bitmapToMat ( bitmap,src );
        Imgproc.GaussianBlur( src, src, new Size(3, 3), 0, 0, Core.BORDER_DEFAULT );
        Imgproc.cvtColor(src,src_gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.Laplacian( src_gray, dst, ddepth,kernel_size, i, delta, Core.BORDER_DEFAULT );
        Mat abs_lay = new Mat();
        Core.convertScaleAbs(dst,abs_lay);
        Bitmap imgBitmap = Bitmap.createBitmap ( abs_lay.cols (),abs_lay.rows (),Bitmap.Config.ARGB_8888 );
        Utils.matToBitmap ( abs_lay,imgBitmap );
        ImageView imageView = (ImageView)findViewById ( R.id.imglaplacian2 );
        imageView.setImageBitmap ( imgBitmap );
    }
}