package com.renomad.capsaicin;

import android.annotation.TargetApi;
import android.app.Activity;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.view.SurfaceHolder;
import java.io.IOException;
import java.util.List;

public class RecordVideoActivity extends Activity {

    private Camera mCamera;
    private SurfaceView mPreview;
    private MediaRecorder mMediaRecorder;
    private String mMediaFilename;
    private CamcorderProfile mProfile;

    private boolean isRecording = false;
    private static final String TAG = "RecordVideoActivity";
    private Button captureButton;
    private Button sendButton;

    public void onAttachedToWindow() {
        Log.i(TAG, "we attached to the window");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_video);

        mPreview = (SurfaceView) findViewById(R.id.preview);
        captureButton = (Button) findViewById(R.id.button_capture);
        sendButton = (Button) findViewById(R.id.button_send);
        mCamera = getCameraInstance();
        configure_preview(mCamera);
    }
    
    /** A safe way to get an instance of the Camera object. */
    private Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            DialogHelper.showGenericDialog("Camera not available", this);
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    } 

    public void onSendClick(View view) {
        Log.i(TAG, "setting result to OK");
        Intent intent = new Intent();
        intent.putExtra("com.renomad.capsaicin.fileuri", mMediaFilename);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * The capture button controls all user interaction. When recording, the button click
     * stops recording, releases {@link android.media.MediaRecorder} and {@link android.hardware.Camera}. When not recording,
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

    @Override
    protected void onPause() {
        super.onPause();
        // if we are using MediaRecorder, release it first
        releaseMediaRecorder();
        // release the camera immediately on pause event
        releaseCamera();
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

    private void releaseCamera(){
        if (mCamera != null){
            // release the camera for other applications
            mCamera.release();
            mCamera = null;
        }
    }

    
    private void configure_preview(Camera mCamera) {
        new CameraPreview(this, mCamera);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private boolean prepareVideoRecorder(){

        // BEGIN_INCLUDE (configure_media_recorder)
        mMediaRecorder = new MediaRecorder();

        // Step 1: Unlock and set camera to MediaRecorder
        Log.i(TAG, "unlocking and setting camera to MediaRecorder");
        mCamera.unlock();
        Log.i(TAG, String.format("assigning mCamera %s to mMediaRecorder %s", mCamera.toString(), mMediaRecorder.toString()));
        mMediaRecorder.setCamera(mCamera);

        // Step 2: Set sources
        Log.i(TAG, "setting sources...");
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT );
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
        Log.i(TAG, "setting a CamcorderProfile");
        mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));

        // Step 4: Set output file
        Log.i(TAG, "setting an output file...");
        mMediaFilename = CameraHelper.getOutputMediaFile(getExternalFilesDir("videos")).toString();
        mMediaRecorder.setOutputFile(mMediaFilename);
        Log.i(TAG, String.format("output file is %s", mMediaFilename));
        // END_INCLUDE (configure_media_recorder)

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
     * Asynchronous task for preparing the {@link android.media.MediaRecorder} since it's a long blocking
     * operation.
     */
    class MediaPrepareTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            // initialize video camera
            if (prepareVideoRecorder()) {
                Log.i("MediaPrepareTask", "camera is available and unlocked, MediaRecorder is prepared");
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
