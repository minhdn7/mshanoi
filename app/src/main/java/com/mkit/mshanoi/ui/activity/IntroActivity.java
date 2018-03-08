package com.mkit.mshanoi.ui.activity;

import android.app.Application;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.core.Context;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;
import com.google.common.eventbus.EventBus;
import com.google.firebase.messaging.FirebaseMessaging;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.mkit.mshanoi.R;
import com.mkit.mshanoi.domain.model.pojo.response.DiaDiemMsResponse;
import com.mkit.mshanoi.domain.repository.SharePrefDefine;
import com.mkit.mshanoi.domain.repository.TinyDB;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class IntroActivity extends AppIntro {

    public List<DiaDiemMsResponse> danhSachDiemMs = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseMessaging.getInstance().subscribeToTopic("msHaNoi");
        TinyDB tinydb = new TinyDB(this);

        try{

            if(tinydb.getBoolean(SharePrefDefine.IS_INTRODUCE)){
                SliderPage sliderPage1 = new SliderPage();
                sliderPage1.setTitle("Xin chào!");
                sliderPage1.setDescription("Chúng tôi là Vnpt Software");
                sliderPage1.setImageDrawable(R.drawable.nha_nghi_1);
                sliderPage1.setBgColor(Color.CYAN);
                addSlide(AppIntroFragment.newInstance(sliderPage1));

                SliderPage sliderPage2 = new SliderPage();
                sliderPage2.setTitle("Giới thiệu");
                sliderPage2.setDescription("Chúng tôi đến đây để mang lại cho các bạn 1 giải pháp về tìm kiếm nhà nghỉ, nhà trọ cho thuê với giá cả hợp lý nhất");
                sliderPage2.setImageDrawable(R.drawable.nha_nghi_1);
                sliderPage2.setBgColor(Color.CYAN);
                addSlide(AppIntroFragment.newInstance(sliderPage2));

                SliderPage sliderPage3 = new SliderPage();
                sliderPage3.setTitle("Giới thiệu");
                sliderPage3.setDescription("Chúng tôi gọi nó là Vnpt Line");
                sliderPage3.setImageDrawable(R.drawable.nha_nghi_1);
                sliderPage3.setBgColor(Color.CYAN);
                addSlide(AppIntroFragment.newInstance(sliderPage3));

                SliderPage sliderPage4 = new SliderPage();
                sliderPage4.setTitle("Hành trình");
                sliderPage4.setDescription("Nào, hãy cùng bắt đầu và trải nghiệm với chúng tôi!");
                sliderPage4.setImageDrawable(R.drawable.nha_nghi_1);
                sliderPage4.setBgColor(Color.CYAN);
                addSlide(AppIntroFragment.newInstance(sliderPage4));
                tinydb.putBoolean(SharePrefDefine.IS_INTRODUCE, false);
            }else {
                Intent intent = new Intent(this, StartActivity.class);
                startActivity(intent);
            }

        }catch (Exception ex){

        }
        
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }

}
