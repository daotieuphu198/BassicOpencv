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
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import Helper.BitmapHelper;

public class img_hough extends AppCompatActivity {
    ImageView imghough;
    int g = 50;
    SeekBar seebarhough;
    Mat dst = new Mat(), cdst = new Mat(), cdstP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_img_hough );
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Hogh Transform");
        OpenCVLoader.initDebug ();
        seebarhough = (SeekBar)findViewById(R.id.seebarhough);
        imghough = (ImageView)findViewById ( R.id.imghough );
        imghough.setImageBitmap ( BitmapHelper.getInstance ().getBitmap () );
      for (int a = 0; a<=100; a++){
          if (g!=50){
              seebarhough.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
          g++;
      }
    }
    private void convexBitmap(){
        Bitmap bitmap = Bitmap.createBitmap ( BitmapHelper.getInstance ().getBitmap () );
       hough(bitmap);
    }
    private void hough(Bitmap bitmap){
        bitmap = bitmap.copy ( Bitmap.Config.ARGB_8888,true );
        Mat src = new Mat (bitmap.getHeight (),bitmap.getWidth (), CvType.CV_8UC4 );

        Utils.bitmapToMat ( bitmap,src );
        Imgproc.Canny(src, dst, 50, 200, 3, false);
        Imgproc.cvtColor(dst, cdst, Imgproc.COLOR_GRAY2BGR);
        cdstP = cdst.clone();
        Mat lines = new Mat();
        Imgproc.HoughLines(dst, lines, 1, Math.PI/180, 150);
        for (int x = 0; x < lines.rows(); x++) {
            double rho = lines.get(x, 0)[0],
                    theta = lines.get(x, 0)[1];
            double a = Math.cos(theta), b = Math.sin(theta);
            double x0 = a*rho, y0 = b*rho;
            Point pt1 = new Point(Math.round(x0 + 1000*(-b)), Math.round(y0 + 1000*(a)));
            Point pt2 = new Point(Math.round(x0 - 1000*(-b)), Math.round(y0 - 1000*(a)));
            Imgproc.line(cdst, pt1, pt2, new Scalar(0, 0, 255), 3, Imgproc.LINE_AA, 0);
        }
        Mat linesP = new Mat();
        Imgproc.HoughLinesP(dst, linesP, 1, Math.PI/180, g, g, 10);
        for (int x = 0; x < linesP.rows(); x++) {
            double[] l = linesP.get(x, 0);
            Imgproc.line(cdstP, new Point(l[0], l[1]), new Point(l[2], l[3]), new Scalar(0, 0, 255), 3, Imgproc.LINE_AA, 0);
        }

        Bitmap imgBitmap = Bitmap.createBitmap (cdstP.cols() ,cdstP.rows (),Bitmap.Config.ARGB_8888 );
        Utils.matToBitmap ( cdstP,imgBitmap );
        ImageView imageView = (ImageView)findViewById ( R.id.imghough2);
        imageView.setImageBitmap ( imgBitmap );
    }
}