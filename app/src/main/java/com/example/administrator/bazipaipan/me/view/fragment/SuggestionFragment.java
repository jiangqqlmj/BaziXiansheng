package com.example.administrator.bazipaipan.me.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.administrator.bazipaipan.R;
import com.example.administrator.bazipaipan.me.MeContainerActivity;
import com.example.administrator.bazipaipan.utils.BmobUtils;

/**
 * Created by 王中阳 on 2015/12/16.
 */
public class SuggestionFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = "SuggestionFragment";
    private MeContainerActivity mycontext;
    private Button button;
    private EditText et_suggestion, et_contactway;

    public SuggestionFragment() {
    }

    public static SuggestionFragment newInstance() {
        SuggestionFragment fragment = new SuggestionFragment();
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mycontext = (MeContainerActivity) context;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initviews();
        initDatas();

    }

    private void initDatas() {
    }


    private void initviews() {
        et_suggestion = (EditText) mycontext.findViewById(R.id.et_suggestion);
        et_contactway = (EditText) mycontext.findViewById(R.id.et_contactway);
        button = (Button) mycontext.findViewById(R.id.btn_suggestion_commit);
        button.setOnClickListener(this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_suggestion, null);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

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

    //点击事件
    @Override
    public void onClick(View v) {
        String suggestion = et_suggestion.getText().toString().trim();
        String contactway = et_contactway.getText().toString().trim();
        switch (v.getId()) {
            case R.id.btn_suggestion_commit:
                if (TextUtils.isEmpty(suggestion)) {
                    mycontext.toast("请输入反馈意见");
                    return;
                }
                if (TextUtils.isEmpty(contactway)) {
                    mycontext.toast("请输入联系方式");
                    return;
                }
                BmobUtils.log(suggestion + contactway);
                mycontext.toast("反馈成功，感想您的宝贵建议");
                mycontext.finish();
                break;
        }
    }
}
