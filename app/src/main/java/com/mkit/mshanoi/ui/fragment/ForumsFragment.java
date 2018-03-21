package com.mkit.mshanoi.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mkit.mshanoi.R;
import com.mkit.mshanoi.app.BaseFragment;
import com.mkit.mshanoi.ui.event.ListMsEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ForumsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ForumsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForumsFragment extends BaseFragment {


    private OnFragmentInteractionListener mListener;
    @BindView(R.id.webView)
    WebView webView;

    public ForumsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ForumsFragment newInstance(String param1, String param2) {
        ForumsFragment fragment = new ForumsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        addControls();
        return view;
    }

    private void addControls() {
        try {
            if (!isConnectedNetwork()) {
                webView.loadUrl("file:///android_asset/CoinMarketCap_2.html");
            } else {
//                webView.loadUrl("http://mshanoi.com/category/review/");
                webView.loadUrl("file:///android_asset/ReviewMsHaNoi.html");
            }

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
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
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
}
