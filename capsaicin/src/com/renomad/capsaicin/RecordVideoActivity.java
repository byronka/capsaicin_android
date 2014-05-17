package com.renomad.capsaicin;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import java.io.IOException;
import java.util.List;
import java.util.Date;
import java.io.File;
import java.text.SimpleDateFormat;
import com.renomad.capsaicin.CameraPreview;

public class RecordVideoActivity extends Activity {

    public static final int MEDIA_TYPE_VIDEO = 2;
    private CameraPreview mPreview;
    private Camera mCamera;
    private MediaRecorder mMediaRecorder;
    private int numberOfCameras;
    private int cameraCurrentlyLocked;

    private String mMediaFilename;
    private boolean isRecording = false;

    private static final String TAG = "RecordVideoActivity";
    private Button captureButton;
    private Button sendButton;

    // The first rear facing camera
    int defaultCameraId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the window title.
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        mPreview = new CameraPreview(this);
        setContentView(R.layout.activity_record_video);
        ((FrameLayout)findViewById(R.id.previewplaceholder)).addView(mPreview);

        // wire up the buttons
        captureButton = (Button) findViewById(R.id.button_capture);
        sendButton = (Button) findViewById(R.id.button_send);

        // Find the total number of cameras available
        numberOfCameras = Camera.getNumberOfCameras();

