package com.example.mynewsandroidtv.adapter;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
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
import com.example.mynewsandroidtv.model.VideoItemModel;
import com.example.mynewsandroidtv.utility.UtilityClass;

import java.util.List;


public class VideoEpisodeAdapter extends BaseAdapter {
    private Context context;
    private float mDensity;
    private LayoutInflater inflater;
    private UtilityClass utilityClass;
    private int imageWidth, imageHeight;
    private int viewHeight = 0;
    private int viewWidth = 0;
    private List<VideoItemModel> videoItemModelList;

    public VideoEpisodeAdapter(Context context, List<VideoItemModel> videoItemModelList) {
        this.context = context;
        this.videoItemModelList = videoItemModelList;
        getImageSize();
        this.mDensity = context.getResources().getDisplayMetrics().density;
        this.utilityClass = new UtilityClass(context);
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return videoItemModelList.size();
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
        ImageView gridImageView;
        getScreenWidth();
        if (convertView == null) {
            viewMain = inflater.inflate(R.layout.ma_grid_item_video_episode, parent, false);
            viewMain.setLayoutParams(new GridView.LayoutParams((ViewGroup.LayoutParams.MATCH_PARENT), ViewGroup.LayoutParams.MATCH_PARENT));
        } else {
            viewMain = convertView;
        }

        TextView tvVideoTitle = viewMain.findViewById(R.id.tv_video_grid_item_title);
        gridImageView = viewMain.findViewById(R.id.iv_video_grid_item_image);
        tvVideoTitle.setText(videoItemModelList.get(position).getName());
        gridImageView.requestLayout();
        gridImageView.getLayoutParams().height = (int) (viewHeight * mDensity + 0.5f);
        gridImageView.getLayoutParams().width = (int) (viewWidth * mDensity + 0.5f);
        gridImageView.setScaleType(ImageView.ScaleType.FIT_XY);

        Glide.with(context).load(videoItemModelList.get(position).getImage())
                .apply(new RequestOptions()
                        .error(R.drawable.vod_default)
                        .placeholder(R.drawable.vod_default)
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(gridImageView);

        tvVideoTitle.setVisibility(View.VISIBLE);

        return viewMain;
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
            this.imageWidth = (165 * 16) / 9;
            this.imageHeight = 112;
            e.printStackTrace();
        }
    }

    private void getScreenWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        viewHeight = displayMetrics.heightPixels / 9 - 8;
        viewWidth = displayMetrics.widthPixels / 12 + 4;
    }

}
