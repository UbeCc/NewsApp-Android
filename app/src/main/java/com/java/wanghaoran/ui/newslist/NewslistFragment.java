package com.java.wanghaoran.ui.newslist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.java.wanghaoran.databinding.FragmentNewslistBinding;

public class NewslistFragment extends Fragment {

    private FragmentNewslistBinding binding;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NewslistViewModel dashboardViewModel =
                new ViewModelProvider(this).get(NewslistViewModel.class);

        binding = FragmentNewslistBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textNewslist;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}