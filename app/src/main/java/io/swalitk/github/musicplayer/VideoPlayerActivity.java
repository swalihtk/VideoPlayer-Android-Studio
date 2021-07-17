package io.swalitk.github.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;

public class VideoPlayerActivity extends AppCompatActivity {

    private ImageView btn_play_pause, btn_next, btn_previous;
    private TextView tw_videoName;
    private SurfaceHolder surfaceHolder_one;
    private SurfaceView surfaceView_one;
    public static final String TAG="myTag";
    private MediaPlayer mediaPlayer;
    private LinearLayout linearLayout_main;
    private ImageView pauseImageView;
    private TextView tw_resolution, tw_duration, tw_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_video_player_landscape);
            pauseImageView=findViewById(R.id.video_play_pause_btn);
            surfaceView_one=findViewById(R.id.surface_view_player_one);
            surfaceView_one.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pauseImageView.getVisibility() == View.VISIBLE){
                        pauseImageView.setVisibility(View.GONE);
                    }else{
                        pauseImageView.setVisibility(View.VISIBLE);
                    }
                }
            });

            pauseImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doStartStop(v);
                }
            });
        }else{
            setContentView(R.layout.activity_video_player);
            pauseImageView=findViewById(R.id.video_play_pause_btn);
            pauseImageView.setVisibility(View.VISIBLE);
            pauseImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doStartStop(v);
                }
            });
            tw_duration=findViewById(R.id.video_duration_text);
            tw_path=findViewById(R.id.video_path_text);
            tw_resolution=findViewById(R.id.video_resolution_text);
            tw_videoName=findViewById(R.id.video_playing_name);

        }


        String videoName=getIntent().getStringExtra("videoName");
        String filePathString=getIntent().getStringExtra("videoPath");
        File  file=new File(filePathString);
        Uri videoUri=Uri.fromFile(file);


        linearLayout_main=findViewById(R.id.linear_layout_video_player);
        surfaceView_one=findViewById(R.id.surface_view_player_one);


        surfaceView_one.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                surfaceHolder_one=holder;
                if(videoUri!=null){
                    mediaPlayer=MediaPlayer.create(getBaseContext(), videoUri, surfaceHolder_one);
                    int video_width=mediaPlayer.getVideoWidth();
                    int video_height=mediaPlayer.getVideoHeight();
                    int video_duration=mediaPlayer.getDuration();
                    Toast.makeText(VideoPlayerActivity.this, video_width+" "+video_height, Toast.LENGTH_SHORT).show();
                    if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                        FrameLayout.LayoutParams params=new FrameLayout.LayoutParams(
                                video_width,
                                video_height
                        );
                        String resolutin_string=video_height+"px X "+video_width+"px";
                        String duration_string=video_duration+"s ";

                        tw_duration.setText(duration_string);
                        tw_resolution.setText(resolutin_string);
                        tw_videoName.setText(videoName);
                        tw_path.setText(filePathString);
                        surfaceView_one.setLayoutParams(params);
                    }else{
                        video_width=video_width*2;
                        video_height=video_height*2;
                        FrameLayout.LayoutParams params=new FrameLayout.LayoutParams(
                                video_width,
                                video_height
                        );
                        surfaceView_one.setLayoutParams(params);
                    }


                    mediaPlayer.start();
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

            }
        });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }

    public void doStartStop(View view){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            pauseImageView.setImageResource(R.drawable.video_play);
        }else{
            mediaPlayer.start();
            pauseImageView.setImageResource(R.drawable.video_pause);
        }
    }

}