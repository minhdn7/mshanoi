package com.mkit.mshanoi.ui.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;

import com.mkit.mshanoi.R;
import com.mkit.mshanoi.app.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PolicyActivity extends BaseActivity {

    @BindView(R.id.webView)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Điều khoản sử dụng");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
