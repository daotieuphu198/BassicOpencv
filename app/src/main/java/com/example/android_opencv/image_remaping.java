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
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.Arrays;

import Helper.BitmapHelper;

import static org.opencv.core.Core.BORDER_CONSTANT;
import static org.opencv.imgproc.Imgproc.INTER_LINEAR;

public class image_remaping extends AppCompatActivity {
    ImageView imgremaping;
    SeekBar seebarremaping;
    private Mat mapX = new Mat();
    private Mat mapY = new Mat();
    private Mat dst = new Mat();
    private int ind = 0;
    int k = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_image_remaping );
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("REMAPING");
        OpenCVLoader.initDebug ();
        seebarremaping = (SeekBar)findViewById(R.id.seebarremaping);
        imgremaping = (ImageView)findViewById ( R.id.imgremaping );
        imgremaping.setImageBitmap ( BitmapHelper.getInstance ().getBitmap () );
        for (int a= 0; a<=100; a++){
          if(k != 0) {
              seebarremaping.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
          k = k+1;
        }

    }

    private void convexBitmap(){
        Bitmap bitmap = Bitmap.createBitmap ( BitmapHelper.getInstance ().getBitmap () );
        Remaping(bitmap);
    }
    private void Remaping(Bitmap bitmap){
        bitmap = bitmap.copy ( Bitmap.Config.ARGB_8888,true );
        Mat src = new Mat (bitmap.getHeight (),bitmap.getWidth (),CvType.CV_8UC4);
        dst.create(src.size(),src.type());
        Utils.bitmapToMat ( bitmap,src );
        mapX = new Mat(src.size(), CvType.CV_32F);
        mapY = new Mat(src.size(), CvType.CV_32F);
        float buffX[] = new float[(int) (mapX.total() * mapX.channels())];
        mapX.get(0, 0, buffX);

        float buffY[] = new float[(int) (mapY.total() * mapY.channels())];
        mapY.get(0, 0, buffY);
        ind =(ind+1)%4;
        for (int i = 0; i < mapX.rows(); i++) {
            for (int j = 0; j < mapX.cols(); j++) {
                switch (ind) {
                    case 0:
                        if( j > mapX.cols()*0.25 && j < mapX.cols()*0.75 && i > mapX.rows()*0.25 && i < mapX.rows()*0.75 ) {
                            buffX[i*mapX.cols() + j] = 2*( j - mapX.cols()*0.25f ) + 0.5f;
                            buffY[i*mapY.cols() + j] = 2*( i - mapX.rows()*0.25f ) + 0.5f;
                        }
                        break;
                }
            }
            mapX.put(0, 0, buffX);
            mapY.put(0, 0, buffY);
            ind = k;
        }

            Imgproc.remap ( src,dst,mapX,mapY,INTER_LINEAR,BORDER_CONSTANT,new Scalar ( 0,0,0 ) );
            Bitmap img_Bitmap = Bitmap.createBitmap ( dst.cols (),dst.rows (),Bitmap.Config.ARGB_8888 );
            Utils.matToBitmap ( dst,img_Bitmap  );
            ImageView imageView = (ImageView)findViewById ( R.id.img_remapping2 );
            imageView.setImageBitmap ( img_Bitmap );









    }


}