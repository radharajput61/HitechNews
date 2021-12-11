package com.app.hitechnews.Activity.Post;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.app.hitechnews.MainActivity;
import com.app.hitechnews.R;
import com.app.hitechnews.Util.PathUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class RecordVoiceActivity extends AppCompatActivity {

    ImageView ivPlayPause,ivComplete;
    VideoView videoView;
    SeekBar seekBar;
    TextView current, total;
    LinearLayout showProgress,relativeLayout;
    double current_pos, total_duration;

    public static final int RequestPermissionCode = 1;
    String newaudiopath= "";
    private static String fileName = null;
    Random random ;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};
    private static String root = Environment.getExternalStorageDirectory().toString();
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;



    private static final String AUDIO_RECORDER_FILE_EXT_3GP = ".3gp";
    private static final String AUDIO_RECORDER_FILE_EXT_MP4 = ".mp4";
    private MediaRecorder recorder = null;
    private int currentFormat = 0;
    private int output_formats[] = { MediaRecorder.OutputFormat.MPEG_4,             MediaRecorder.OutputFormat.THREE_GPP };
    private String file_exts[] = { AUDIO_RECORDER_FILE_EXT_MP4, AUDIO_RECORDER_FILE_EXT_3GP };
    String filepath;
    public static final String EXTRA_RECORDED_FILE = "com.example.android.wordlistsql.REPLY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_voice);
        setVideo();
        setValue();
        requestPermission();
    }


    public void setVideo() {
        videoView = (VideoView) findViewById(R.id.videoview);
        ivPlayPause=findViewById(R.id.ivPlayPause);
        new SimpleTooltip.Builder(this)
                .anchorView(ivPlayPause)
                .text("Press and Hold to Record Your Video")
                .gravity(Gravity.TOP)
                .build()
                .show();
        ivComplete=findViewById(R.id.ivComplete);
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        current = (TextView) findViewById(R.id.current);
        total = (TextView) findViewById(R.id.total);
        showProgress = (LinearLayout) findViewById(R.id.showProgress);
        relativeLayout = (LinearLayout) findViewById(R.id.relative);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                setVideoProgress();
            }
        });
        ivComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File originFile = new File(fileName);
                moveCacheFile(RecordVoiceActivity.this,originFile,newaudiopath);
                Log.v("sdkjfh","1=-"+fileName+", current-"+current_pos);
                Log.v("ksdksdfg",""+newaudiopath);
                Intent returnIntent = new Intent();
                returnIntent.putExtra(EXTRA_RECORDED_FILE,fileName);
                returnIntent.putExtra("duration",""+current_pos);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });
        playVideo();
        setPause();
    }
    public static boolean moveCacheFile(Context context, File cacheFile, String internalStorageName) {

        boolean ret = false;
        FileInputStream fis = null;
        FileOutputStream fos = null;

        try {
            fis = new FileInputStream(cacheFile);

            ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
            byte[] buffer = new byte[1024];
            int read = -1;
            while( (read = fis.read(buffer) ) != -1 ) {
                baos.write(buffer, 0, read);
            }
            baos.close();
            fis.close();

            fos = context.openFileOutput(internalStorageName, Context.MODE_PRIVATE);
            baos.writeTo(fos);
            fos.close();

            // delete cache
            cacheFile.delete();

            ret = true;
        }
        catch(Exception e) {
            //Log.e(TAG, "Error saving previous rates!");
        }
        finally {
            try { if ( fis != null ) fis.close(); } catch (IOException e) { }
            try { if ( fos != null ) fos.close(); } catch (IOException e) { }
        }

        return ret;
    }

    private void setValue() {
        newaudiopath = Environment.getExternalStorageDirectory() + "/Android/data/record"  ;
        fileName = getExternalCacheDir().getAbsolutePath();

        fileName += "/audio.mp3";

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        random = new Random();

        root = Environment.getExternalStorageDirectory().toString();

    }

    private void startRecording(){
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(output_formats[currentFormat]);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(fileName);
        recorder.setOnErrorListener(errorListener);
        recorder.setOnInfoListener(infoListener);

        try {
            recorder.prepare();
            recorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private MediaRecorder.OnErrorListener errorListener = new MediaRecorder.OnErrorListener() {
        @Override
        public void onError(MediaRecorder mr, int what, int extra) {
            Log.v("Error:", ""+ what + ", " + extra);
        }
    };

    private MediaRecorder.OnInfoListener infoListener = new MediaRecorder.OnInfoListener() {
        @Override
        public void onInfo(MediaRecorder mr, int what, int extra) {
            Log.v("Warning:", ""+ what + ", " + extra);
        }
    };

    private void stopRecording(){
        if(null != recorder){
            if(current_pos>3000) {
                recorder.stop();
                recorder.reset();
                recorder.release();
                recorder = null;
            }else{
                ivComplete.setVisibility(View.INVISIBLE);
                new SimpleTooltip.Builder(this)
                        .anchorView(ivPlayPause)
                        .text("Record Your Voice Minimum 3 Second")
                        .gravity(Gravity.TOP)
                        .build()
                        .show();
            }
        }
    }

    public void playVideo() {
        try  {
            String firstVideoPath = PathUtil.getRealPath(this, MainActivity.videoUri);
            videoView.setVideoURI(Uri.parse(firstVideoPath));
            ivComplete.setVisibility(View.INVISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setPause() {
        ivPlayPause.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // Pressed
                    videoView.start();
                    ivPlayPause.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
                    ivComplete.setVisibility(View.INVISIBLE);
                    startRecording();
                    Toast.makeText(RecordVoiceActivity.this, "Pressed", Toast.LENGTH_SHORT).show();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    // Released
                    videoView.pause();
                    ivPlayPause.setImageResource(R.drawable.ic_baseline_play_circle_filled_241);
                    ivComplete.setVisibility(View.VISIBLE);
                    stopRecording();
                    Toast.makeText(RecordVoiceActivity.this, "Released", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }
    public void setVideoProgress() {
        //get the video duration
        current_pos = videoView.getCurrentPosition();
        total_duration = videoView.getDuration();

        //display video duration
        total.setText(timeConversion((long) total_duration));
        current.setText(timeConversion((long) current_pos));
        seekBar.setMax((int) total_duration);
        final Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    current_pos = videoView.getCurrentPosition();
                    current.setText(timeConversion((long) current_pos));
                    seekBar.setProgress((int) current_pos);
                    handler.postDelayed(this, 1000);
                } catch (IllegalStateException ed){
                    ed.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 1000);

        //seekbar change listner
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                current_pos = seekBar.getProgress();
                videoView.seekTo((int) current_pos);
            }
        });
    }
    public String timeConversion(long value) {
        String songTime;
        int dur = (int) value;
        int hrs = (dur / 3600000);
        int mns = (dur / 60000) % 60000;
        int scs = dur % 60000 / 1000;

        if (hrs > 0) {
            songTime = String.format("%02d:%02d:%02d", hrs, mns, scs);
        } else {
            songTime = String.format("%02d:%02d", mns, scs);
        }
        return songTime;
    }


    private void requestPermission() {
        ActivityCompat.requestPermissions(RecordVoiceActivity.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(RecordVoiceActivity.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(RecordVoiceActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }
}