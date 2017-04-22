package com.hc.dialog;

import android.content.Context;
import android.view.ContextThemeWrapper;

/**
 * Created by laxzh on 2017/4/22.
 */

class AlertController {

    public static class AlertParams{

        public Context mContext;

        public int themeResId;

        public AlertParams(Context context, int themeResId){
            this.mContext = context;
            this.themeResId = themeResId;
        }

    }

}
