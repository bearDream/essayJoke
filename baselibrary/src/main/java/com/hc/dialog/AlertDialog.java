package com.hc.dialog;

import android.content.Context;
import android.view.ContextThemeWrapper;

import com.hc.baselibrary.R;

/**
 * Created by laxzh on 2017/4/22.
 */

public class AlertDialog extends android.app.AlertDialog{

    public AlertDialog(Context context, int themeResId){
        super(context, themeResId);

    }

    public class Builder{
        private final AlertController.AlertParams P;

        public Builder(Context context) {
            this(context, R.style.dialog);
        }

        public Builder(Context context, int themeResId) {
            P = new AlertController.AlertParams(context, themeResId);
        }

    }
}
