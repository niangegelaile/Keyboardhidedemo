package com.ange.keyboardhidedemo;

import android.os.Bundle;
import android.widget.ScrollView;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initKeyBoardListener((ScrollView) findViewById(R.id.scrollView));
    }
}
