package com.example.mynewsandroidtv.utility;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.mynewsandroidtv.R;
import com.example.mynewsandroidtv.interfaceCallback.DialogBtnPressResponse;

import java.io.IOException;
import java.io.InputStream;

public class UtilityClass {
    private Context context;

    public UtilityClass(Context context) {
        this.context = context;
    }

    /**
     * method return font type face
     */
    public Typeface getFontFamily(String fontName) {
        Typeface typeface = null;
        try {
            String path = "fonts/" + fontName;
            typeface = Typeface.createFromAsset(context.getAssets(), path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return typeface;
    }


    public void setDefaultImage(ImageView imageView, String imageUrl) {
        try {
            Glide.with(context)
                    .load(imageUrl)
                    .error(R.drawable.vod_default)
                    .placeholder(R.drawable.vod_default)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * this is Internet Connection testing
     */
    public boolean checkInternetConnection() {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr != null && conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            System.out.println("Internet Connection Not Present");
            return false;
        }
    }


    private Dialog myCustomDialog;

    public void showDialogWithTwoButton(final DialogBtnPressResponse response,
                                        final int tag, String errorMessage) {
        if (myCustomDialog != null && myCustomDialog.isShowing())
            myCustomDialog.dismiss();
        myCustomDialog = new Dialog(context, R.style.CustomTheme);
        myCustomDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        myCustomDialog.setContentView(R.layout.double_button_dialog);

        myCustomDialog.show();

        final Button btnLeft = myCustomDialog.findViewById(R.id.retryBtn);
        final Button btnRight = myCustomDialog.findViewById(R.id.exitBtn);

        TextView appNameExitDialog = myCustomDialog.findViewById(R.id.appName);
        TextView txtExitMessage = myCustomDialog.findViewById(R.id.errorMessage);

        txtExitMessage.setText(errorMessage);
        btnLeft.requestFocus();


        btnLeft.setBackgroundColor(ContextCompat.getColor(context, R.color.btn_selected_bg));
        btnRight.setBackgroundColor(ContextCompat.getColor(context, R.color.btn_un_selected_bg));
        btnLeft.setTextColor(ContextCompat.getColor(context, R.color.btn_selected_text_color));
        btnRight.setTextColor(ContextCompat.getColor(context, R.color.btn_un_selected_text_color));

        btnLeft.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                btnLeft.setBackgroundColor(ContextCompat.getColor(context, R.color.btn_selected_bg));
                btnRight.setBackgroundColor(ContextCompat.getColor(context, R.color.btn_un_selected_bg));
                btnLeft.setTextColor(ContextCompat.getColor(context, R.color.btn_selected_text_color));
                btnRight.setTextColor(ContextCompat.getColor(context, R.color.btn_un_selected_text_color));

            } else {
                btnLeft.setBackgroundColor(ContextCompat.getColor(context, R.color.btn_un_selected_bg));
                btnRight.setBackgroundColor(ContextCompat.getColor(context, R.color.btn_selected_bg));
                btnLeft.setTextColor(ContextCompat.getColor(context, R.color.btn_un_selected_text_color));
                btnRight.setTextColor(ContextCompat.getColor(context, R.color.btn_selected_text_color));
            }
        });
        btnLeft.setOnClickListener(v -> {
            myCustomDialog.dismiss();
            response.dialogLeftButtonPressed(myCustomDialog, tag);
        });
        btnRight.setOnClickListener(v -> {
            response.dialogRightButtonPressed(myCustomDialog, tag);
            myCustomDialog.dismiss();

        });
        myCustomDialog.setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_BUTTON_B) {
                myCustomDialog.dismiss();
                if (response != null)
                    response.dialogExit(myCustomDialog, tag);
                return true;
            } else
                return (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE);
        });
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = context.getAssets().open("app_data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty() || str.equalsIgnoreCase("null") || str.equalsIgnoreCase("");
    }

}
