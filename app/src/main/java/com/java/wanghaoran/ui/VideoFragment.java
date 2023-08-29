package com.java.wanghaoran.ui;

import android.media.MediaPlayer;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.net.Uri;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import java.net.URI;

import com.java.wanghaoran.R;

public class VideoFragment extends Fragment {
    private String videoPath  = "";
    private VideoView videoView;

    public VideoFragment() {}

    public static VideoFragment getInstance(String path) {
        VideoFragment fragment = new VideoFragment();
        Bundle args = new Bundle();
        args.putString("video", path);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            videoPath = getArguments().getString("video");
        }
        Log.d("video", videoPath);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        videoView = view.findViewById(R.id.videoView);
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);
        videoView.setMediaController(new MediaController(this.getContext()));
        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {videoView.start();}
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {}
        });
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {return false;}
        });
        videoView.start();
        Log.d("Video", "" + videoView.isPlaying());
        return view;
    }

//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        videoView = view.findViewById(R.id.videoView);
//
//        // 设置视频路径，可以是本地文件路径或网络视频URL
//        String videoPath = videoWebPath;
//        Uri uri = Uri.parse(videoPath);
//        videoView.setVideoURI(uri);
//
//        // 添加媒体控制器
//        MediaController mediaController = new MediaController(requireContext());
//        mediaController.setAnchorView(videoView);
//        videoView.setMediaController(mediaController);
//
//        // 开始播放视频
//        videoView.start();
//    }

}
