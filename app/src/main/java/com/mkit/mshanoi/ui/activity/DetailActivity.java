package com.mkit.mshanoi.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
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
import com.mkit.mshanoi.ui.widget.TouchyWebView;
import com.squareup.picasso.Picasso;
import com.willy.ratingbar.ScaleRatingBar;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    @BindView(R.id.txtTittleNhaNghi)
    TextView txtTittleNhaNghi;
    @BindView(R.id.icBack)
    ImageView icBack;
    @BindView(R.id.webMieuTaChiTiet)
    TouchyWebView webMieuTaChiTiet;


    private DiaDiemMsResponse diaDiemMSDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().hide();
        addControls();
        addEvents();
        loadMapImage(this, 21.028511, 105.804817);
    }

    private void addEvents() {
    }

    private void addControls() {
        diaDiemMSDetail = EventBus.getDefault().getStickyEvent(DiaDiemMsEvent.class).getDiaDiemMsResponse();
//        webMieuTaChiTiet.loadUrl(diaDiemMSDetail.getLinkTruyen());
        webMieuTaChiTiet.loadUrl("https://drive.google.com/file/d/1-iq1WTuT5YQANqQWtiGYxrMgW69E8RFn/view?usp=sharing");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_home:
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
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
        sb.append("&zoom=12");
        sb.append("&size=600x300");
        sb.append("&key=AIzaSyAoDF0slrRXQUIuaI5GXG8E2xX1_xjAOgc&img.jpg");

        Log.i("LOG", "Picasso loading: " + sb.toString());
        Picasso.with(context)
                .load(sb.toString())
                .into(imgGoogleStaticMap);
    }
}
