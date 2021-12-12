package com.example.mynewsandroidtv.interfaceCallback;

import android.app.Dialog;

public interface DialogBtnPressResponse {
    void dialogLeftButtonPressed(Dialog dialog, int tag);

    void dialogExit(Dialog dialog, int tag) ;

    void dialogRightButtonPressed(Dialog dialog, int tag);
}
