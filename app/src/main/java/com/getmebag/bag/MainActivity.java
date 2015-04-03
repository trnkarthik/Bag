package com.getmebag.bag;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.getmebag.bag.annotations.CurrentUser;
import com.getmebag.bag.base.BagAuthBaseActivity;
import com.getmebag.bag.base.BagAuthBaseFragment;
import com.getmebag.bag.model.BagUser;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MainActivity extends BagAuthBaseActivity {

    public static Intent intent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP
                | FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MainFragment())
                    .commit();
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class MainFragment extends BagAuthBaseFragment {

        @InjectView(R.id.something)
        TextView textView;

        @Inject
        @CurrentUser
        BagUser currentUser;

        public MainFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            ButterKnife.inject(this, rootView);

            textView.setText(currentUser.getEmail() + "\n" + currentUser.getUserName());

            return rootView;
        }
    }
}
