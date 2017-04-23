package com.hc.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.widget.ViewDragHelper;
import android.util.SparseArray;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.util.HashMap;

/**
 * Created by laxzh on 2017/4/22.
 */

class AlertController {

    private AlertDialog mDialog;
    private Window mWindow;

    private DialogViewHelper mViewHelper;

    public AlertController(AlertDialog dialog, Window window) {
        this.mDialog = dialog;
        this.mWindow = window;
    }

    public void setViewHelper(DialogViewHelper viewHelper) {
        this.mViewHelper = viewHelper;
    }

    //获取Dialog
    public AlertDialog getDialog() {
        return mDialog;
    }

    //获取Dialog的Window
    public Window getWindow() {
        return mWindow;
    }

    //设置文本
    public void setText(int viewId, CharSequence text) {
        //为了减少每次点击时都findViewById的次数，因此弄一个缓存来保存，如果查找的在缓存中则不需要继续查找下去
        mViewHelper .setText(viewId, text);
    }

    public  <T extends View> T getViews(int viewId) {
        return mViewHelper.getViews(viewId);
    }

    //设置点击事件
    public void setOnClickListenter(int viewId, View.OnClickListener listener) {
        mViewHelper.setOnClickListenter(viewId, listener);
    }

    public static class AlertParams{

        public Context mContext;

        public int mThemeResId;
        //点击空白是否取消  默认可以取消
        public boolean mCancelable = true;
        //取消监听
        public DialogInterface.OnCancelListener mOnCancelListener;
        //消失监听
        public DialogInterface.OnDismissListener mOnDismissListener;
        //按键监听
        public DialogInterface.OnKeyListener mOnKeyListener;
        //布局
        public View mView;
        //布局layout id
        public int mViewLayoutResId;
        //存放字体的修改
        public SparseArray<CharSequence> mTextArray = new SparseArray<>();
        //存放按键监听器
        public SparseArray<View.OnClickListener> mClickArray = new SparseArray<>();
        //宽度
        public int mWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
        //高度
        public int mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
        //动画
        public int mAnimation = 0;
        //位置
        public int mGravity = Gravity.CENTER;

        public AlertParams(Context context, int themeResId){
            this.mContext = context;
            this.mThemeResId = themeResId;
        }

        //绑定和设置参数
        public void apply(AlertController mAlert) {

            //设置布局
            DialogViewHelper viewHelper = null;
            if (mViewLayoutResId != 0){
                viewHelper = new DialogViewHelper(mContext, mViewLayoutResId);
            }

            if (mView != null){
                viewHelper = new DialogViewHelper();
                viewHelper.setContentView(mView);
            }

            //如果没有mViewLayoutResId和mView，则说明没有使用.setContentView（view,ResId），这样的话则需要抛异常
            if(viewHelper == null){
                throw new IllegalArgumentException("请设置布局，调用setContentView方法");
            }

            //给dialog设置布局
            mAlert.getDialog().setContentView(viewHelper.getContentView());

            //设置controller的viewHelper类
            mAlert.setViewHelper(viewHelper);

            //设置文本
            int textArraySize = mTextArray.size();
            for (int i = 0; i < textArraySize; i++) {
                mAlert.setText(mTextArray.keyAt(i),mTextArray.valueAt(i));
            }

            //设置点击
            int clickArraySize = mClickArray.size();
            for (int i = 0; i < clickArraySize; i++) {
                mAlert.setOnClickListenter(mClickArray.keyAt(i),mClickArray.valueAt(i));
            }

            //配置自定义的效果， 全屏， 等效果

            Window window = mAlert.getWindow();

            //设置位置
            window.setGravity(mGravity);
            if(mAnimation != 0){
                window.setWindowAnimations(mAnimation);
            }

            //设置动画
            if (mAnimation != 0){
                window.setWindowAnimations(mAnimation);
            }

            //设置宽高
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = mWidth;
            params.height = mHeight;
            window.setAttributes(params);
        }
    }

}
