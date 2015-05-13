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

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.getmebag.bag.R;
import com.getmebag.bag.androidspecific.prefs.BooleanPreference;
import com.getmebag.bag.annotations.CurrentUser;
import com.getmebag.bag.annotations.IsThisLoggedInFirstTimeUse;
import com.getmebag.bag.base.BagAuthBaseFragment;
import com.getmebag.bag.ftx.FTXLocationActivity;
import com.getmebag.bag.model.BagUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    Map<String, Boolean> usernameAvailability = new HashMap<>();

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
        if (!isThisLoggedInFTX.get()) {
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

        addUserNameRow(profileItems, 0);
        addPhoneNumberRow(profileItems, 1);
        addBirthdayRow(profileItems, 2);

        ProfileListAdapter adapter = new ProfileListAdapter(getActivity(),
                R.layout.list_item_profile, profileItems, getActivity().getSupportFragmentManager());
        profileListView.setAdapter(adapter);
    }

    private void addBirthdayRow(ArrayList<ProfileItem> profileItems, int position) {
        profileItems.add(position, new ProfileItem.Builder()
                .setItemIndicationIcon(getString(R.string.icon_font_birthday))
                .setItemDescription(getBirthDate())
                .setItemActionIcon(getBirthDateActionIcon())
                .setItemActionType(BIRTHDAY)
                .setItemActionIconSize(24)
                .setItemCTAIcon(getString(R.string.icon_font_more_info))
                .setItemCTADialogMessage(getString(R.string.more_info_cta_birthday))
                .setItemCTADialogTitle(getString(R.string.more_info_cta_birthday_title))
                .build());
    }

    private void addPhoneNumberRow(ArrayList<ProfileItem> profileItems, int position) {
        profileItems.add(position, new ProfileItem.Builder()
                .setItemIndicationIcon(getString(R.string.icon_font_phone))
                .setItemDescription(getMyPhoneNumber())
                .setItemActionIcon(getString(R.string.icon_font_edit))
                .setItemActionType(PHONE_NUMBER)
                .setItemCTAIcon(getString(R.string.icon_font_more_info))
                .setItemCTADialogMessage(getString(R.string.more_info_cta_phone_number))
                .setItemCTADialogTitle(getString(R.string.more_info_cta_phone_number_title))
                .setItemDescriptionHeader(getString(R.string.ftx_optional))
                .build());
    }

    private void addUserNameRow(ArrayList<ProfileItem> profileItems, int position) {
        String currentUserNameWithOutSpaces = stripSpaces(currentUser.getCachedUserData().getUserName());
        ProfileItem.Builder userNameBuilder = new ProfileItem.Builder();
        if (isThisLoggedInFTX.get()) {
            checkIfBagUserAliasExistsInFireBase(currentUserNameWithOutSpaces);
            userNameBuilder
                    .setItemIndicationIcon(getString(R.string.icon_font_user))
                    .setItemDescription(stripSpaces(currentUser.getCachedUserData().getUserName()))
                    .setItemActionIcon(getString(R.string.icon_font_edit))
                    .setItemActionType(USERNAME)
                    .setItemDescriptionHeader(getString(R.string.ftx_username_cannot_be_changed_later))
                    .setItemDescriptionHeaderColor(getResources()
                            .getColor(R.color.default_theme_warning_messages));
        } else {
            userNameBuilder
                    .setItemIndicationIcon(getString(R.string.icon_font_user))
                    .setItemDescription(stripSpaces(currentUser.getBagUserName()));
        }

        profileItems.add(position, userNameBuilder.build());
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

    private String stripSpaces(String profileData) {
        if (!TextUtils.isEmpty(profileData)) {
            return profileData.replaceAll("\\s+", "");
        }
        return null;
    }

    private void checkIfBagUserAliasExistsInFireBase(final String alias) {
        Firebase firebase = new Firebase(getString(R.string.firebase_bag_user_alias_url) +
                alias);
        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    //Username available
                    usernameAvailability.put(alias, true);
                } else {
                    //Username taken
                    usernameAvailability.put(alias, false);
                    refreshUserNameRow(alias);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                usernameAvailability.put(alias, null);
            }
        });
    }

    private void refreshUserNameRow(String alias) {
        if (usernameAvailability.get(alias) != null && !usernameAvailability.get(alias)) {
            //TODO :
        }
    }

    @OnClick(R.id.profile_next)
    public void profileScreenNextButton(View view) {
        startActivity(FTXLocationActivity.intent(getActivity()));
        getActivity().overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);
    }

}
