package com.mkit.mshanoi.ui.fragment;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;

import com.mkit.mshanoi.R;
import com.mkit.mshanoi.app.BaseFragment;
import com.mkit.mshanoi.domain.model.pojo.response.DiaDiemMsResponse;
import com.mkit.mshanoi.ui.activity.HomeActivity;
import com.mkit.mshanoi.ui.event.ListMsEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DanhSachMsFragment extends BaseFragment {

    private OnFragmentInteractionListener mListener;
    @BindView(R.id.lvDanhSachMS)
    ListView lvDanhSachMS;
    @BindView(R.id.webView)
    WebView webView;
    private List<DiaDiemMsResponse> danhSachDiaDiem = new ArrayList<>();
    private HomeActivity homeActivity = (HomeActivity) getActivity();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_danh_sach_ms, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        addControls();
        return view;
    }

    private void addControls() {
        try {
            danhSachDiaDiem = EventBus.getDefault().getStickyEvent(ListMsEvent.class).getListMs();
            if (danhSachDiaDiem != null && danhSachDiaDiem.size() > 0) {
//                    DanhSachMsAdapter danhSachMsAdapter = new DanhSachMsAdapter(getActivity(), R.layout.item_danh_sach_ms, danhSachDiaDiem);
//                    lvDanhSachMS.setAdapter(danhSachMsAdapter);
            }
            webView.getSettings().setJavaScriptEnabled(true);
            if (!isConnectedNetwork()) {
                webView.loadUrl("file:///android_asset/CoinMarketCap_2.html");
            } else {
//                webView.loadUrl("http://mshanoi.com/category/list-tong-hop/");
                webView.loadUrl("file:///android_asset/mshanoi_2.html");
            }

            // load link in webview
            this.webView.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                // remove header in webview
                @Override
                public void onPageFinished(WebView view, String url)
                {
                    webView.loadUrl("javascript:(function() { " +
                            "var head = document.getElementsByTagName('header')[0];"
                            + "head.parentNode.removeChild(head);" +
                            "})()");
                    hideProgressBar();
                }

                @Override
                public void onPageStarted(
                        WebView view, String url, Bitmap favicon)
                {
                    showProgressBar();
                }
            });
            // end

        } catch (Exception e) {
            e.printStackTrace();
        }

    }




    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (webView.canGoBack()) {
                    webView.goBack();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
