package com.example.mynewsandroidtv.videoEpisod;


import android.content.Context;
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
import com.example.mynewsandroidtv.activity.MainActivity;
import com.example.mynewsandroidtv.adapter.HybridCategoryAdapter;
import com.example.mynewsandroidtv.model.MyItemModel;
import com.example.mynewsandroidtv.utility.UtilityClass;

import java.util.List;


public class HybridCategoryFragment extends Fragment {
    private static final String TAG = "AudioCategoryFragment";
    public GridView gridViewCategories;
    private ProgressBar pbLoadingIndicator;
    private HybridCategoryAdapter categoryAdapter;
    private UtilityClass utilityClass;
    private Context context;
    private int selectedItem = -1;
    private TextView tvEmptyGridItemMsg;
    private String categoryName;
    private Animation hyperspaceJumpAnimation;

    private String selectedItemId;
    private List<MyItemModel> miCategories;
    private MyItemModel subCategory;

    public static HybridCategoryFragment newInstance(List<MyItemModel> miCategories,
                                                     int selectedItem) {
        HybridCategoryFragment fragment = new HybridCategoryFragment();
        fragment.miCategories = miCategories;
        fragment.selectedItem = selectedItem;
//        fragment.subCategory = subCategory;
        return fragment;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null) {
//            onItemClicked = (FragmentContract.OnCategoryItemClicked) getActivity();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getActivity() != null) {
            this.utilityClass = new UtilityClass(getActivity());
            this.context = getActivity();
            this.hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.fly_in_from_center);
        }

        View rootView = inflater.inflate(R.layout.grid_layout, container, false);
        gridViewCategories = rootView.findViewById(R.id.my_grid_view);
        pbLoadingIndicator = rootView.findViewById(R.id.pb_loading_indicator);
        tvEmptyGridItemMsg = rootView.findViewById(R.id.tv_empty_grid_item_msg);
        pbLoadingIndicator.setVisibility(View.GONE);

        float mDensity = getResources().getDisplayMetrics().density;

        if (miCategories != null) {
            categoryAdapter = new HybridCategoryAdapter(context, miCategories);
            gridViewCategories.setSelector(R.color.transparent);
            gridViewCategories.setAdapter(categoryAdapter);

            tvEmptyGridItemMsg.setVisibility(View.INVISIBLE);

        } else {
            //onFailure(categoryName, false);
        }

        rootView.setPadding(
                (int) (8 * mDensity + 0.5f),
                (int) (40 * mDensity + 0.5f),
                (int) (17 * mDensity + 0.5f),
                (int) (0 * mDensity + 0.5f));

        gridViewCategories.setNumColumns(4);
        gridViewCategories.setVerticalSpacing((int) (10 * mDensity + 0.5f));
        gridViewCategories.setHorizontalSpacing((int) (1 * mDensity + 0.5f));


        gridViewCategories.setVerticalScrollBarEnabled(false);

        if (selectedItem >= 0) {
            gridViewCategories.requestFocus();
            gridViewCategories.setFocusable(true);
            gridViewCategories.setSelection(selectedItem);
        }

        gridViewCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = position;
                view.startAnimation(hyperspaceJumpAnimation);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        gridViewCategories.setOnItemClickListener((parent, view, position, id) -> {
            ((MainActivity) (getActivity())).onCategoryItemClicked(null, position, true);
            selectedItem = position;
        });
        return rootView;
    }


}
