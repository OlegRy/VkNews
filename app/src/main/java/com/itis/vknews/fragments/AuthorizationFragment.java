package com.itis.vknews.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.itis.vknews.R;
import com.itis.vknews.utils.Constants;
import com.vk.sdk.VKSdk;

public class AuthorizationFragment extends Fragment {

    private Button btn_authorize;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_authorization, container, false);

        btn_authorize = (Button) view.findViewById(R.id.btn_authorize);
        btn_authorize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VKSdk.authorize(Constants.SCOPES);
            }
        });
        return view;
    }
}
