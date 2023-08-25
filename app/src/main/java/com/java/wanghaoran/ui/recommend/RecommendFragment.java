package com.java.wanghaoran.ui.recommend;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.java.wanghaoran.databinding.FragmentRecommendBinding;

public class RecommendFragment extends Fragment {

    private FragmentRecommendBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        RecommendViewModel recommendViewModel =
                new ViewModelProvider(this).get(RecommendViewModel.class);

        binding = FragmentRecommendBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textRecommend;
        recommendViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}