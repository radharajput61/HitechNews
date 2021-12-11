package com.app.hitechnews.Activity.Post;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.app.hitechnews.Adapter.BgMusicAdapter;
import com.app.hitechnews.Model.BGMusic;
import com.app.hitechnews.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class BackgroundMusicActivity extends AppCompatActivity {

    RecyclerView backgroundMusic_recyclerview;
    ArrayList<BGMusic> bg_music= new ArrayList<BGMusic>();
    BgMusicAdapter bgMusicAdapter;
    MediaPlayer mp;
    String path = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background_music);
        backgroundMusic_recyclerview= findViewById(R.id.backgroundMusic_recyclerview);
        backgroundMusic_recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        copyrawtoSDCArd();
    }
    private void copyrawtoSDCArd() {
        bg_music.add(new BGMusic("music1", Environment.getExternalStorageDirectory() + "/Android/data/music1" ,R.raw.music1) );
        bg_music.add(new BGMusic("music2",Environment.getExternalStorageDirectory() + "/Android/data/music2",R.raw.music2) );
        bg_music.add(new BGMusic("music3",Environment.getExternalStorageDirectory() + "/Android/data/music3",R.raw.music3) );
        bg_music.add(new BGMusic("music4",Environment.getExternalStorageDirectory() + "/Android/data/music4",R.raw.music4) );
        bg_music.add(new BGMusic("music5",Environment.getExternalStorageDirectory() + "/Android/data/music5",R.raw.music5) );
        bg_music.add(new BGMusic("music6",Environment.getExternalStorageDirectory() + "/Android/data/music6",R.raw.music6) );
        bg_music.add(new BGMusic("music7",Environment.getExternalStorageDirectory() + "/Android/data/music7",R.raw.music7) );
        int[] iArr = {R.raw.music1, R.raw.music2, R.raw.music3, R.raw.music4, R.raw.music5, R.raw.music6, R.raw.music7};
        for (int i = 0; i < 7; i++) {
            path = Environment.getExternalStorageDirectory() + "/Music";
            File file = new File(path);
            if (file.mkdirs() || file.isDirectory()) {
                String str = i + ".mp3";
                try {
                    CopyRAWtoSDCard(iArr[i], path + File.separator + str);
                } catch (IOException e) {
                    Log.v("CopyRAWtoSDCard",""+e.getMessage());
                    e.printStackTrace();
                }
                Log.v("CopyRAWtoSDCard",""+iArr[i]);
                Log.v("CopyRAWtoSDCard","  this.path   "+path + "  File.separator  "+File.separator + " str  "+str);

            }
        }

        bgMusicAdapter = new BgMusicAdapter( BackgroundMusicActivity.this,bg_music, new BgMusicAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BGMusic item) {
                if(BgMusicAdapter.btnClick.equalsIgnoreCase("add"))
                {
                    path = Environment.getExternalStorageDirectory() + "/Music/"+ "0.mp3";
                    Log.v("sdfjhgs",""+path);
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("audio",""+path);
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();

                }else if(BgMusicAdapter.btnClick.equalsIgnoreCase("play")){
                    if(mp!=null)
                        mp.release();

                    mp = MediaPlayer.create(BackgroundMusicActivity.this, item.getResource());
                    mp.start();
                }
            }
        });
        backgroundMusic_recyclerview.setAdapter(bgMusicAdapter);
    }
    private void CopyRAWtoSDCard(int id, String path) throws IOException {
        InputStream in = getResources().openRawResource(id);
        FileOutputStream out = new FileOutputStream(path);
        byte[] buff = new byte[1024];
        int read = 0;
        try {
            while ((read = in.read(buff)) > 0) {
                out.write(buff, 0, read);
            }



        } finally {
            in.close();
            out.close();

            Log.v("  out ","out "+out );
            Log.v("  in  ","in  "+in );
        }



    }
}