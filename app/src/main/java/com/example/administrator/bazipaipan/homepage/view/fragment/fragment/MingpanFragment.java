package com.example.administrator.bazipaipan.homepage.view.fragment.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.administrator.bazipaipan.R;
import com.example.administrator.bazipaipan.homepage.HomepageContainerActivity;

/**
 * Created by 王中阳 on 2015/12/16.
 */
public class MingpanFragment extends Fragment {

    public static final String TAG = "MingpanFragment";
    private static final String DEFAULT_URL = "http://bazi.up1024.com";
    //1context
    private HomepageContainerActivity mycontext;
    private WebView mingpan_webview;

    public static MingpanFragment newInstance() {
        MingpanFragment mingpanFragment = new MingpanFragment();
        return mingpanFragment;
    }

    public MingpanFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mycontext = (HomepageContainerActivity) context;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mingpan, container, false);
        initviews(view);
        return view;
    }

    private void initviews(View view) {
        mingpan_webview = (WebView) view.findViewById(R.id.mingpan_webview);
        mingpan_webview.loadUrl(DEFAULT_URL);
        mingpan_webview.getSettings().setJavaScriptEnabled(true);
        mingpan_webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                return true;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
