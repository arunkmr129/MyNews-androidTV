package com.example.mynewsandroidtv.videoEpisod;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mynewsandroidtv.R;
import com.example.mynewsandroidtv.activity.PlayerActivity;
import com.example.mynewsandroidtv.adapter.VideoEpisodeAdapter;
import com.example.mynewsandroidtv.model.MyItemModel;
import com.example.mynewsandroidtv.utility.AppConst;
import com.example.mynewsandroidtv.utility.UtilityClass;


public class VideoEpisodeFragment extends Fragment {

    private static final String TAG = "VideoEpisodeFragment";
    public GridView gridViewCategories;
    private ProgressBar pbLoadingIndicator;
    private TextView tvEmptyGridItemMsg, btnEmptyGridItemFocus;
    private UtilityClass utilityClass;
    private Context context;
    public MyItemModel miCategory;
    private VideoEpisodeAdapter videoEpisodeAdapter;
    private Animation hyperspaceJumpAnimation;

    public static VideoEpisodeFragment newInstance(MyItemModel miCategory, boolean isSingleCategory) {
        VideoEpisodeFragment fragment = new VideoEpisodeFragment();
        fragment.miCategory = miCategory;
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getActivity() != null) {
            this.utilityClass = new UtilityClass(getActivity());
            this.context = getActivity();
            this.hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.fly_in_from_center);
        }

        View rootView = inflater.inflate(R.layout.grid_layout_episode, container, false);
        gridViewCategories = rootView.findViewById(R.id.my_grid_view);
        pbLoadingIndicator = rootView.findViewById(R.id.pb_loading_indicator);
        tvEmptyGridItemMsg = rootView.findViewById(R.id.tv_empty_grid_item_msg);
        btnEmptyGridItemFocus = rootView.findViewById(R.id.btn_empty_grid_item_focus);
        pbLoadingIndicator.setVisibility(View.INVISIBLE);
        float mDensity = getResources().getDisplayMetrics().density;

        gridViewCategories.setSelector(R.color.transparent);
        gridViewCategories.setVerticalScrollBarEnabled(false);

        rootView.setPadding((int) (8 * mDensity + 0.5f), (int) (40 * mDensity + 0.5f), (int) (17 * mDensity + 0.5f), (int) (0 * mDensity + 0.5f));

        gridViewCategories.setNumColumns(4);
        gridViewCategories.setVerticalSpacing((int) (10 * mDensity + 0.5f));
        gridViewCategories.setHorizontalSpacing((int) (1 * mDensity + 0.5f));

        videoEpisodeAdapter = new VideoEpisodeAdapter(context, miCategory.getVideoItemModelList());
        gridViewCategories.setAdapter(videoEpisodeAdapter);

        gridViewCategories.requestFocus();
        gridViewCategories.setFocusable(true);
        gridViewCategories.setSelection(0);

        gridViewCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                view.startAnimation(hyperspaceJumpAnimation);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        gridViewCategories.setOnItemClickListener((adapterView, view, i, l) -> {
            if (utilityClass.checkInternetConnection()) {
                Intent intent = new Intent(context, PlayerActivity.class);
                intent.putExtra(AppConst.ITEM, miCategory.getVideoItemModelList().get(i));
                startActivity(intent);
            }
        });

        return rootView;
    }

}
