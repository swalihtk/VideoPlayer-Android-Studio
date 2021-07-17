package io.swalitk.github.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private File file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView=findViewById(R.id.recyclerView_main_video_list);
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                        displayVideos();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(MainActivity.this, "File permission needed!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    // Get Videos
    public ArrayList<File> getVideos(File file){
        ArrayList<File> myVideos=new ArrayList<>();
        File[] allFiles=file.listFiles();

        for(File myFile: allFiles){
            if(!myFile.isHidden() && myFile.isDirectory()){
                myVideos.addAll(getVideos(myFile));
            }else{
                if(myFile.getName().endsWith(".mp4")){
                    myVideos.add(myFile);
                }
            }
        }
        return myVideos;
    }

    // Display All Videos
    public void displayVideos(){
        ArrayList<File> allVideos=new ArrayList<>(getVideos(Environment.getExternalStorageDirectory()));
        VideoListAdapter videoAdapter=new VideoListAdapter(MainActivity.this, allVideos);
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 1));
        recyclerView.setAdapter(videoAdapter);
    }


    // Recycler View
    public class VideoFileViewHolder extends RecyclerView.ViewHolder{

        CardView videoContainer;
        TextView videoName;
        public VideoFileViewHolder(@NonNull View itemView) {
            super(itemView);
            videoName=itemView.findViewById(R.id.recyclerView_video_name_textView);
            videoContainer=itemView.findViewById(R.id.card_video_container);
        }
    }

    public class VideoListAdapter extends RecyclerView.Adapter<VideoFileViewHolder>{

        ArrayList<File> allVideos;
        Context context;

        VideoListAdapter(Context context, ArrayList<File> allVideos){
            this.context=context;
            this.allVideos=allVideos;
        }
        @NonNull
        @Override
        public VideoFileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new VideoFileViewHolder(LayoutInflater.from(context).inflate(R.layout.video_list_design, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MainActivity.VideoFileViewHolder holder, int position) {
            holder.videoName.setText(allVideos.get(position).getName());
            holder.videoName.setSelected(true);
            holder.videoContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(MainActivity.this, VideoPlayerActivity.class);
                    intent.putExtra("videoPath", allVideos.get(position).getAbsolutePath());
                    intent.putExtra("videoName", allVideos.get(position).getName());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return allVideos.size();
        }

    }
}