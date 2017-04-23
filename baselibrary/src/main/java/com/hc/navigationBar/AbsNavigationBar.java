package com.hc.navigationBar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by soft01 on 2017/4/23.
 * 邮箱：450848477@qq.com
 * Version:1.0
 * Description:导航栏的基类
 */

public class AbsNavigationBar implements INavigationBar{

    private Builder.AbsNavigationParams mParams;

    public AbsNavigationBar (Builder.AbsNavigationParams params){
        this.mParams = params;
        createAndBindView();
    }

    //绑定和创建View
    private void createAndBindView() {
        //创建view
        View navigationView = LayoutInflater.from(mParams.mContext)
                .inflate(bindLayoutId(), mParams.mParent,false);
        //添加
        mParams.mParent.addView(navigationView, 0);

        //绑定
        applyView();
    }

    @Override
    public int bindLayoutId() {
        return 0;
    }

    @Override
    public void applyView() {

    }

    //Builer类
    public abstract static class Builder{

        AbsNavigationParams P;

        public Builder(Context context, ViewGroup parent){
            P = new AbsNavigationParams(context, parent);
        }

        public abstract AbsNavigationBar builder();

        public static class AbsNavigationParams {

            private Context mContext;
            private ViewGroup mParent;

            public AbsNavigationParams(Context context, ViewGroup parent){
                this.mContext = context;
                this.mParent = parent;
            }

        }
    }
}
