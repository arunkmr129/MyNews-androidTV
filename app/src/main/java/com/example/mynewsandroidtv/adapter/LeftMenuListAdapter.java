package com.example.mynewsandroidtv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mynewsandroidtv.R;
import com.example.mynewsandroidtv.model.MenuItemModel;
import com.example.mynewsandroidtv.utility.UtilityClass;

import java.util.List;


public class LeftMenuListAdapter extends BaseAdapter {
    private List<MenuItemModel> appMenuItems;
    private Context context;
    private UtilityClass utilityClass;

    public LeftMenuListAdapter(Context context, List<MenuItemModel> appMenuItems) {
        this.appMenuItems = appMenuItems;
        this.context = context;
        this.utilityClass = new UtilityClass(context);
    }

    @Override
    public int getCount() {
        return appMenuItems.size();
    }

    @Override
    public Object getItem(int position) {
        return appMenuItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View listView;
        if (view == null) {
            listView = LayoutInflater.from(context).inflate(R.layout.ma_left_menu_items, viewGroup, false);
        } else {
            listView = view;
        }

        TextView textViewMenuItem = listView.findViewById(R.id.tv_menu_name);
        try {
            if (textViewMenuItem != null) {
                textViewMenuItem.setText(appMenuItems.get(position).getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        float mDensity = context.getResources().getDisplayMetrics().density;
        textViewMenuItem.setPadding((int) (35
                * mDensity + 0.5f), 0, 5, 0);
        return listView;
    }
}
