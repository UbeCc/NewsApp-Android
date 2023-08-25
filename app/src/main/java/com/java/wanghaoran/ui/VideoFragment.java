package com.java.wanghaoran.ui;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;
import com.java.wanghaoran.R;

import java.net.URI;


public class VideoFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "video";
    private String videoWebPath  = "";
    private VideoView videoView;


    public VideoFragment() {
        // Required empty public constructor
    }


    public static VideoFragment newInstance(String path) {
        VideoFragment fragment = new VideoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, path);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            videoWebPath = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_v_ideo, container, false);

        try {
            videoView = view.findViewById(R.id.videoView);
            String path = videoWebPath;
            Uri uri = Uri.parse(path);
            videoView.setVideoURI(uri);
            videoView.setMediaController(new MediaController(this.getContext()));
            videoView.requestFocus();

            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                }
            });
            videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    return false;
                }
            });
            videoView.start();



            Log.d("Video View", "isPlaying: " + videoView.isPlaying());
        }catch(Exception e){
            Log.d("Video View", "failed " );
        }

//       vv_video.setVideoPath(path);
//       vv_video.setMediaController(new MediaController(this.getBaseContext()));
//       vv_video.requestFocus();
//       vv_video.start();

        return view;
    }
}