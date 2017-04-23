package com.hc.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.hc.baselibrary.R;

import java.lang.ref.WeakReference;

/**
 * Created by laxzh on 2017/4/22.
 */

public class AlertDialog extends Dialog {

    private AlertController mAlert;

    public AlertDialog(Context context, int themeResId) {
        super(context, themeResId);
        mAlert = new AlertController(this, getWindow());
    }

    //设置文本
    public void setText(int viewId, CharSequence text) {
        //为了减少每次点击时都findViewById的次数，因此弄一个缓存来保存，如果查找的在缓存中则不需要继续查找下去
        mAlert .setText(viewId, text);
    }

    public  <T extends View> T getViews(int viewId) {
        return mAlert.getViews(viewId);
    }

    //设置点击事件
    public void setOnClickListenter(int viewId, View.OnClickListener listener) {
        mAlert.setOnClickListenter(viewId, listener);
    }







    public static class Builder {
        private final AlertController.AlertParams P;

        public Builder(Context context) {
            this(context, R.style.dialog);
        }

        public Builder(Context context, int themeResId) {
            P = new AlertController.AlertParams(context, themeResId);
        }

        //创建Dialog
        public AlertDialog create() {
            final AlertDialog dialog = new AlertDialog(this.P.mContext, P.mThemeResId);
            P.apply(dialog.mAlert);
            dialog.setCancelable(this.P.mCancelable);
            if(P.mCancelable) {
                dialog.setCanceledOnTouchOutside(true);
            }

            dialog.setOnCancelListener(P.mOnCancelListener);
            dialog.setOnDismissListener(P.mOnDismissListener);
            if(P.mOnKeyListener != null) {
                dialog.setOnKeyListener(P.mOnKeyListener);
            }

            return dialog;
        }

        //显示Dialog
        public AlertDialog show() {
            final AlertDialog dialog = create();
            dialog.show();
            return dialog;
        }


        //设置View(布局),cancelable,onCancelListener,onDismissListener,onKeyListener，text(文本)，listener(点击事件)
        public AlertDialog.Builder setContentView(View view) {
            this.P.mView = view;
            this.P.mViewLayoutResId = 0;
            return this;
        }

        public AlertDialog.Builder setContentView(int layoutResId) {
            this.P.mView = null;
            this.P.mViewLayoutResId = layoutResId;
            return this;
        }

        public AlertDialog.Builder setCancelable(boolean cancelable) {
            this.P.mCancelable = cancelable;
            return this;
        }

        public AlertDialog.Builder setOnCancelListener(OnCancelListener onCancelListener) {
            this.P.mOnCancelListener = onCancelListener;
            return this;
        }

        public AlertDialog.Builder setOnDismissListener(OnDismissListener onDismissListener) {
            this.P.mOnDismissListener = onDismissListener;
            return this;
        }

        public AlertDialog.Builder setOnKeyListener(OnKeyListener onKeyListener) {
            this.P.mOnKeyListener = onKeyListener;
            return this;
        }

        public Builder setText(int viewId, CharSequence text){
            P.mTextArray.put(viewId, text);
            return this;
        }

        public Builder setOnClickListener(int viewId, View.OnClickListener listener){
            P.mClickArray.put(viewId, listener);
            return this;
        }

        //设置全屏效果
        public Builder fullWidth(){
            P.mWidth = ViewGroup.LayoutParams.MATCH_PARENT;
            return this;
        }

        //设置底部弹出   是否有动画
        public Builder fromBottom(boolean isAnimation){
            if (isAnimation){
                P.mAnimation = R.style.dialog_from_bottom_anim;
            }
            P.mGravity = Gravity.BOTTOM;
            return this;
        }

        //设置dialog的宽高
        public Builder setWidthAndHeight(int width, int height){
            P.mWidth = width;
            P.mHeight = height;
            return this;
        }

        //添加默认动画
        public Builder addDefaultAnimation(){
            P.mAnimation = R.style.dialog_from_bottom_anim;
            return this;
        }

        //添加动画
        public Builder addAnimation(int styleAnimation){
            P.mAnimation = styleAnimation;
            return this;
        }

    }
}
