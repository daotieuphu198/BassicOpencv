package com.example.android_opencv;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.util.List;

import Helper.BitmapHelper;
import gun0912.tedbottompicker.TedBottomPicker;

public class morphologycal extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private Button btn_hinhM,btn_erode,btn_dilate,btn_morphology,btn_camera;
    private ImageView img2;
    private int i = 0;
    private Mat mRGBA, mRGBAT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_morphologycal );
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Morphological Transformaations");



        btn_camera = (Button)findViewById(R.id.btn_camera);
        btn_hinhM = (Button)findViewById(R.id.btn_hinhM);
        img2 = (ImageView)findViewById(R.id.img2);
        btn_erode = (Button)findViewById(R.id.btn_erode);
        btn_dilate = (Button)findViewById(R.id.btn_dilate);
        btn_morphology = (Button)findViewById(R.id.btn_morphology);
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JavaCameraView javaCameraView = findViewById(R.id.cameraView);
                i = i+1;
                img2 = null;
                javaCameraView.setVisibility(SurfaceView.VISIBLE);
                javaCameraView.setCvCameraViewListener(javaCameraView.enableView());
            }
        });

        btn_erode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img2 == null && i!=0) {
                    Intent intent = new Intent();
                    intent.setClass(morphologycal.this, camera_erode.class);
                    startActivity(intent);
                    finish();

                } else  if (i ==0 && img2 != null){
                    Intent intent = new Intent();
                    intent.setClass(morphologycal.this, img_erode.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(morphologycal.this, "asked to choose an image or camera", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_dilate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img2 == null && i!=0) {
                    Intent intent = new Intent();
                    intent.setClass(morphologycal.this, camera_dilate.class);
                    startActivity(intent);
                    finish();

                } else  if (i ==0 && img2 != null){
                    Intent intent = new Intent();
                    intent.setClass(morphologycal.this, img_dialate.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(morphologycal.this, "asked to choose an image or camera", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_morphology.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img2 == null && i != 0) {
                    Intent intent = new Intent();
                    intent.setClass(morphologycal.this, camera_morphology.class);
                    startActivity(intent);
                    finish();

                } else if (i == 0 && img2 != null) {
                    Intent intent = new Intent();
                    intent.setClass(morphologycal.this, img_morphology.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(morphologycal.this, "asked to choose an image or camera", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btn_hinhM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //goiham su ly thu vien khi su dung thao tac Onclick
                i=0;
                requestPermission();
            }
        });

    }
    private void requestPermission(){
        PermissionListener permissionlistener = new PermissionListener() {
            //loi canh bao
            @Override
            public void onPermissionGranted() {
                Toast.makeText(morphologycal.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                openImagePicket();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(morphologycal.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }


        };
        //su dung thu vien camera va read external strorage
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions( Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }
    private void openImagePicket(){
        TedBottomPicker.OnImageSelectedListener listener = new TedBottomPicker.OnImageSelectedListener() {
            @Override
            public void onImageSelected(Uri uri) {
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                    img2.setImageBitmap(bitmap);
                    BitmapHelper.getInstance ().setBitmap ( bitmap );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(morphologycal.this)
                .setOnImageSelectedListener(listener)
                .create();
        tedBottomPicker.show(getSupportFragmentManager());
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRGBA = new Mat(height, width, CvType.CV_8UC4);
    }

    @Override
    public void onCameraViewStopped() {
        mRGBA.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRGBA = inputFrame.rgba();
        mRGBAT = mRGBA.t();
        Core.flip(mRGBA.t(),mRGBAT,1);
        Imgproc.resize(mRGBAT, mRGBAT, mRGBA.size());

        return mRGBAT;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}