        // Find the ID of the default camera
        CameraInfo cameraInfo = new CameraInfo();
            for (int i = 0; i < numberOfCameras; i++) {
                Camera.getCameraInfo(i, cameraInfo);
                if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
                    defaultCameraId = i;
                }
            }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Open the default i.e. the first rear facing camera.
        mCamera = Camera.open();
        cameraCurrentlyLocked = defaultCameraId;
        mPreview.setCamera(mCamera);
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaRecorder();
        // Because the Camera object is a shared resource, it's very
        // important to release it when the activity is paused.
        releaseCamera();
    }   

    private void releaseCamera(){
        if (mCamera != null){
            // release the camera for other applications
            mPreview.setCamera(null);
            mCamera.release();
            mCamera = null;
        }
    }


    //@Override
    //public boolean onCreateOptionsMenu(Menu menu) {

    //    // Inflate our menu which can gather user input for switching camera
    //    MenuInflater inflater = getMenuInflater();
    //    inflater.inflate(R.menu.camera_menu, menu);
    //    return true;
    //}

    //public boolean onOptionsItemSelected(MenuItem item) {
    //    // Handle item selection
    //    switch (item.getItemId()) {
    //    case R.id.switch_cam:
    //        // check for availability of multiple cameras
    //        if (numberOfCameras == 1) {
    //            AlertDialog.Builder builder = new AlertDialog.Builder(this);
    //            builder.setMessage(this.getString(R.string.camera_alert))
    //                   .setNeutralButton("Close", null);
    //            AlertDialog alert = builder.create();
    //            alert.show();
    //            return true;
    //        }

    //        // OK, we have multiple cameras.
    //        // Release this camera -> cameraCurrentlyLocked
    //        if (mCamera != null) {
    //            mCamera.stopPreview();
    //            mPreview.setCamera(null);
    //            mCamera.release();
    //            mCamera = null;
    //        }

    //        // Acquire the next camera and request Preview to reconfigure
    //        // parameters.
    //        mCamera = Camera
    //                .open((cameraCurrentlyLocked + 1) % numberOfCameras);
    //        cameraCurrentlyLocked = (cameraCurrentlyLocked + 1)
    //                % numberOfCameras;
    //        mPreview.switchCamera(mCamera);

    //        // Start the preview
    //        mCamera.startPreview();
    //        return true;
    //    default:
    //        return super.onOptionsItemSelected(item);
    //    }
    //}
    
    //The following section is from the old file
    //OLD SECTION BEGINS
    //OLD SECTION BEGINS
    //OLD SECTION BEGINS

    public void onSendClick(View view) {
        Log.i(TAG, "setting result to OK");
        Intent intent = new Intent();
        intent.putExtra("com.renomad.capsaicin.fileuri", mMediaFilename);
        setResult(RESULT_OK, intent);
        finish();
    }
    
    /**
     * The capture button controls all user interaction. When recording, the button click
     * stops recording, releases {@link android.media.MediaRecorder} 
     * and {@link android.hardware.Camera}. When not recording,
     * it prepares the {@link android.media.MediaRecorder} and starts recording.
     *
     * @param view the view generating the event.
     */
    public void onCaptureClick(View view) {
        if (isRecording) {
            // stop recording and release camera
            mMediaRecorder.stop();  // stop the recording
            releaseMediaRecorder(); // release the MediaRecorder object
            mCamera.lock();         // take camera access back from MediaRecorder
            // inform the user that recording has stopped
            setCaptureButtonText("Capture");
            isRecording = false;
            releaseCamera();
        } else {
            new MediaPrepareTask().execute(null, null, null);
        }
    }

    private void setCaptureButtonText(String title) {
        captureButton.setText(title);
    }

    private void releaseMediaRecorder(){
        if (mMediaRecorder != null) {
            // clear recorder configuration
            mMediaRecorder.reset();
            // release the recorder object
            mMediaRecorder.release();
            mMediaRecorder = null;
            // Lock camera for later use i.e taking it back from MediaRecorder.
            // MediaRecorder doesn't need it anymore and we will release it if the activity pauses.
            mCamera.lock();
        }
    }
    
    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type){
          return Uri.fromFile(getOutputMediaFile(type));
    }
    
    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                                                        Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_"+ timeStamp + ".mp4");

        return mediaFile;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private boolean prepareVideoRecorder(){

        // BEGIN_INCLUDE (configure_media_recorder)
        mMediaRecorder = new MediaRecorder();

        // Step 1: Unlock and set camera to MediaRecorder
        Log.i(TAG, "unlocking and setting camera to MediaRecorder");
        mCamera.unlock();
        Log.i(TAG, String.format("assigning mCamera %s to mMediaRecorder %s",
                                 mCamera.toString(), mMediaRecorder.toString()));
        mMediaRecorder.setCamera(mCamera);

        // Step 2: Set sources
        Log.i(TAG, "setting sources...");
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER );
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
        Log.i(TAG, "setting a CamcorderProfile");
        mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));

        // Step 4: Set output file
        Log.i(TAG, "setting an output file...");
        //mMediaFilename = CameraHelper.getOutputMediaFile(getExternalFilesDir("videos")).toString();
        mMediaFilename = getOutputMediaFile(MEDIA_TYPE_VIDEO).toString();
        mMediaRecorder.setOutputFile(mMediaFilename);
        
        Log.i(TAG, String.format("output file is %s", mMediaFilename));

        // Step 4.5: set preview display for camcorder:
        Log.i(TAG, "setting preview display for camcorder");
        mMediaRecorder.setPreviewDisplay(mPreview.getSurface());

        // Step 5: Prepare configured MediaRecorder
        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            Log.d(TAG, "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        }
        return true;
    }

    /**
     * Asynchronous task for preparing the {@link android.media.MediaRecorder} 
     * since it's a long blocking
     * operation.
     */
    class MediaPrepareTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            // initialize video camera
            if (prepareVideoRecorder()) {
                Log.i("MediaPrepareTask", 
                      "camera is available and unlocked, MediaRecorder is prepared");
                // Camera is available and unlocked, MediaRecorder is prepared,
                // now you can start recording
                mMediaRecorder.start();

                isRecording = true;
            } else {
                Log.i("MediaPrepareTask", "prepare didn't work, release the camera");
                // prepare didn't work, release the camera
                releaseMediaRecorder();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (!result) {
                RecordVideoActivity.this.finish();
            }
            // inform the user that recording has started
            setCaptureButtonText("Stop");

        }
    }
}

