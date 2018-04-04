package com.mkit.mshanoi.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mkit.mshanoi.R;
import com.mkit.mshanoi.app.BaseActivity;
import com.mkit.mshanoi.domain.model.pojo.response.DiaDiemMsResponse;
import com.mkit.mshanoi.ui.event.DiaDiemMsEvent;
import com.squareup.picasso.Picasso;
import com.willy.ratingbar.ScaleRatingBar;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailActivity extends BaseActivity {

    @BindView(R.id.imgAnhDaiDien)
    ImageView imgAnhDaiDien;
    @BindView(R.id.lvDanhSachPhongNoiBat)
    RecyclerView lvDanhSachPhongNoiBat;
    @BindView(R.id.txtTenNhaNghi)
    TextView txtTenNhaNghi;
    @BindView(R.id.btnLoaiHinh)
    Button btnLoaiHinh;
    @BindView(R.id.txtDiaChiNhaNghi)
    TextView txtDiaChiNhaNghi;
    @BindView(R.id.imgGoogleStaticMap)
    ImageView imgGoogleStaticMap;
    @BindView(R.id.btnXemBanDo)
    Button btnXemBanDo;
    @BindView(R.id.simpleRatingBar)
    ScaleRatingBar simpleRatingBar;
    @BindView(R.id.viewBinhLuan)
    LinearLayout viewBinhLuan;

    @BindView(R.id.viewDanhSachPhong)
    LinearLayout viewDanhSachPhong;
    @BindView(R.id.lvDanhSachBinhLuan)
    ListView lvDanhSachBinhLuan;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
//    @BindView(R.id.webMieuTaChiTiet)
//    TouchyWebView webMieuTaChiTiet;

    @BindView(R.id.webMieuTaChiTiet)
    WebView webMieuTaChiTiet;
    @BindView(R.id.txtGiaTic)
    TextView txtGiaTic;
    @BindView(R.id.viewChonDanhSachPhong)
    LinearLayout viewChonDanhSachPhong;

    private DiaDiemMsResponse diaDiemMSDetail;
    // get tọa độ
    private double latitude = 0;
    private double longitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().hide();
        addControls();
        addEvents();

    }


    private void addEvents() {
        btnXemBanDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, MapDetailActivity.class);
                intent.putExtra("LAT_POSITION", diaDiemMSDetail.getLatTitule());
                intent.putExtra("LONG_POSITION", diaDiemMSDetail.getLongTitule());

                startActivity(intent);
            }
        });
        imgGoogleStaticMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, MapDetailActivity.class);
                intent.putExtra("LAT_POSITION", diaDiemMSDetail.getLatTitule());
                intent.putExtra("LONG_POSITION", diaDiemMSDetail.getLongTitule());

                startActivity(intent);
            }
        });

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (scrollView != null) {
                    if (scrollView.getChildAt(0).getTop() >= scrollView.getScrollY()) {
                        getSupportActionBar().hide();
                        //scroll view is at top
                    } else {
                        //scroll view is not at top
                        getSupportActionBar().show();
                    }
                }
            }
        });

    }

    private void addControls() {
        try {
            diaDiemMSDetail = EventBus.getDefault().getStickyEvent(DiaDiemMsEvent.class).getDiaDiemMsResponse();

            setTitle(diaDiemMSDetail.getName());
            txtGiaTic.setText(diaDiemMSDetail.getTic());
            webMieuTaChiTiet.getSettings().setJavaScriptEnabled(true);
            webMieuTaChiTiet.setVerticalScrollBarEnabled(false);
            webMieuTaChiTiet.setHorizontalScrollBarEnabled(false);
            if (!isConnectedNetwork()) {
                webMieuTaChiTiet.loadUrl("file:///android_asset/CoinMarketCap_2.html");
            } else {

                webMieuTaChiTiet.loadUrl(diaDiemMSDetail.getLinkTruyen());
            }

            // load link in webview
            this.webMieuTaChiTiet.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                // remove header in webview
                @Override
                public void onPageFinished(WebView view, String url) {
                    webMieuTaChiTiet.loadUrl("javascript:(function() { " +
                            "var head = document.getElementsByTagName('header')[0];"
                            + "head.parentNode.removeChild(head);" +
                            "})()");
                    webMieuTaChiTiet.loadUrl("javascript:(function() { " +
                            "var registration = document.getElementById('registration');"
                            + "var secondary = document.getElementById('secondary');"
                            + "registration.style.display  = 'none';"
                            + "secondary.style.display  = 'none';" +
                            "})()");

                    hideProgressBar();
                }

                @Override
                public void onPageStarted(
                        WebView view, String url, Bitmap favicon) {
                    showProgressBar();
                }
            });
            // end

            txtTenNhaNghi.setText(diaDiemMSDetail.getName());
            txtDiaChiNhaNghi.setText(diaDiemMSDetail.getAddress());
            simpleRatingBar.setRating(Float.valueOf(diaDiemMSDetail.getRate()));
            loadMapImage(this, diaDiemMSDetail.getLatTitule(), diaDiemMSDetail.getLongTitule());
            setTitle(diaDiemMSDetail.getName());

        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void loadMapImage(Context context, double latitude, double longitude) {
        StringBuffer sb = new StringBuffer();
        sb.append("https://maps.googleapis.com/maps/api/staticmap?");
        sb.append("&markers=color:%3agreen|");
        sb.append(latitude);
        sb.append(",");
        sb.append(longitude);
        sb.append("&zoom=15");
        sb.append("&size=600x300");
//        sb.append("&maptype=hybrid");
        sb.append("&key=AIzaSyCEOzecGURX8jT60oVdONBpqqNWQCTORVs&img.jpg");

        Log.i("LOG", "Picasso loading: " + sb.toString());
        Picasso.with(context)
                .load(sb.toString())
                .into(imgGoogleStaticMap);
    }


    @OnClick(R.id.btnCall)
    public void onViewClicked() {
        if(diaDiemMSDetail != null && diaDiemMSDetail.getPhone() != null && !diaDiemMSDetail.getPhone().trim().equals("")){

        }else {
            showDialog("Cơ sở có số điện thoại chưa được xác minh, Không thể thực hiện cuộc gọi!");
        }
    }
}
