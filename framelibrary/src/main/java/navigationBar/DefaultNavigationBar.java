package navigationBar;

import android.content.Context;
import android.view.ViewGroup;

import com.example.administrator.framelibrary.R;
import com.hc.navigationBar.AbsNavigationBar;

/**
 * Created by soft01 on 2017/4/23.
 * 邮箱：450848477@qq.com
 * Version:1.0
 * Description:
 */

public class DefaultNavigationBar extends AbsNavigationBar {

    public DefaultNavigationBar(Builder.AbsNavigationParams params) {
        super(params);
    }

    @Override
    public int bindLayoutId() {
        return R.layout.title_bar;
    }

    @Override
    public void applyView() {

    }


    public static class Builder extends AbsNavigationBar.Builder{

        AbsNavigationParams P;

        public Builder(Context context, ViewGroup parent) {
            super(context, parent);
            P = new DefaultNavigationParams(context, parent);
        }

        @Override
        public DefaultNavigationBar builder() {
            DefaultNavigationBar navigationBar = new DefaultNavigationBar(P);
            return navigationBar;
        }

        //设置所有效果

        public static class DefaultNavigationParams extends AbsNavigationBar.Builder.AbsNavigationParams{

            //把设置的效果放进来

            DefaultNavigationParams(Context context, ViewGroup parent) {
                super(context, parent);
            }
        }
    }
}
