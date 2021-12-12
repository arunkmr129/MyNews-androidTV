package com.example.mynewsandroidtv.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mynewsandroidtv.R;
import com.example.mynewsandroidtv.adapter.LeftMenuListAdapter;
import com.example.mynewsandroidtv.model.MainDataModel;
import com.example.mynewsandroidtv.model.MenuItemModel;
import com.example.mynewsandroidtv.utility.MyApplication;
import com.example.mynewsandroidtv.videoEpisod.HybridCategoryFragment;
import com.example.mynewsandroidtv.videoEpisod.VideoEpisodeFragment;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity {

    private ListView leftMenuListView;
    private TextView tvPageTitle;
    private MainDataModel mainDataModel;
    private FragmentManager fragmentManager;
    private int startMenuIndex = -1;
    private int selectedCategoryItemIndex = -1;
    private Fragment fragment;
    private List<MenuItemModel> menuItemModelList;
    private TextView tvLeftMenuLevel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeView();
        getData();
        loadMenu();

        fragmentManager = getSupportFragmentManager();

        leftMenuListView.setOnKeyListener((view, keyCode, keyEvent) -> {
            boolean result = false;
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_DPAD_DOWN:
                        break;
                    case KeyEvent.KEYCODE_DPAD_UP:
                        break;
                }
                if ((startMenuIndex == menuItemModelList.size() - 1 && keyCode == KeyEvent.KEYCODE_DPAD_DOWN)) {
                    leftMenuListView.setSelection(0);
                    result = true;
                } else if (startMenuIndex == 0 && keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                    leftMenuListView.setSelection(menuItemModelList.size() - 1);
                    result = true;
                }
            }
            return result;
        });

        leftMenuListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                startMenuIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        leftMenuListView.setOnItemClickListener((adapterView, view, i, l) -> {
            Fragment fragment = fragmentManager.findFragmentById(R.id.fl_data_container);
            if (fragment instanceof VideoEpisodeFragment)
                makeVideoEpisodeFocusable((VideoEpisodeFragment) fragment);
            else if (fragment instanceof HybridCategoryFragment)
                makeHybridCatFocusable((HybridCategoryFragment) fragment);

            tvLeftMenuLevel = adapterView.getChildAt(i).findViewById(R.id.tv_menu_name);
            tvLeftMenuLevel.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.menu_selected_text_color));
        });

        leftMenuListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                startMenuIndex = position;
                int count = parent.getChildCount();
                for (int j = 0; j < count; j++) {
                    tvLeftMenuLevel = parent.getChildAt(j).findViewById(R.id.tv_menu_name);
                    if (j == position)
                        tvLeftMenuLevel.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.menu_selected_text_color));
                    else
                        tvLeftMenuLevel.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.black_color));
                }
                tvPageTitle.setText(menuItemModelList.get(position).getName());
                onCategoryItemClicked(menuItemModelList, startMenuIndex, false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void initializeView() {
        leftMenuListView = findViewById(R.id.left_menu_list);
        tvPageTitle = findViewById(R.id.tv_page_title);
    }

    private void getData() {
        if (MyApplication.getMainDataModel() != null)
            mainDataModel = MyApplication.getMainDataModel();
    }

    private void loadMenu() {
        menuItemModelList = new ArrayList();
        menuItemModelList = mainDataModel.getMenuItems();
        leftMenuListView.setAdapter(new LeftMenuListAdapter(this, menuItemModelList));
        leftMenuListView.setSelection(0);
    }

    private void makeVideoEpisodeFocusable(VideoEpisodeFragment episodeFragment) {
        episodeFragment.gridViewCategories.requestFocus();
        episodeFragment.gridViewCategories.setFocusable(true);
        episodeFragment.gridViewCategories.setSelection(0);
    }

    private void makeHybridCatFocusable(HybridCategoryFragment categoryFragment) {
        categoryFragment.gridViewCategories.requestFocus();
        categoryFragment.gridViewCategories.setFocusable(true);
        categoryFragment.gridViewCategories.setSelection(0);
    }

    private void makeLeftMenuFocusable() {
        leftMenuListView.requestFocus();
        leftMenuListView.setFocusable(true);
        leftMenuListView.setSelection(startMenuIndex);
    }

    public void onCategoryItemClicked(List<MenuItemModel> arrayListMenuRevised, int itemPosition, boolean isSingleCategory) {
        if (!isSingleCategory) {
            fragment = HybridCategoryFragment.newInstance(arrayListMenuRevised.get(itemPosition).getItems(), selectedCategoryItemIndex);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fl_data_container, fragment);
            fragmentTransaction.commit();
        } else {
            selectedCategoryItemIndex = itemPosition;
            fragment = VideoEpisodeFragment.newInstance(menuItemModelList.get(startMenuIndex).getItems().get(itemPosition), isSingleCategory);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fl_data_container, fragment);
            fragmentTransaction.commit();
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_data_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Fragment fragment = fragmentManager.findFragmentById(R.id.fl_data_container);
        boolean result = false;
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if (leftMenuListView.hasFocus()) {
                    if (fragment instanceof HybridCategoryFragment) {
                        makeHybridCatFocusable((HybridCategoryFragment) fragment);
                        result = true;
                    } else if (fragment instanceof VideoEpisodeFragment) {
                        makeVideoEpisodeFocusable((VideoEpisodeFragment) fragment);
                        result = true;
                    }
                }
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                result = true;
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                if (!leftMenuListView.hasFocus()) {
                    if (fragment instanceof HybridCategoryFragment) {
                        makeLeftMenuFocusable();
                        selectedCategoryItemIndex = -1;
                        result = true;
                    } else if (fragment instanceof VideoEpisodeFragment) {
                        onCategoryItemClicked(menuItemModelList, startMenuIndex, false);
                        result = true;
                    }
                }
                break;
            default:
                break;
        }
        return result || super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = fragmentManager.findFragmentById(R.id.fl_data_container);
//        if (leftMenuListView != null && leftMenuListView.hasFocus()) {
//            dialogsUtil.showDialogWithTwoButtonMain(presenter, AppConst.MAIN_ACTIVITY_INDEX, getString(R.string.exitFromAppMessage));
//            return;
//        }
        /*if (fragment instanceof SettingPageFragment) {
            makeLeftMenuFocusable();
        } else */
        if (((fragment instanceof VideoEpisodeFragment && (menuItemModelList.get(startMenuIndex).getItems().size() == 0)))) {
            makeLeftMenuFocusable();
        } else if (((fragment instanceof VideoEpisodeFragment && (menuItemModelList.get(startMenuIndex).getItems().size() > 1)))) {
            onCategoryItemClicked(menuItemModelList, startMenuIndex, false);
        } else if (leftMenuListView != null && !leftMenuListView.hasFocus()) {
            selectedCategoryItemIndex = -1;
            makeLeftMenuFocusable();
        }
    }

}