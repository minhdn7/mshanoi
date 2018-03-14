package com.mkit.mshanoi.app.di;

import android.content.Context;

import com.mkit.mshanoi.app.BaseActivity;
import com.mkit.mshanoi.app.BaseFragment;
import com.mkit.mshanoi.app.LineApplication;
import com.mkit.mshanoi.ui.activity.DetailActivity;
import com.mkit.mshanoi.ui.activity.HomeActivity;
import com.mkit.mshanoi.ui.fragment.DanhSachMsFragment;
import com.mkit.mshanoi.ui.fragment.MapFragment;

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
        DetailActivity.class,
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
//        HomeFragment.class,
//        SettingFragment.class,
//        MenuFragment.class,
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
