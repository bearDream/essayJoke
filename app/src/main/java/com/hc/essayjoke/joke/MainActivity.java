package com.hc.essayjoke.joke;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    }

    @OnClick(R.id.main_image)
    private void onClick(View view){
        Toast.makeText(this,"点击事件",Toast.LENGTH_LONG).show();
    }

}
