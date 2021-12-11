package com.app.hitechnews.Activity.Home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.hitechnews.Adapter.CommentAdapter;
import com.app.hitechnews.Model.CommentModel;
import com.app.hitechnews.R;
import com.app.hitechnews.Util.API;
import com.app.hitechnews.Util.MyPreferences;
import com.github.ybq.android.spinkit.SpinKitView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends AppCompatActivity {

    TextView tvLike,tvComment,tvView;
    String PostId,Title,Description,Location,Date,RelatedTo,TotalComment,TotalLike,TotalView,VideoLink,UserId;
    VideoView videoView;
    ImageView pause;
    SeekBar seekBar;
    TextView current,total,tvPostComment;
    SpinKitView skLoaderComment;
    LinearLayout showProgress,relativeLayout,llMainLoader;
    double current_pos, total_duration;
    List<CommentModel> commentModels = new ArrayList<>();
    CommentAdapter commentAdapter;
    RecyclerView comment_recyclerview;
    EditText etComment;
    MyPreferences myPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        PostId= getIntent().getStringExtra("PostId");
        Title= getIntent().getStringExtra("Title");//
        Description= getIntent().getStringExtra("Description");//
        Location= getIntent().getStringExtra("Location");//
        Date= getIntent().getStringExtra("Date");
        RelatedTo= getIntent().getStringExtra("RelatedTo");
        TotalComment= getIntent().getStringExtra("TotalComment");//
        TotalLike= getIntent().getStringExtra("TotalLike");//
        TotalView= getIntent().getStringExtra("TotalView");//
        VideoLink= getIntent().getStringExtra("VideoLink");//

        myPreferences=new MyPreferences(getApplicationContext());
        UserId=myPreferences.getUserId();
        llMainLoader=findViewById(R.id.llMainLoader);
        tvPostComment=findViewById(R.id.tvPostComment);
        skLoaderComment=findViewById(R.id.skLoaderComment);
        etComment=findViewById(R.id.etComment);
        tvLike=findViewById(R.id.tvLike);
        tvLike.setText(TotalLike);
        tvComment=findViewById(R.id.tvComment);
        tvComment.setText(TotalComment);
        tvView=findViewById(R.id.tvView);
        tvView.setText(TotalView);
        comment_recyclerview=findViewById(R.id.comment_recyclerview);
        comment_recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        setVideo();
        getPostCommentData();
        tvPostComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etComment.getText().toString().isEmpty())
                {
                    etComment.setError("Please Enter Comment");
                }
                else
                {
                    skLoaderComment.setVisibility(View.VISIBLE);
                    tvPostComment.setVisibility(View.GONE);
                    tvPostComment.setEnabled(false);
                    setPostCommentData(etComment.getText().toString().trim(),PostId,UserId);
                }
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
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                setVideoProgress();
            }
        });
        playVideo();
        setPause();
    }

    public void playVideo() {
        try  {
            videoView.setVideoURI(Uri.parse(VideoLink));
            videoView.start();
            pause.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    // display video progress
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

    private void getPostCommentData()
    {

        final JSONObject obj = new JSONObject();
        try
        {
            obj.put("PostId", PostId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.v("hgsdfad",obj.toString());

        final RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        final JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, API.CommentPostList, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.v("response",response.toString());
                try {
                    JSONArray array=response.getJSONArray("Response");
                    Log.v("adfghd",""+array);

                    if(array.length()>0)
                    {
                        for (int i=0;i<array.length();i++)
                        {
                            Log.v("hvjfg",array.toString());
                            JSONObject object=array.getJSONObject(i);
                            String name="Suraj";
                            String CommentMsg=object.getString("CommentMsg");

                            commentModels.add(new CommentModel("",name,CommentMsg));
                        }
                        commentAdapter =new CommentAdapter(getApplicationContext(),commentModels);
                        comment_recyclerview.setAdapter(commentAdapter);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("jgdkjhkj",error.toString());
            }
        });
        requestQueue.add(request);
    }

    private void setPostCommentData(final String CommentMsg,final String PostId,final String UserId)
    {

        final JSONObject obj = new JSONObject();
        try
        {
            obj.put("CommentMsg", CommentMsg);
            obj.put("PostId", PostId);
            obj.put("UserId", UserId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.v("sdfjhg",obj.toString());

        final RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        final JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, API.CommentPost, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.v("response",response.toString());
                try {
                    boolean status=response.getBoolean("Status");

                    String Msg=response.getString("Message");
                    if(status)
                    {
                        skLoaderComment.setVisibility(View.GONE);
                        tvPostComment.setVisibility(View.VISIBLE);
                        tvPostComment.setEnabled(false);
                        etComment.setText("");
                        Toast.makeText(CommentActivity.this, ""+Msg, Toast.LENGTH_LONG).show();
                        getPostCommentData();
                    }else{

                        Toast.makeText(CommentActivity.this, ""+Msg, Toast.LENGTH_LONG).show();
                        skLoaderComment.setVisibility(View.GONE);
                        tvPostComment.setVisibility(View.VISIBLE);
                        tvPostComment.setEnabled(true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("jgdkjhkj",error.toString());
            }
        });
        requestQueue.add(request);
    }
}