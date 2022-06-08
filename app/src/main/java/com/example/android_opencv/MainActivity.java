package com.example.android_opencv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;

public class   MainActivity extends AppCompatActivity {
    private Button btn_img1,btn_img2, btn_img3, btn_img4 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OpenCVLoader.initDebug ();
        btn_img1 = (Button)findViewById(R.id.bnt_img1);
        btn_img2 = (Button)findViewById(R.id.btn_img2);
        btn_img3 = (Button)findViewById(R.id.btn_img3);
        btn_img4 = (Button)findViewById(R.id.btn_img4);
        //tạo sự kiện onclick tro btn_img1
        btn_img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(MainActivity.this, Image_preprocessing.class);
                startActivity(intent);
            }
        });
        btn_img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(MainActivity.this, morphologycal.class);
                startActivity(intent);
            }
        });
        btn_img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(MainActivity.this, border_delection.class);
                startActivity(intent);
            }
        });
        btn_img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(MainActivity.this, Image_segmentation.class);
                startActivity(intent);
            }
        });
    }

}