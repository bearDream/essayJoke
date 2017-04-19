package com.hc.essayjoke.joke;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hc.baselibrary.ioc.CheckNet;
import com.hc.baselibrary.ioc.OnClick;
import com.hc.baselibrary.ioc.ViewById;
import com.hc.baselibrary.ioc.ViewUtils;

public class MainActivity extends AppCompatActivity {

    @ViewById(R.id.main_text)
    private TextView textView;

    @ViewById(R.id.main_image)
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtils.inject(this);
        textView.setText("IOC ...");

        AsyncTask asyncTask = new AsyncTask() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                return null;
            }
        };
    }

    @OnClick(R.id.main_image)
    @CheckNet
    private void onClick(ImageView view) {
        Toast.makeText(this, "图片点击事件", Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.main_text)
    private void onClick(TextView textView) {
        Toast.makeText(this, "文字点击事件", Toast.LENGTH_LONG).show();
    }

}
