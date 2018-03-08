package com.mkit.mshanoi.app;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.Settings.Secure;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.mkit.mshanoi.R;
import com.mkit.mshanoi.domain.repository.TinyDB;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;



import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;

/**
 * Created by VietNH on 4/17/2017.
 */

public class BaseFragment extends Fragment implements Validator.ValidationListener{
    protected boolean isPassedValidate;
    protected Validator validator;

    public String deviceId;
    private KProgressHUD hud;
    public TinyDB tinyDB;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        appPrefs = LineApplication.getApp().getAppPrefs();
        injectDependencies();
        tinyDB = new TinyDB(getContext());
        validator = new Validator(this);
        validator.setValidationListener(this);
        deviceId = Secure.getString(getActivity().getContentResolver(), Secure.ANDROID_ID);
        hud = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDetailsLabel("Đang kết nối...")
                .setCancellable(true)
                .setAnimationSpeed(3)
                .setDimAmount(0.5f);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected void injectDependencies() {
//        ((LineApplication) getActivity().getApplication()).inject(this);
    }
    public boolean isConnectedNetwork() {
        ConnectivityManager conMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }



    @Override
    public void onValidationSucceeded() {
        isPassedValidate = true;
        Log.e("Valid: ", "Valid Success");
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        isPassedValidate = false;
        Log.e("Valid: ", "" + isPassedValidate);
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getActivity());
            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
                ((EditText) view).setFocusable(true);
            } else {
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager)  getActivity().getSystemService(getActivity().getApplication().INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }

    public Boolean isSpecialCharAvailable(String s) {
        //int counter =0;
        if (s == null || s.trim().isEmpty()) {
            return false;
        }
        Pattern p = Pattern.compile("[^A-Za-z0-9]");//replace this with your needs
        Matcher m = p.matcher(s);
        // boolean b = m.matches();

        boolean b = m.find();
        if (b == true)
            return true;
        else
            return false;
    }

    public Boolean kiemTraKyTuSo(String s) {
        //int counter =0;
        if (s == null || s.trim().isEmpty()) {
            return false;
        }
        Pattern p = Pattern.compile("[0-9]");//replace this with your needs
        Matcher m = p.matcher(s);
        // boolean b = m.matches();

        boolean b = m.find();
        if (b == true)
            return true;
        else
            return false;
    }

    public Boolean kiemTraKyTuChu(String s) {
        //int counter =0;
        if (s == null || s.trim().isEmpty()) {
            return false;
        }
        Pattern p = Pattern.compile("[A-Za-z]");//replace this with your needs
        Matcher m = p.matcher(s);
        // boolean b = m.matches();

        boolean b = m.find();
        if (b == true)
            return true;
        else
            return false;
    }

    public void showProgressBar() {
        try{
            if (hud != null && !hud.isShowing())
                hud.show();
//            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }catch (Exception ex){

        }

    }

    public void hideProgressBar() {
        try{
            if (hud != null && hud.isShowing())
                hud.dismiss();
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }catch (Exception ex){

        }

    }

    public void dilogThongBao(String title, String noiDung, String sButtton){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_thong_bao, null);
        dialogBuilder.setView(dialogView);

        final AlertDialog b = dialogBuilder.create();
        b.setCanceledOnTouchOutside(false);
        b.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        b.show();
        // set width
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(b.getWindow().getAttributes());
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        width = (int) (width * 0.8);
        lp.width = width;
        lp.gravity = Gravity.CENTER;
        b.getWindow().setAttributes(lp);
        //
        TextView txtHeaderDialog = (TextView) dialogView.findViewById(R.id.txtHeaderDialog);
        TextView txtNoiDung = (TextView) dialogView.findViewById(R.id.txtNoiDung);
        ImageView imgExit = (ImageView) dialogView.findViewById(R.id.imgExit);
        Button btnTiepTuc = (Button) dialogView.findViewById(R.id.btnTiepTuc);

        txtHeaderDialog.setText(title);
        txtNoiDung.setText(noiDung);
        btnTiepTuc.setText(sButtton);
        imgExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });

        btnTiepTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });

    }
}