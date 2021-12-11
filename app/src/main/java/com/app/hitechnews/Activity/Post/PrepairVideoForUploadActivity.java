package com.app.hitechnews.Activity.Post;

import static android.os.Environment.getExternalStoragePublicDirectory;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.hitechnews.MainActivity;
import com.app.hitechnews.Model.Input;
import com.app.hitechnews.Model.UploadResponse;
import com.app.hitechnews.R;
import com.app.hitechnews.Util.API;
import com.app.hitechnews.Util.ApiClient;
import com.app.hitechnews.Util.EndPointInterface;
import com.app.hitechnews.Util.FileUploader;
import com.app.hitechnews.Util.MyPreferences;
import com.app.hitechnews.Util.PathUtil;
import com.app.hitechnews.Util.UtilVideo;
import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.ExecuteCallback;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.vincent.videocompressor.VideoCompress;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;
import retrofit2.Call;
import retrofit2.Callback;

import static com.app.hitechnews.Util.ClsGlobal.CreateProgressDialog;
import static com.app.hitechnews.Util.ClsGlobal.hideProgress;
import static com.app.hitechnews.Util.ClsGlobal.showProgress;
import static com.app.hitechnews.Util.ClsGlobal.updateProgress;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;

public class PrepairVideoForUploadActivity extends AppCompatActivity {

    VideoView videoView;
    ImageView pause;
    SeekBar seekBar;
    int video_index = 0;
    MyPreferences myPreferences;
    double current_pos, total_duration;
    TextView current, total,tvSubmit,tvVoice,tvVoiceMsg,tvLocation,tvLocationMsg,tvBackgroundMsg,tvBackground;
    LinearLayout showProgress,llMainLoader;
    LinearLayout relativeLayout,llVoice,llBackground,llLocation,llBackgroundOuter;
    private static final int MY_AUDIO_VOICE_REQUEST_CODE = 222;
    private Activity activity;
    private List<Input> inputs = new ArrayList<>();
    String audioPath;
    String firstVideoPath;
    String secondVideoPath;
    String videoPath1;
    String videoPath2;
    String backgroundMusicPath;
    String audioPath2;
    Uri videoPlayPath;
    public static final String TAG = PrepairVideoForUploadActivity.class.getSimpleName();
    String UserId,filePath,strCity="Lucknow",strLocation="Uttar Pradesh",videoLinkBase64="",videoLinkFinal="";
    public static final int NEW_LOCATION_ACTIVITY_REQUEST_CODE = 4;
    public static final int NEW_BACKGROUND_ACTIVITY_REQUEST_CODE = 5;
    public static final int NEW_RECORDVOICE_ACTIVITY_REQUEST_CODE = 61;
    SpinKitView skLoader;
    EditText etTitle,etDesc;
    String mDuration,prepairBackground,videoCurrentPlayPath,Thumbnail="";
    Random r = new Random();
    boolean isVideoProper=false;
    private TextView  tv_progress;
    private long startTime, endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepair_video_for_upload);
        videoPlayPath=MainActivity.videoUri;
        Log.v("kjsdhj",""+videoPlayPath);
        activity = this;
        tv_progress = (TextView) findViewById(R.id.tv_progress);
        myPreferences=new MyPreferences(this);
        UserId=myPreferences.getUserId();
        skLoader=findViewById(R.id.skLoader);
        etTitle=findViewById(R.id.etTitle);
        etDesc=findViewById(R.id.etDesc);
        tvVoice=findViewById(R.id.tvVoice);
        tvVoiceMsg=findViewById(R.id.tvVoiceMsg);
        llBackgroundOuter=findViewById(R.id.llBackgroundOuter);
        llMainLoader=findViewById(R.id.llMainLoader);
        tvLocation=findViewById(R.id.tvLocation);
        tvLocation.setText(strCity+" | "+strLocation);
        tvLocationMsg=findViewById(R.id.tvLocationMsg);
        llLocation=findViewById(R.id.llLocation);
        tvBackgroundMsg=findViewById(R.id.tvBackgroundMsg);
        llLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrepairVideoForUploadActivity.this, SelectLocationActivity.class);
                startActivityForResult(intent, NEW_LOCATION_ACTIVITY_REQUEST_CODE);
            }
        });

        llVoice=findViewById(R.id.llVoice);
        llVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isVideoProper)
                {
                    showchooser();
                }else{
                    new SimpleTooltip.Builder(PrepairVideoForUploadActivity.this)
                            .anchorView(seekBar)
                            .text("Video size minimum 3 second to continue")
                            .gravity(Gravity.BOTTOM)
                            .build()
                            .show();
                }

            }
        });
        llBackground=findViewById(R.id.llBackground);
        tvBackground=findViewById(R.id.tvBackground);
        llBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AddNewVoice();
                if(isVideoProper)
                {
                    Intent intent = new Intent(PrepairVideoForUploadActivity.this, BackgroundMusicActivity.class);
                    startActivityForResult(intent, NEW_BACKGROUND_ACTIVITY_REQUEST_CODE);
                }else{
                    new SimpleTooltip.Builder(PrepairVideoForUploadActivity.this)
                            .anchorView(seekBar)
                            .text("Video size minimum 3 second to continue")
                            .gravity(Gravity.BOTTOM)
                            .build()
                            .show();
                }
            }
        });
        tvSubmit=findViewById(R.id.tvSubmit);
        new SimpleTooltip.Builder(this)
                .anchorView(tvSubmit)
                .text("Post Your Video After Doing All Work")
                .gravity(Gravity.TOP)
                .build()
                .show();
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isVideoProper)
                {
                    new SimpleTooltip.Builder(PrepairVideoForUploadActivity.this)
                            .anchorView(seekBar)
                            .text("Video size minimum 3 second to continue")
                            .gravity(Gravity.BOTTOM)
                            .build()
                            .show();
                }
                else if(strCity.equalsIgnoreCase("") || strLocation.equalsIgnoreCase(""))
                {
                    Toast.makeText(activity, "Please Select City and Location", Toast.LENGTH_SHORT).show();
                    llLocation.setBackgroundColor(getResources().getColor(R.color.danger));
                }
