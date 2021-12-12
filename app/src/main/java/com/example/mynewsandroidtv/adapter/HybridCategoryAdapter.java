package com.example.mynewsandroidtv.adapter;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.mynewsandroidtv.R;
import com.example.mynewsandroidtv.model.MyItemModel;
import com.example.mynewsandroidtv.utility.UtilityClass;

import java.util.List;


public class HybridCategoryAdapter extends BaseAdapter {

    private Context context;
    private float mDensity;
    private LayoutInflater inflater;
    private UtilityClass utilityClass;
    private int imageWidth, imageHeight;
    public List<MyItemModel> miCategories;


    public HybridCategoryAdapter(Context context, List<MyItemModel> miCategories) {
        this.context = context;
        this.mDensity = context.getResources().getDisplayMetrics().density;
        this.utilityClass = new UtilityClass(context);
        this.inflater = LayoutInflater.from(context);
        this.miCategories = miCategories;
        getImageSize();
    }


    @Override
    public int getCount() {
        return miCategories.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View viewMain;
        String imageUrl;
        MyItemModel miCategory = miCategories.get(position);
        imageUrl = miCategory.getImage();

        if (convertView == null) {
            viewMain = inflater.inflate(R.layout.grid_item_show_series, parent, false);
            viewMain.setLayoutParams(new GridView.LayoutParams((ViewGroup.LayoutParams.MATCH_PARENT), ViewGroup.LayoutParams.MATCH_PARENT));
        } else {
            viewMain = convertView;
        }

        /* Shows and Series */
        TextView tvCategoryTitle = viewMain.findViewById(R.id.grid_item_title);
        ImageView gridImageView = viewMain.findViewById(R.id.grid_item_image);

        tvCategoryTitle.setVisibility(View.VISIBLE);

        gridImageView.requestLayout();

        gridImageView.getLayoutParams().height = (int) (115 * mDensity + 0.5f);
        gridImageView.getLayoutParams().width = (int) (200 * mDensity + 0.5f);
        loadGlide(imageUrl, R.drawable.vod_default, gridImageView);

        gridImageView.setScaleType(ImageView.ScaleType.FIT_XY);

        return viewMain;
    }

    private void loadGlide(String imageUrl, int imagePlaceHolder, ImageView gridImageView) {
        Glide.with(context).load(imageUrl)
                .apply(new RequestOptions()
                        .error(imagePlaceHolder)
                        .placeholder(imagePlaceHolder)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .override(imageWidth, imageHeight))
                .into(gridImageView);
    }

    private void getImageSize() {
        try {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (wm != null) {
                Display display = wm.getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int imageSize = size.x / 3;
                this.imageWidth = (imageSize * 16) / 9;
                this.imageHeight = imageSize;
            }
        } catch (Exception e) {
            this.imageWidth = (300 * 16) / 9;
            this.imageHeight = 300;
        }
    }

}
