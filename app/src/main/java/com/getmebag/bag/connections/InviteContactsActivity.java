package com.getmebag.bag.connections;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.getmebag.bag.R;
import com.getmebag.bag.base.BagAuthBaseActivity;

import javax.inject.Inject;

public class InviteContactsActivity extends BagAuthBaseActivity {

    @Inject
    InviteContactsFragment inviteContactsFragment;

    public static Intent intent(Context context) {
        Intent intent = new Intent(context, InviteContactsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_contacts);
        addFragmentToContentView(savedInstanceState, R.id.container,
                inviteContactsFragment);
        enableActionBarUpIcon(!isThisLoggedInFTX.get());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}