//                else if(videoLinkBase64.equalsIgnoreCase(""))
//                {
//                    Toast.makeText(activity, "Error In Video", Toast.LENGTH_SHORT).show();
//                }
                else if(etTitle.getText().toString().isEmpty())
                {
                    Toast.makeText(activity, "Please Enter Title", Toast.LENGTH_SHORT).show();
                }
//                else if(etDesc.getText().toString().isEmpty())
//                {
//                    Toast.makeText(activity, "Please Enter Description", Toast.LENGTH_SHORT).show();
//                }
                else
                {
                    skLoader.setVisibility(View.VISIBLE);
                    tv_progress.setVisibility(View.VISIBLE);
                    tvSubmit.setVisibility(View.GONE);
                    tvSubmit.setEnabled(false);

//               submitUpload();

                    CompressVideoForUploading();


                }
            }
        });
        setVideo();
    }


    private void submitUpload() {

        if (videoLinkFinal != null
                && !videoLinkFinal.equalsIgnoreCase("")){
            CreateProgressDialog(PrepairVideoForUploadActivity.this);
            showProgress("Video Is Uploading ...");
            FileUploader fileUploader = new FileUploader(new File(videoLinkFinal),PrepairVideoForUploadActivity.this);
            fileUploader.SetCallBack(new FileUploader.FileUploaderCallback() {
                @Override
                public void onError(String msg) {
                    hideProgress();
                    Toast.makeText(PrepairVideoForUploadActivity.this,"Please Check Your Internat Connection or Data",Toast.LENGTH_LONG).show();
//                    Toast.makeText(PrepairVideoForUploadActivity.this,"Error !! "+ msg,Toast.LENGTH_SHORT).show();
                    skLoader.setVisibility(View.GONE);
                    tv_progress.setVisibility(View.GONE);
                    tvSubmit.setVisibility(View.VISIBLE);
                    tvSubmit.setEnabled(true);
                }

                @Override
                public void onFinish(String responses) {
//                    Toast.makeText(PrepairVideoForUploadActivity.this,"Success !! "+responses,Toast.LENGTH_SHORT).show();
                    Log.v("jfgjkh",""+responses);
                    setUploadMyPostData(UserId,strCity,strLocation,etTitle.getText().toString().trim(),
                            etDesc.getText().toString().trim(),"News",responses);
                    hideProgress();

                    videoLinkFinal = "";
                }

                @Override
                public void onProgressUpdate(int currentpercent, int totalpercent,String msg) {
                    skLoader.setVisibility(View.GONE);
                    tv_progress.setVisibility(View.GONE);
                    tvSubmit.setVisibility(View.VISIBLE);
                    tvSubmit.setEnabled(true);
                    updateProgress(totalpercent,"Uploading ",msg);
                }
            });


          //  txt_file_Name.setText("File Path");
        }else {
            Toast.makeText(PrepairVideoForUploadActivity.this,"Select File",Toast.LENGTH_SHORT).show();
        }

    }




    public void showchooser() {
        final BottomSheetDialog bottomSheet = new BottomSheetDialog(PrepairVideoForUploadActivity.this);
        View mView = LayoutInflater.from(PrepairVideoForUploadActivity.this).inflate(R.layout.voice_layout_selection, null);
        bottomSheet.setContentView(mView);
        ImageView ivVoice = mView.findViewById(R.id.ivVoice);
        ImageView ivGallery = mView.findViewById(R.id.ivGallery);
        ImageView tvCancel = mView.findViewById(R.id.tvCancel);
        ivGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewVoice();
                bottomSheet.cancel();
            }
        });
        ivVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrepairVideoForUploadActivity.this, RecordVoiceActivity.class);
                startActivityForResult(intent, NEW_RECORDVOICE_ACTIVITY_REQUEST_CODE);
                bottomSheet.cancel();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheet.cancel();
            }
        });
        bottomSheet.show();
        bottomSheet.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog dialogc = (BottomSheetDialog) dialog;
            }
        });
    }

    public void setVideo() {
        videoView = (VideoView) findViewById(R.id.videoview);
        pause = (ImageView) findViewById(R.id.pause);
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        current = (TextView) findViewById(R.id.current);
        total = (TextView) findViewById(R.id.total);
        showProgress = (LinearLayout) findViewById(R.id.showProgress);
        relativeLayout = (LinearLayout) findViewById(R.id.relative);
        video_index = getIntent().getIntExtra("pos" , 0);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                setVideoProgress();
            }
        });
        playVideo(video_index);
        setPause();
    }

    // play video file
    public void playVideo(int pos) {

        try  {
            firstVideoPath = PathUtil.getRealPath(this, MainActivity.videoUri);
            Bitmap bmThumbnail;
            bmThumbnail = ThumbnailUtils.createVideoThumbnail(firstVideoPath,
                    MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);

            Bitmap bmThumbnail_resized = Bitmap.createScaledBitmap(bmThumbnail,1920, 1080, true);
            Thumbnail= getEncoded64ImageStringFromBitmap(bmThumbnail_resized);
            Log.v("kjsgdjgh",""+Thumbnail);
           // videoLinkBase64  = encodeFileToBase64Binary(firstVideoPath);//
           // Log.v("videoBase64",videoLinkBase64);
            String firstVideoPath = PathUtil.getRealPath(this, videoPlayPath);
            Log.v("sdkfjhf",""+firstVideoPath);
            videoLinkFinal=firstVideoPath;
            videoView.setVideoURI(Uri.parse(firstVideoPath));
            videoView.start();
            pause.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
            video_index=pos;
            executeExtractAudioCommand(firstVideoPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        Log.v("agdsh", "5");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
        return imgString;
    }
    // display video progress
    public void setVideoProgress() {
        //get the video duration
        current_pos = videoView.getCurrentPosition();
        total_duration = videoView.getDuration();
        Log.v("assdds",""+total_duration);
        if(total_duration<3000)
        {
            isVideoProper=false;
            new SimpleTooltip.Builder(this)
                    .anchorView(seekBar)
                    .text("Video size minimum 3 second to continue")
                    .gravity(Gravity.BOTTOM)
                    .build()
                    .show();
        }else{
            isVideoProper=true;
        }
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

    public void setPause() {
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()) {
                    videoView.pause();
                    pause.setImageResource(R.drawable.ic_baseline_play_circle_filled_241);
                } else {
                    videoView.start();
                    pause.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
                }
            }
        });
    }

    //time conversion
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

    public void AddNewVoice(){
        Intent intent = new Intent();
        intent.setType("audio/mpeg");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, MY_AUDIO_VOICE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(requestCode == MY_AUDIO_VOICE_REQUEST_CODE && resultCode == RESULT_OK){
            try{
                llMainLoader.setVisibility(View.VISIBLE);
                llBackgroundOuter.setVisibility(View.VISIBLE);
                executeCutAudioCommand(0, (int) total_duration,intent.getData());

            }catch (Exception o){
                Toast.makeText(activity, "Input not added.", Toast.LENGTH_SHORT).show();
            }
        }else if (requestCode == NEW_RECORDVOICE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Log.v("asduiud",""+intent.getStringExtra(RecordVoiceActivity.EXTRA_RECORDED_FILE));
            mDuration=intent.getStringExtra("duration");
            Log.v("asduiud",""+mDuration);
            String cutDuration=String.format("%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours((long)Double.parseDouble(mDuration)),
                    TimeUnit.MILLISECONDS.toMinutes((long)Double.parseDouble(mDuration)),
                    TimeUnit.MILLISECONDS.toSeconds((long)Double.parseDouble(mDuration)) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) current_pos))
            );
            Log.v("sdsdaad",""+cutDuration);
            llMainLoader.setVisibility(View.VISIBLE);
            llBackgroundOuter.setVisibility(View.VISIBLE);
            audioPath=intent.getStringExtra(RecordVoiceActivity.EXTRA_RECORDED_FILE);
            executeCutVideoCommand("00:00:00",cutDuration,MainActivity.videoUri);
