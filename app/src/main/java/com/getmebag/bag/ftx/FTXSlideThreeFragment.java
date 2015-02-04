package com.getmebag.bag.ftx;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.getmebag.bag.R;
import com.getmebag.bag.base.FBBaseFragment;

import java.util.Arrays;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FTXSlideThreeFragment extends FBBaseFragment {

    @InjectView(R.id.fb_login_button)
    LoginButton loginBtn;
    @InjectView(R.id.username)
    TextView username;

    @Inject
    public FTXSlideThreeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView;
        rootView = inflater.inflate(R.layout.fragment_ftxslide_three, container, false);

        ButterKnife.inject(this, rootView);

        loginBtn.setFragment(this);
        loginBtn.setReadPermissions(Arrays.asList("email"));
        loginBtn.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
                if (user != null) {
                    username.setText("You are currently logged in as " + user.getName());
                } else {
                    username.setText("You are not logged in.");
                }
            }
        });

        return rootView;
    }

}
