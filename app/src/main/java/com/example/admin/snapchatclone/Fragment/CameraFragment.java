package com.example.admin.snapchatclone.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.admin.snapchatclone.FindUsersActivity;
import com.example.admin.snapchatclone.Helper.BitmapHelper;
import com.example.admin.snapchatclone.R;
import com.example.admin.snapchatclone.ShowCaptureActivity;
import com.example.admin.snapchatclone.Start.SplashScreenActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.List;

public class CameraFragment extends Fragment implements SurfaceHolder.Callback{

    final int CAMERA_REQUEST_CODE=1;


    SurfaceView mSurfaceView;
    SurfaceHolder mSurfaceHoler;
    ImageView image;
    Camera camera;
    Camera.PictureCallback jpegCallBack;


    public static CameraFragment newInstance(){
        CameraFragment cameraFragment=new CameraFragment();
        return cameraFragment;
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera,container,false);
        mSurfaceView=view.findViewById(R.id.surfaceView);
        mSurfaceHoler=mSurfaceView.getHolder();



        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA},CAMERA_REQUEST_CODE);
        }
        else {

            mSurfaceHoler.addCallback(this);
            mSurfaceHoler.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }


        Button mLogout=view.findViewById(R.id.logout);
        Button mCapture=view.findViewById(R.id.capture);
        Button mFindUsers=view.findViewById(R.id.findUsers);
        mFindUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findUsers();
            }
        });
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogOut();
            }
        });
        mCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });

        jpegCallBack=new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                Bitmap decodeBitmap= BitmapFactory.decodeByteArray(data,0,data.length);
                Bitmap rotateBitmap=rotate(decodeBitmap);
                BitmapHelper.getInstance().setBitmap(rotateBitmap);
                Intent intent=new Intent(getActivity(),ShowCaptureActivity.class);
                startActivity(intent);
                return;
            }
        };

        return view;
    }
    private Bitmap rotate(Bitmap decodeBitmap) {
        int w=decodeBitmap.getWidth();
        int h=decodeBitmap.getHeight();
        Matrix matrix=new Matrix();
        matrix.setRotate(90);
        return Bitmap.createBitmap(decodeBitmap,0,0,w,h,matrix,true);
    }
    private void findUsers() {
        Intent intent=new Intent(getContext(),FindUsersActivity.class);
        startActivity(intent);
        return;
    }


    private void captureImage() {
        camera.takePicture(null,null,jpegCallBack);
    }

    private void LogOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent=new Intent(getContext(),SplashScreenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        return;
    }



    public void surfaceCreated(SurfaceHolder holder) {
        camera=Camera.open();
        Camera.Parameters parameters;
        parameters= camera.getParameters();
        camera.setDisplayOrientation(90);
        parameters.setPreviewFrameRate(30);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);

        Camera.Size bestSize=null;
        List<Camera.Size> sizeList=camera.getParameters().getSupportedPreviewSizes();
        bestSize=sizeList.get(0);
        for(int i=1;i<sizeList.size();i++){
            if((sizeList.get(i).width*sizeList.get(i).height)>(bestSize.width*bestSize.height)){
                bestSize=sizeList.get(i);
            }
        }
        camera.setParameters(parameters);
        try {
            camera.setPreviewDisplay(mSurfaceHoler);
        } catch (IOException e) {
            e.printStackTrace();
        }
        parameters.setPreviewSize(bestSize.width,bestSize.height);
        camera.startPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case CAMERA_REQUEST_CODE:
            {
                if (grantResults.length>0&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    mSurfaceHoler.addCallback(this);
                    mSurfaceHoler.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                }
                else {
                    Toast.makeText(getContext(), "Please provide the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
}
