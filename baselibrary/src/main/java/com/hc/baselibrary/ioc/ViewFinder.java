package com.hc.baselibrary.ioc;

import android.app.Activity;
import android.view.View;

/**
 * Created by soft01 on 2017/4/18.
 *
 * View 的findById的辅助类
 */

public class ViewFinder {

    private Activity mActivity;

    private View mView;

    public ViewFinder(Activity activity) {
        this.mActivity = activity;
    }

    public ViewFinder(View view) {
        this.mView = view;
    }

    public View findViewById(int viewId){
        return mActivity!=null?mActivity.findViewById(viewId):mView.findViewById(viewId);
    }

}
