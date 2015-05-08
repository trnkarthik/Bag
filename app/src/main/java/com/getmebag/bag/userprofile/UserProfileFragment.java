package com.getmebag.bag.userprofile;

import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.getmebag.bag.R;
import com.getmebag.bag.androidspecific.prefs.BooleanPreference;
import com.getmebag.bag.annotations.CurrentUser;
import com.getmebag.bag.annotations.IsThisLoggedInFirstTimeUse;
import com.getmebag.bag.base.BagAuthBaseFragment;
import com.getmebag.bag.ftx.FTXLocationActivity;
import com.getmebag.bag.model.BagUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.getmebag.bag.userprofile.ProfileListAdapter.BIRTHDAY;
import static com.getmebag.bag.userprofile.ProfileListAdapter.PHONE_NUMBER;
import static com.getmebag.bag.userprofile.ProfileListAdapter.USERNAME;

/**
 * Created by karthiktangirala on 4/16/15.
 */
public class UserProfileFragment extends BagAuthBaseFragment {

    @InjectView(R.id.profile_listview)
    ListView profileListView;

    @InjectView(R.id.profile_picture)
    CircleImageView profilePicture;

    @InjectView(R.id.profile_email)
    TextView profileEmail;

    @InjectView(R.id.profile_next)
    Button nextButton;

    @Inject
    @IsThisLoggedInFirstTimeUse
    BooleanPreference isThisLoggedInFTX;

    @Inject
    @CurrentUser
    BagUser currentUser;

    @Inject
    public UserProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_profile, container, false);
        ButterKnife.inject(this, rootView);
        setUpProfilePicture();
        setUpProfileEmail();
        setupProfileListView();
        showNextButtonIfRequired();
        return rootView;
    }

    private void showNextButtonIfRequired() {
        if(!isThisLoggedInFTX.get()) {
            nextButton.setVisibility(GONE);
        } else {
            nextButton.setVisibility(VISIBLE);
        }
    }

    private void setUpProfileEmail() {
        profileEmail.setText(currentUser.getCachedUserData().getEmail());
    }

    private void setUpProfilePicture() {
//        TODO : Sort this out
        Picasso.with(getActivity())
                .load(currentUser.getCachedUserData().getProfilePictureURL())
//                .placeholder(R.drawable.)
//                .error(R.drawable.user_placeholder_error)
                .into(profilePicture);
    }

    private void setupProfileListView() {
        ArrayList<ProfileItem> profileItems = new ArrayList<>();

        profileItems.add(new ProfileItem.Builder()
                .setItemIndicationIcon(getString(R.string.icon_font_user))
                .setItemDescription(currentUser.getCachedUserData().getUserName())
                .setItemActionIcon(getString(R.string.icon_font_edit))
                .setItemActionType(USERNAME)
                .setItemDescriptionHeader("Username cannot be changed later.")
                .setItemDescriptionHeaderColor(getResources()
                        .getColor(R.color.default_theme_warning_messages))
                .build());

        profileItems.add(new ProfileItem.Builder()
                .setItemIndicationIcon(getString(R.string.icon_font_phone))
                .setItemDescription(getMyPhoneNumber())
                .setItemActionIcon(getString(R.string.icon_font_edit))
                .setItemActionType(PHONE_NUMBER)
                .setItemCTAIcon(getString(R.string.icon_font_more_info))
                .setItemCTADialogMessage(getString(R.string.more_info_cta_phone_number))
                .setItemDescriptionHeader("(Optional)")
                .build());

        profileItems.add(new ProfileItem.Builder()
                .setItemIndicationIcon(getString(R.string.icon_font_birthday))
                .setItemDescription(getBirthDate())
                .setItemActionIcon(getBirthDateActionIcon())
                .setItemActionType(BIRTHDAY)
                .setItemActionIconSize(24)
                .setItemCTAIcon(getString(R.string.icon_font_more_info))
                .setItemCTADialogMessage(getString(R.string.more_info_cta_birthday))
                .build());

        ProfileListAdapter adapter = new ProfileListAdapter(getActivity(),
                R.layout.list_item_profile, profileItems, getActivity().getSupportFragmentManager());
        profileListView.setAdapter(adapter);
    }

    private String getBirthDate() {
        if (!TextUtils.isEmpty(currentUser.getCachedUserData().getBirthDate())) {
            return currentUser.getCachedUserData().getBirthDate();
        } else {
            return getString(R.string.profile_date_placeholder);
        }
    }

    private String getBirthDateActionIcon() {
        if (TextUtils.isEmpty(currentUser.getCachedUserData().getBirthDate())) {
            return getString(R.string.icon_font_calendar);
        } else {
            return null;
        }
    }

    private String getMyPhoneNumber() {
        TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService
                (Context.TELEPHONY_SERVICE);
        return telephonyManager.getLine1Number();
    }

/*
    private void checkIfUserExistsAndSaveDataInFireBase(String firebasePath, final AuthData authData) {
        Firebase firebase = new Firebase(firebasePath);
        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    //New Guy
                    isThisLoggedInFTX.set(true);
                    saveDataInFireBase(authData, true);
                } else {
                    //Old Dude
                    isThisLoggedInFTX.set(false);
                    saveDataInFireBase(authData, false);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(getActivity(), "Sorry! Result not available at this time!",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
*/

    @OnClick(R.id.profile_next)
    public void profileScreenNextButton(View view) {
        startActivity(FTXLocationActivity.intent(getActivity()));
        getActivity().overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);
    }

}
