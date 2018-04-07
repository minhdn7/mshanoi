package com.mkit.mshanoi.app.di;

import android.content.Context;

import com.mkit.mshanoi.app.BaseActivity;
import com.mkit.mshanoi.app.BaseFragment;
import com.mkit.mshanoi.app.LineApplication;
import com.mkit.mshanoi.ui.activity.DetailActivity;
import com.mkit.mshanoi.ui.activity.HomeActivity;
import com.mkit.mshanoi.ui.activity.IntroActivity;
import com.mkit.mshanoi.ui.activity.MainActivity;
import com.mkit.mshanoi.ui.activity.MapDetailActivity;
import com.mkit.mshanoi.ui.activity.PolicyActivity;
import com.mkit.mshanoi.ui.activity.ShareActivity;
import com.mkit.mshanoi.ui.activity.StartActivity;
import com.mkit.mshanoi.ui.fragment.DanhSachMsFragment;
import com.mkit.mshanoi.ui.fragment.ForumsFragment;
import com.mkit.mshanoi.ui.fragment.HomeFragment;
import com.mkit.mshanoi.ui.fragment.MapFragment;
import com.mkit.mshanoi.ui.fragment.MenuFragment;

import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
/**
 * Created by LiKaLi on 1/22/2018.
 */
@Module(
    includes = {
//        LoginModule.class,
        UserModule.class,
        NetModule.class },
    injects = {
        //App
        LineApplication.class,

        // - view
        BaseActivity.class,
        BaseFragment.class,


        //Activity
        HomeActivity.class,
        IntroActivity.class,
        DetailActivity.class,
        MainActivity.class,
        MapDetailActivity.class,
        StartActivity.class,
        PolicyActivity.class,
        ShareActivity.class,
//        DetailActivity.class,
//        ForgotPasswordActivity.class,
//        HomeActivity.class,
//        IntroActivity.class,
//        LoginActivity.class,
//        MainActivity.class,
//        MapActivity.class,
//        MainActivity.class,
//        OTPActivity.class,
//        RegisterActivity.class,
//        TimNhaNghiActivity.class,
//        UuDaiActivity.class,
//        BinhLuanActivity.class,
//        SupportActivity.class,
//        DatDoAnActivity.class,
//        LichSuActivity.class,
//        DanhSachPhongDatActivity.class,
//        SubmitRegisterActivity.class,
//        DetailHistoryActivity.class,
//        ChangePasswordActivity.class,
//        ChiaSeActivity.class,
//        //Fragment
          DanhSachMsFragment.class,
          MapFragment.class,
          HomeFragment.class,
          MenuFragment.class,
          ForumsFragment.class
//        ThongBaoFragment.class
                }, library = true)
public class AppModule {

  private Context context;

  public AppModule(Context context) {
    this.context = context;
  }

  @Provides @Singleton public Context provideApplicationContext() {
    return this.context;
  }
}