//            PrepairVideoForMixing();
        }else if (requestCode == NEW_LOCATION_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Log.v("kdjfh",""+intent.getStringExtra(SelectLocationActivity.EXTRA_CITY)+","+intent.getStringExtra(SelectLocationActivity.EXTRA_ADDRESS));
            strCity=intent.getStringExtra(SelectLocationActivity.EXTRA_CITY);
            strLocation=intent.getStringExtra(SelectLocationActivity.EXTRA_ADDRESS);
            tvLocation.setText(intent.getStringExtra(SelectLocationActivity.EXTRA_CITY)+" | "+intent.getStringExtra(SelectLocationActivity.EXTRA_ADDRESS));
            tvLocationMsg.setVisibility(View.VISIBLE);
        }else if (requestCode == NEW_BACKGROUND_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            try {
                if (intent != null && intent.hasExtra("audio")) {
                    backgroundMusicPath = intent.getStringExtra("audio");
                    Log.v("dashgd",""+backgroundMusicPath);
                    PrepairBackgroundMusicForMixing(0,(int) Double.parseDouble(mDuration));
//                    executeCutBackgroundAudioCommand(0, Integer.parseInt(mDuration),backgroundMusicPath);
                }
            } catch (Exception e) {
            }
        }
    }
    // Extract Audio From Video
    private void executeExtractAudioCommand(String inputFileAbsolutePath)
    {
        String extractAudioPath = getAudioFilePath3();
        String[] complexCommand = {"-y", "-i", inputFileAbsolutePath, "-vn", "-ar", "44100", "-ac", "2", "-b:a", "256k", "-f", "mp3", extractAudioPath};
        long executionId = FFmpeg.executeAsync(complexCommand, new ExecuteCallback() {
            @Override
            public void apply(final long executionId, final int returnCode) {
                if (returnCode == RETURN_CODE_SUCCESS) {
                    audioPath=extractAudioPath;
                    mDuration=""+videoView.getDuration();
                    Log.v("jsfdjhg",audioPath+","+mDuration);
                } else if (returnCode == RETURN_CODE_CANCEL) {
                } else {
               }
            }
        });
        Log.v("dsfjks",""+executionId);

    }
    // Starting To Marge External Audio With Video
    private void executeCutAudioCommand(int startMs, int endMs,Uri selectedAudioUri)
    {
        File moviesDir = getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MUSIC
        );
        String filePrefix = "my_cut_audio";
        String fileExtn = ".mp3";
        String yourRealPath = getPath(PrepairVideoForUploadActivity.this, selectedAudioUri);
        audioPath = getPath(PrepairVideoForUploadActivity.this, selectedAudioUri);
        File dest = new File(moviesDir, filePrefix + fileExtn);
        int fileNo = 0;
        while (dest.exists()) {
            fileNo++;
            dest = new File(moviesDir, filePrefix + fileNo + fileExtn);
        }
        filePath = dest.getAbsolutePath();
        filePath = getAudioFilePath();
        Log.v("lsjd",yourRealPath+","+filePath);
        Log.v("smfjhsdf",startMs+","+endMs);

        String[] complexCommand = {"-i", yourRealPath, "-ss", "" + startMs / 1000, "-t", "" + (endMs - startMs) / 1000, "-acodec", "copy", filePath};
        long executionId = FFmpeg.executeAsync(complexCommand, new ExecuteCallback() {
            @Override
            public void apply(final long executionId, final int returnCode) {
                if (returnCode == RETURN_CODE_SUCCESS) {
                    llMainLoader.setVisibility(View.VISIBLE);
                    audioPath=filePath;
                    mDuration=""+endMs;
                    PrepairVideoForMixing();
                } else if (returnCode == RETURN_CODE_CANCEL) {
                    llMainLoader.setVisibility(View.GONE);
                    Log.v(Config.TAG, "Async command execution cancelled by user.");
                } else {
                    llMainLoader.setVisibility(View.GONE);
                    Log.v(Config.TAG, String.format("Async command execution failed with returnCode=%d.", returnCode));
                }
            }
        });
        Log.v("dsfjks",""+executionId);

    }
    private void CompressVideoForUploading() {
        showProgress("Prepair Video For Uploading ...");
        String outputDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();

        String destPath = outputDir + File.separator + "VID_" + new SimpleDateFormat("yyyyMMdd_HHmmss", getLocale()).format(new Date()) + ".mp4";
        VideoCompress.compressVideoLow(videoLinkFinal, destPath, new VideoCompress.CompressListener() {
            @Override
            public void onStart() {
                startTime = System.currentTimeMillis();
                Log.v("sdghhjgd","onStart");
                UtilVideo.writeFile(PrepairVideoForUploadActivity.this, "Start at: " + new SimpleDateFormat("HH:mm:ss", getLocale()).format(new Date()) + "\n");
            }

            @Override
            public void onSuccess() {
                endTime = System.currentTimeMillis();
                hideProgress();
                UtilVideo.writeFile(PrepairVideoForUploadActivity.this, "End at: " + new SimpleDateFormat("HH:mm:ss", getLocale()).format(new Date()) + "\n");
                UtilVideo.writeFile(PrepairVideoForUploadActivity.this, "Total: " + (1000) + "s" + "\n");
                UtilVideo.writeFile(PrepairVideoForUploadActivity.this);
                Log.v("sdghhjgd","complete");
                videoLinkFinal=destPath;
                Log.v("jhdsghjg",""+videoLinkFinal);
                submitUpload();
            }

            @Override
            public void onFail() {
                endTime = System.currentTimeMillis();
                UtilVideo.writeFile(PrepairVideoForUploadActivity.this, "Failed Compress!!!" + new SimpleDateFormat("HH:mm:ss", getLocale()).format(new Date()));
                Log.v("sdghhjgd","Finished");
            }

            @Override
            public void onProgress(float percent) {
                tv_progress.setText("Prepairing - "+String.valueOf(Math.round(percent)) + "%");
                Log.v("sdghhjgd","onProgress");
            }
        });
    }
    private Locale getLocale() {
        Configuration config = getResources().getConfiguration();
        Locale sysLocale = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sysLocale = getSystemLocale(config);
        } else {
            sysLocale = getSystemLocaleLegacy(config);
        }

        return sysLocale;
    }
    @TargetApi(Build.VERSION_CODES.N)
    public static Locale getSystemLocale(Configuration config){
        return config.getLocales().get(0);
    }
    @SuppressWarnings("deprecation")
    public static Locale getSystemLocaleLegacy(Configuration config){
        return config.locale;
    }

    private void PrepairVideoForMixing() {
        videoPath1 = getVideoFilePath();
        String[] command = {"-y", "-i", firstVideoPath, "-preset", "ultrafast", "-vf", "scale=1920:1080", videoPath1};
        long rc = FFmpeg.execute(command);
        if (rc == RETURN_CODE_SUCCESS) {
            llMainLoader.setVisibility(View.VISIBLE);
            PrepairAudioForMixing();
        } else {
            llMainLoader.setVisibility(View.GONE);
//            Toast.makeText(this, "uri exception1", Toast.LENGTH_LONG).show();

        }
    }

    private void PrepairAudioForMixing() {
        videoPath2 = getVideoFilePath();
        String[] command = {"-y", "-i", audioPath, "-preset", "ultrafast", "-vf", "scale=1920:1080", videoPath2};
        long rc = FFmpeg.execute(command);
        if (rc == RETURN_CODE_SUCCESS) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                llMainLoader.setVisibility(View.VISIBLE);
                addAudioToVideo();
            }
        } else {
            Toast.makeText(this, "uri exception", Toast.LENGTH_LONG).show();
            llMainLoader.setVisibility(View.GONE);
        }
    }

    private void addAudioToVideo() {
        Log.v("jsdhkasj",""+videoPath1+","+videoPath2);
        String outputVideo = getVideoFilePath();
        String[] merge = {"-y", "-i", videoPath1, "-i", videoPath2, "-c", "copy", "-map", "0:v:0", "-map", "1:a:0", outputVideo};

        long rc = FFmpeg.executeAsync(merge, (executionId, returnCode) -> {
            if (returnCode == RETURN_CODE_SUCCESS) {
                llMainLoader.setVisibility(View.GONE);
//                Toast.makeText(this, "Video Mix With Audio", Toast.LENGTH_LONG).show();
                try  {

                    Log.v("jasdgh",""+outputVideo);
                    videoCurrentPlayPath=outputVideo;
                    tvVoice.setText("Voice Added");
                    tvVoice.setTextColor(getResources().getColor(R.color.white));
                    llVoice.setBackgroundColor(getResources().getColor(R.color.green));
                    tvVoiceMsg.setVisibility(View.VISIBLE);
                    videoView.setVideoURI(Uri.parse(outputVideo));
                    videoView.start();
                    pause.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
                    videoLinkFinal=outputVideo;
//                    videoLinkBase64  = encodeFileToBase64Binary(outputVideo);//
//                    Log.v("videoBase64",videoLinkBase64);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (returnCode == Config.RETURN_CODE_CANCEL) {
                llMainLoader.setVisibility(View.GONE);
                Toast.makeText(this, "Duet  cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Duet  failed", Toast.LENGTH_LONG).show();
                llMainLoader.setVisibility(View.GONE);
            }
        });
    }

    // End To Marge External Audio With Video
    //--------------------------------------------------------------------------------------------

    // Starting To Marge Recorded Audio With Video
    private void executeCutVideoCommand(String startMs, String endMs,Uri selectedAudioUri)
    {
        String cutVideoPath = getVideoFilePath();
        Log.v("lsjd",""+cutVideoPath);

        String yourRealPath = getPath(PrepairVideoForUploadActivity.this, selectedAudioUri);
        String[] complexCommand = {"-i", yourRealPath, "-ss", "" + startMs, "-to", "" + endMs, "-c:v", "copy", "-c:a", "copy", cutVideoPath};
        long executionId = FFmpeg.executeAsync(complexCommand, new ExecuteCallback() {
            @Override
            public void apply(final long executionId, final int returnCode) {
                if (returnCode == RETURN_CODE_SUCCESS) {
                    llMainLoader.setVisibility(View.VISIBLE);
//                    Toast.makeText(PrepairVideoForUploadActivity.this, "Cutting", Toast.LENGTH_SHORT).show();
                    firstVideoPath=cutVideoPath;
                    PrepairVideoForMixing();

                } else if (returnCode == RETURN_CODE_CANCEL) {
                    llMainLoader.setVisibility(View.GONE);
                    Log.v(Config.TAG, "Async command execution cancelled by user.");
                } else {
                    llMainLoader.setVisibility(View.GONE);
                    Log.v(Config.TAG, String.format("Async command execution failed with returnCode=%d.", returnCode));
                }
            }
        });
        Log.v("dsfjks",""+executionId);
    }

    //    PrepairVideoForMixing
    //    PrepairAudioForMixing
    //    addAudioToVideo

    // End To Marge Recorded Audio With Video
    //--------------------------------------------------------------------------------------------

    // Starting To Marge External Audio to Backgroud Music

    private void PrepairBackgroundMusicForMixing(int startMs,int endMs) {
        prepairBackground = getAudioFilePath2();
        Log.v("dkjfs","1"+prepairBackground+","+backgroundMusicPath);
        String[] command = {"-y", "-i", backgroundMusicPath, "-preset", "ultrafast", "-vf", "scale=480:840", prepairBackground};
        long rc = FFmpeg.execute(command);
        if (rc == RETURN_CODE_SUCCESS) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                audioPath2=prepairBackground;
                Toast.makeText(activity, "Background Prepair complete", Toast.LENGTH_SHORT).show();
                Log.v("dsfhmg",""+audioPath2);
                prepairBackground = getAudioFilePath2();
                String[] command1 = {"-i", audioPath2, "-ss", "" + startMs / 1000, "-t", "" + (endMs - startMs) / 1000, "-acodec", "copy", prepairBackground};
                long rc1 = FFmpeg.execute(command1);
                if (rc1 == RETURN_CODE_SUCCESS) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        Log.v("dshkgf",""+prepairBackground);
                        audioPath2=prepairBackground;
                        mixAudioToBackround();
                        Toast.makeText(this, "Background Id Trimmed", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(this, "uri exception when cut", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            Toast.makeText(this, "uri exception", Toast.LENGTH_LONG).show();
//            llMainLoader.setVisibility(View.GONE);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void mixAudioToBackround() {
        Log.v("sjdjhsd",""+audioPath+","+audioPath2);
        String outputAudio = getAudioFilePath();
        String[] mix = {"-i", audioPath, "-i", audioPath2, "-filter_complex", "amix=inputs=2:duration=longest", outputAudio};

        long rc = FFmpeg.executeAsync(mix, (executionId, returnCode) -> {
            if (returnCode == RETURN_CODE_SUCCESS) {
                llMainLoader.setVisibility(View.GONE);
                try  {
                    Toast.makeText(this, "Added", Toast.LENGTH_LONG).show();
                    Log.v("sdhgsdf",""+outputAudio);
                    String videoOutputPath = getVideoFilePath();
                    String[] mixAudioAndVideo = {"-i", videoCurrentPlayPath, "-i", outputAudio, "-map", "0:v", "-map", "1:a", "-c:v", "copy", "-shortest", videoOutputPath};
                    long rc1 = FFmpeg.execute(mixAudioAndVideo);
                    if (rc1 == RETURN_CODE_SUCCESS) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

                            videoView.setVideoURI(Uri.parse(videoOutputPath));
                            videoView.start();
                            tvBackground.setText("Background Added");
                            tvBackground.setTextColor(getResources().getColor(R.color.white));
                            llBackground.setBackgroundColor(getResources().getColor(R.color.green));
                            tvBackgroundMsg.setVisibility(View.VISIBLE);
                            pause.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
                            videoLinkFinal=videoOutputPath;
//                            videoLinkBase64  = encodeFileToBase64Binary(videoOutputPath);//
//                            Log.v("videoBase64",videoLinkBase64);
                        }
                    } else {
                        Toast.makeText(this, "uri exception", Toast.LENGTH_LONG).show();
                        llMainLoader.setVisibility(View.GONE);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (returnCode == Config.RETURN_CODE_CANCEL) {
                llMainLoader.setVisibility(View.GONE);
                Toast.makeText(this, "Mixing  cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Mixing  failed", Toast.LENGTH_LONG).show();
                llMainLoader.setVisibility(View.GONE);
            }
        });
    }

    // END To Marge External Audio to Backgroud Music
    //--------------------------------------------------------------------------------------------

    protected String getVideoFilePath() {
        String mPath;
        String fname = new SimpleDateFormat("yyyyMM_dd-HHmmss").format(new Date()) +(r.nextInt(9999 - 1111) + 1111)+ "duet.mp4";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            File appSpecificExternalDir = new File(getExternalFilesDir("DuetLK"), fname);
            mPath = appSpecificExternalDir.getAbsolutePath();
        } else {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "eighteen" + "/";
            File dir = new File(path);

            boolean isDirectoryCreated = dir.exists();
            if (!isDirectoryCreated) {
                dir.mkdir();
            }
            mPath = path + fname;
        }
        return mPath;
    }

    protected String getAudioFilePath() {
        String mPath;
        String fname = new SimpleDateFormat("yyyyMM_dd-HHmmss").format(new Date())+(r.nextInt(9999 - 1111) + 1111) + "mixduet.mp3";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            File appSpecificExternalDir = new File(getExternalFilesDir("DuetLK"), fname);
            mPath = appSpecificExternalDir.getAbsolutePath();
        } else {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "eighteen" + "/";
            File dir = new File(path);

            boolean isDirectoryCreated = dir.exists();
            if (!isDirectoryCreated) {
                dir.mkdir();
            }
            mPath = path + fname;
        }
        return mPath;
    }

    protected String getAudioFilePath2() {
        String mPath;
        String fname = new SimpleDateFormat("yyyyMM_dd-HHmmss").format(new Date()) +(r.nextInt(9999 - 1111))+ "mixduet.mp3";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            File appSpecificExternalDir = new File(getExternalFilesDir("DuetLK"), fname);
            mPath = appSpecificExternalDir.getAbsolutePath();
        } else {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "eighteenpre" + "/";
            File dir = new File(path);

            boolean isDirectoryCreated = dir.exists();
            if (!isDirectoryCreated) {
                dir.mkdir();
            }
            mPath = path + fname;
        }
        return mPath;
    }
    protected String getAudioFilePath3() {
        String mPath;
        String fname = new SimpleDateFormat("yyyyMM_dd-HHmmss").format(new Date()) +(r.nextInt(9999 - 1111))+ "simpleduet.mp3";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            File appSpecificExternalDir = new File(getExternalFilesDir("DuetLK"), fname);
            mPath = appSpecificExternalDir.getAbsolutePath();
        } else {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "eighteenpre" + "/";
            File dir = new File(path);

            boolean isDirectoryCreated = dir.exists();
            if (!isDirectoryCreated) {
                dir.mkdir();
            }
            mPath = path + fname;
        }
        return mPath;
    }

    public static String encodeFileToBase64Binary(String fileName) throws IOException {
        Log.d(TAG, "encodeFileToBase64Binary: " + fileName);
        File file = new File(fileName);
        Log.v("hjghjjhj",""+file);
        Uri ImageUri=Uri.fromFile(file);
        byte[] bytes = loadFile(file);
        String encodedString = Base64.encodeToString(bytes, Base64.DEFAULT);
        Log.v(TAG, "gsadgjhdsg: " + encodedString);
        return encodedString;
    }
    private static byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        Log.v("hjkdh",""+length);

        Log.v("lengthhhytftf",""+length);
        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        is.close();
        return bytes;
    }


    private String getPath(final Context context, final Uri uri) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                }
                else if (isDownloadsDocument(uri)) {

                    final String id = DocumentsContract.getDocumentId(uri);

                    if (id != null && id.startsWith("raw:")) {
                        return id.substring(4);
                    }

                    String[] contentUriPrefixesToTry = new String[]{
                            "content://downloads/public_downloads",
                            "content://downloads/my_downloads",
                            "content://downloads/all_downloads"
                    };

                    for (String contentUriPrefix : contentUriPrefixesToTry) {
                        Uri contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), Long.valueOf(id));
                        try {
                            String path = getDataColumn(context, contentUri, null, null);
                            if (path != null) {
                                return path;
                            }
                        } catch (Exception e) {}
                    }

                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("Audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }else if ("Video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    }else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{
                            split[1]
                    };

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {
                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        }

        return null;
    }

    private String getDataColumn(Context context, Uri uri, String selection,
                                 String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }




    private void setUploadMyPostData(final String UserId,final String City, final String Location,final String Title,final String Description,final String PostRelto,final String Videolink)
    {
        final JSONObject obj = new JSONObject();
        try
        {
            obj.put("UserId", UserId);
            obj.put("City", City);
            obj.put("Location", Location);
            obj.put("Title", Title);
            obj.put("Description", Description);
            obj.put("PostRelto", PostRelto);
            obj.put("Videolink", Videolink);
            obj.put("Thumbnail", Thumbnail);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.v("ksjdf",""+obj);
        final RequestQueue requestQueue= Volley.newRequestQueue(PrepairVideoForUploadActivity.this);

        final JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, API.UploadPost, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.v("response",response.toString());
                try {
                    boolean status=response.getBoolean("Status");

                    String Msg=response.getString("Message");
                    if(status)
                    {
                        skLoader.setVisibility(View.GONE);
                        tv_progress.setVisibility(View.GONE);
                        tvSubmit.setVisibility(View.VISIBLE);
                        tvSubmit.setEnabled(false);
                        Toast.makeText(PrepairVideoForUploadActivity.this, ""+Msg, Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }else{

                        Toast.makeText(PrepairVideoForUploadActivity.this, ""+Msg, Toast.LENGTH_LONG).show();
                        skLoader.setVisibility(View.GONE);
                        tv_progress.setVisibility(View.GONE);
                        tvSubmit.setVisibility(View.VISIBLE);
                        tvSubmit.setEnabled(true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("jgdkjhkj",error.toString());
                skLoader.setVisibility(View.GONE);
                tv_progress.setVisibility(View.GONE);
                tvSubmit.setVisibility(View.VISIBLE);
                tvSubmit.setEnabled(true);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(600000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(request);
    }

    public void setUploadMyPostData1(final Context context, String UserId, String City, String Location, String Title, String Description, String PostRelto, String Videolink, final SpinKitView loader) {


        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<UploadResponse> call = git.UplaodRequest(UserId, City, Location, Title, Description, PostRelto, Videolink);
            call.enqueue(new Callback<UploadResponse>() {
                @Override
                public void onResponse(Call<UploadResponse> call, retrofit2.Response<UploadResponse> response) {
                    Log.v("payment_req", "hello response : " + new Gson().toJson(response.body()).toString());
                    if (loader != null) {
                        if (loader.getVisibility() == View.VISIBLE)
                            loader.setVisibility(View.GONE);
                        if(response!=null && response.body()!=null) {
                            // Log.v("payment_req", "hello response : " + new Gson().toJson(response.body()).toString());
                            /// Toast.makeText(context, "" + new Gson().toJson(response.body()).toString(), Toast.LENGTH_SHORT).show();
                            // UploadResponse response1=    new Gson().toJson(response.body()).toString();
                            if (response.body().getStatus()) {
                                tvSubmit.setVisibility(View.VISIBLE);
                                tvSubmit.setEnabled(false);
                                Toast.makeText(PrepairVideoForUploadActivity.this, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(PrepairVideoForUploadActivity.this, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();

                            }
                        }else {
                            Toast.makeText(PrepairVideoForUploadActivity.this, "htahat"    , Toast.LENGTH_LONG).show();

                        }
                    }
                }

                @Override
                public void onFailure(Call<UploadResponse> call, Throwable t) {
                    Log.e("response", "error ");
                    if (loader != null) {
                        if (loader.getVisibility() == View.VISIBLE)
                            loader.setVisibility(View.GONE);
                    }
                    Toast.makeText(PrepairVideoForUploadActivity.this, "rehtdhth" + t.getMessage(), Toast.LENGTH_LONG).show();

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(PrepairVideoForUploadActivity.this, "gvffs   " + e.getMessage(), Toast.LENGTH_LONG).show();

            if (loader != null) {
                if (loader.getVisibility() == View.VISIBLE)
                    loader.setVisibility(View.GONE);
            }
        }

    }

}