package com.hc.baselibrary.ioc;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by soft01 on 2017/4/18.
 */

public class ViewUtils {

    public static void inject(Activity activity){
        inject(new ViewFinder(activity),activity);
    }

    public static void inject(View view){
        inject(new ViewFinder(view),view);
    }

    public static void inject(View view,Object object){
        inject(new ViewFinder(view),view);
    }

    //兼容上面三个方法  反射需要执行的类
    public static void inject(ViewFinder viewFinder, Object object){
        injectField(viewFinder, object);
        injectEvent(viewFinder, object);
    }

    private static void injectField(ViewFinder viewFinder, Object object) {
        //获取类中的所有属性
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();

        //获取viewById的value值
        for(Field field : fields){
            ViewById viewById = field.getAnnotation(ViewById.class);
            if(viewById != null){
                int viewId = viewById.value();
                //findViewById找到view
                View view = viewFinder.findViewById(viewId);
                if(view != null){
                    //能够注入所有修饰符
                    field.setAccessible(true);
                    try {
                        field.set(object, view);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        //动态注入找到view
    }

    private static void injectEvent(ViewFinder viewFinder, Object object) {
        //获取类中的所有属性
        Class<?> clazz = object.getClass();
        Method[] methods = clazz.getDeclaredMethods();

        for (Method method:methods) {
            if(method != null){
                OnClick onClick =method.getAnnotation(OnClick.class);
                if (onClick != null){
                    //获取viewById的value值
                    int[] viewIds = onClick.value();
                    for (int viewid : viewIds){
                        //findViewById找到view
                        View view = viewFinder.findViewById(viewid);
                        if(view != null){
                            //view.setOnclickListentener
                            view.setOnClickListener(new DeclaredOnClickListener(method,object));
                        }
                    }
                }
            }
        }
    }

    private static class DeclaredOnClickListener implements View.OnClickListener {

        private Method mMethod;
        private Object mObject;

        public DeclaredOnClickListener(Method method, Object object) {
            this.mMethod = method;
            this.mObject = object;
        }

        @Override
        public void onClick(View v) {
            try {
                //反射执行方法
                mMethod.setAccessible(true);
                mMethod.invoke(mObject,v);
            } catch (Exception e) {
                try {
                    mMethod.invoke(mObject,null);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }
        }
    }
}
