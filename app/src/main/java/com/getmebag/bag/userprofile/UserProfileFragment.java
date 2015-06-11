package com.getmebag.bag.userprofile;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.IconTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;
import com.getmebag.bag.R;
import com.getmebag.bag.androidspecific.prefs.CustomObjectPreference;
import com.getmebag.bag.annotations.CurrentUser;
import com.getmebag.bag.annotations.CurrentUserPreference;
import com.getmebag.bag.base.BagAuthBaseFragment;
import com.getmebag.bag.dialog.DialogActionsListener;
import com.getmebag.bag.ftx.FTXLocationActivity;
import com.getmebag.bag.model.BagUser;
import com.getmebag.bag.model.CachedUserData;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.text.TextUtils.isEmpty;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;
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

    @InjectView(R.id.profile_picture_alt)
    IconTextView profilePictureAlt;

    @InjectView(R.id.profile_email)
    TextView profileEmail;

    @InjectView(R.id.profile_next)
    Button nextButton;

    @Inject
    @CurrentUser
    BagUser currentUser;

    @Inject
    @CurrentUserPreference
    CustomObjectPreference<BagUser> currentUserPreference;

    private ProfileListAdapter adapter;

    @Inject
    public UserProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_profile, container, false);
        ButterKnife.inject(this, rootView);

        //Initially, next button should be disabled.
        nextButton.setEnabled(false);

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
        profilePicture.setVisibility(GONE);
        profilePictureAlt.setVisibility(VISIBLE);

        Picasso.with(getActivity())
                .load(currentUser.getCachedUserData().getProfilePictureURL())
                .placeholder(R.drawable.circular_textview)
                .into(profilePicture, new Callback() {
                    @Override
                    public void onSuccess() {
                        profilePictureAlt.setVisibility(GONE);
                        profilePicture.setVisibility(VISIBLE);
                    }

                    @Override
                    public void onError() {
                        profilePicture.setVisibility(GONE);
                        profilePictureAlt.setVisibility(VISIBLE);
                    }
                });
    }

    private void setupProfileListView() {
        ArrayList<ProfileItem> profileItems = new ArrayList<>();

        addUserNameRow(profileItems, 0);
        addPhoneNumberRow(profileItems, 1);
        addBirthdayRow(profileItems, 2);

        adapter = new ProfileListAdapter(getActivity(),
                R.layout.list_item_profile, profileItems,
                getActivity().getSupportFragmentManager());
        profileListView.setAdapter(adapter);
    }

    private void addBirthdayRow(ArrayList<ProfileItem> profileItems, int position) {
        profileItems.add(position, ProfileItem.builder()
                .setItemIndicationIcon(getString(R.string.icon_font_birthday))
                .setItemDescription(getBirthDate())
                .setItemDescriptionColor(getBirthDateDescriptionColor())
                .setItemActionIcon(getString(R.string.icon_font_calendar))
                .setItemActionType(BIRTHDAY)
                .setItemActionIconSize(getBirthDateActionIconSize())
                .setItemCTAIcon(getString(R.string.icon_font_more_info))
                .setDialogActionsListener(new DialogActionsListener() {
                    @Override
                    public void setOnDialogNeutralButtonListener(String date, Dialog dialog) {
                        ProfileItem dateItem = adapter.getItem(2);
                        ProfileItem.Builder dateItemBuilder = dateItem.toBuilder();

                        if (!isEmpty(date)) {
                            dateItemBuilder
                                    .setItemDescription(date)
                                    .setItemDescriptionColor(getResources()
                                            .getColor(R.color.default_theme_dark_text_color));
                        } else {
                            dateItemBuilder
                                    .setItemDescription(getString(R.string.select_your_birthday))
                                    .setItemDescriptionColor(getResources().getColor(R.color.gray01));
                        }

                        adapter.remove(dateItem);
                        adapter.insert(dateItemBuilder.build(),
                                (dateItem.getItemActionType() - 1));
                        adapter.notifyDataSetChanged();

                        makeText(getActivity(), date, LENGTH_LONG).show();
                        dialog.dismiss();
                    }

                    @Override
                    public void setOnDialogPositiveButtonListener(String arg) {

                    }

                    @Override
                    public void setOnDialogNegativeButtonListener(Dialog dialog) {

                    }
                })
                .setItemCTADialogMessage(getString(R.string.more_info_cta_birthday))
                .setItemCTADialogTitle(getString(R.string.more_info_cta_birthday_title))
                .setItemDescriptionHeader(getString(R.string.ftx_optional))
                .build());
    }

    private void addPhoneNumberRow(ArrayList<ProfileItem> profileItems, int position) {
        profileItems.add(position, ProfileItem.builder()
                .setItemIndicationIcon(getString(R.string.icon_font_phone))
                .setItemDescription(getMyPhoneNumber())
                .setItemActionIcon(getString(R.string.icon_font_edit))
                .setItemActionType(PHONE_NUMBER)
                .setItemCTAIcon(getString(R.string.icon_font_more_info))
                .setDialogActionsListener(new DialogActionsListener() {
                    @Override
                    public void setOnDialogNeutralButtonListener(String phoneNumber, Dialog dialog) {
                        ProfileItem phoneNumberItem = adapter.getItem(1);
                        ProfileItem.Builder phoneNumberItemBuilder = phoneNumberItem.toBuilder();

                        if (!isEmpty(phoneNumber)) {
                            phoneNumberItemBuilder
                                    .setItemDescription(phoneNumber)
                                    .setItemDescriptionColor(getResources()
                                            .getColor(R.color.default_theme_dark_text_color));
                        } else {
                            phoneNumberItemBuilder
                                    .setItemDescription(getString(R.string.enter_your_phone_number))
                                    .setItemDescriptionColor(getResources().getColor(R.color.gray01));
                        }

                        adapter.remove(phoneNumberItem);
                        adapter.insert(phoneNumberItemBuilder.build(),
                                (phoneNumberItem.getItemActionType() - 1));
                        adapter.notifyDataSetChanged();

                        makeText(getActivity(), phoneNumber, LENGTH_LONG).show();
                        dialog.dismiss();
                    }

                    @Override
                    public void setOnDialogPositiveButtonListener(String arg) {

                    }

                    @Override
                    public void setOnDialogNegativeButtonListener(Dialog dialog) {

                    }
                })
                .setItemCTADialogMessage(getString(R.string.more_info_cta_phone_number))
                .setItemCTADialogTitle(getString(R.string.more_info_cta_phone_number_title))
                .setItemDescriptionHeader(getString(R.string.ftx_optional))
                .build());
    }

    private void addUserNameRow(ArrayList<ProfileItem> profileItems, int position) {
        ProfileItem.Builder userNameBuilder = ProfileItem.builder();
        if (isThisLoggedInFTX.get()) {
            userNameBuilder
                    .setItemIndicationIcon(getString(R.string.icon_font_user))
                    .setItemDescription(getBagUserName())
                    .setCachedUserData(currentUser.getCachedUserData())
                    .setItemActionIcon(getString(R.string.icon_font_edit))
                    .setItemDescriptionColor(getResources()
                            .getColor(R.color.gray01))
                    .setItemActionType(USERNAME)
                    .setDialogActionsListener(new DialogActionsListener() {

                        @Override
                        public void setOnDialogNeutralButtonListener(String username, Dialog dialog) {
                            if (!isEmpty(username)) {
                                ProfileItem userNameItem = adapter.getItem(0);

                                ProfileItem newUserNameItem = userNameItem.toBuilder()
                                        .setItemDescription(username)
                                        .setItemDescriptionColor(getResources()
                                                .getColor(R.color.default_theme_dark_text_color))
                                        .setItemDescriptionHeader(getString(R.string.ftx_username_looks_great,
                                                username))
                                        .setItemDescriptionHeaderColor(getResources()
                                                .getColor(R.color.default_theme_warning_solved)).build();

                                adapter.remove(userNameItem);
                                adapter.insert(newUserNameItem, (userNameItem.getItemActionType() - 1));
                                adapter.notifyDataSetChanged();

                                nextButton.setEnabled(!isEmpty(username));
                            }
                            makeText(getActivity(), username, LENGTH_LONG).show();
                            dialog.dismiss();
                        }

                        @Override
                        public void setOnDialogPositiveButtonListener(String arg) {

                        }

                        @Override
                        public void setOnDialogNegativeButtonListener(Dialog dialog) {

                        }
                    })
                    .setItemDescriptionHeader(getString(R.string.ftx_username_cannot_be_changed_later))
                    .setItemDescriptionHeaderColor(getResources()
                            .getColor(R.color.default_theme_warning_messages));
        } else {
            userNameBuilder
                    .setItemIndicationIcon(getString(R.string.icon_font_user))
                    .setItemDescription(getBagUserName());
        }

        profileItems.add(position, userNameBuilder.build());
    }

    private String getBagUserName() {
        if (currentUser.getBagUserName() != null) {
            return currentUser.getBagUserName();
        }
        return getString(R.string.choose_a_username);
    }

    private String getBirthDate() {
        if (!isEmpty(currentUser.getCachedUserData().getBirthDate())) {
            return currentUser.getCachedUserData().getBirthDate();
        } else {
            return getString(R.string.select_your_birthday);
        }
    }

    private int getBirthDateDescriptionColor() {
        if (isEmpty(currentUser.getCachedUserData().getBirthDate())) {
            return getResources().getColor(R.color.gray01);
        } else {
            return 0;
        }
    }

    private int getBirthDateActionIconSize() {
        if (isEmpty(currentUser.getCachedUserData().getBirthDate())) {
            return 24;
        } else {
            return 20;
        }
    }

    private String getMyPhoneNumber() {
        TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService
                (Context.TELEPHONY_SERVICE);
        return telephonyManager.getLine1Number() != null
                ? telephonyManager.getLine1Number()
                : getString(R.string.enter_your_phone_number);
    }

    private void checkIfBagUserAliasExistsInFireBaseSaveIfNot(final String alias) {
        Firebase aliasFirebaseRef = new Firebase(getString(R.string.firebase_bag_user_alias_url) +
                alias);

        aliasFirebaseRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData currentData) {
                if (currentData.getValue() == null) {
                    currentData.setValue(currentUser.getUniqueId());
                    return Transaction.success(currentData);
                } else {
                    Toast.makeText(getActivity(),
                            "Username already taken.Please choose a different one.",
                            LENGTH_LONG).show();
                    return Transaction.abort();
                }
            }

            @Override
            public void onComplete(FirebaseError firebaseError, boolean committed, DataSnapshot dataSnapshot) {
                if (firebaseError == null) {
                    //Save updated data in local and launch next screen.
                    updateLocalUserObject();
                    launchNextScreen();
                } else {
                    nextButton.setEnabled(false);
                    Toast.makeText(getActivity(),
                            "Some Error! Please try again.",
                            LENGTH_LONG).show();
                }
            }
        }, false);
    }

    private void updateLocalUserObject() {
        CachedUserData cachedUserData = new CachedUserData.Builder(currentUser.getCachedUserData())
                .setBirthDate(getCurrentSetBirthday()).build();
        BagUser bagUser = new BagUser.Builder(currentUser)
                .setCachedUserData(cachedUserData)
                .setPhoneNumber(getCurrentSetPhoneNumber())
                .setBagUserName(getCurrentSetUserName())
                .build();
        currentUserPreference.set(bagUser);
    }

    private String getCurrentSetUserName() {
        String currentUserName = adapter
                .getItem(ProfileListAdapter.USERNAME - 1).getItemDescription();
        if (currentUserName != null &&
                !currentUserName.equals(getString(R.string.choose_a_username))) {
            return currentUserName;
        }
        return null;
    }

    private String getCurrentSetPhoneNumber() {
        String currentPhoneNumber = adapter
                .getItem(ProfileListAdapter.PHONE_NUMBER - 1).getItemDescription();
        if (currentPhoneNumber != null &&
                !currentPhoneNumber.equals(getString(R.string.enter_your_phone_number))) {
            return currentPhoneNumber;
        }
        return null;
    }

    private String getCurrentSetBirthday() {
        String currentBirthday = adapter
                .getItem(ProfileListAdapter.BIRTHDAY - 1).getItemDescription();
        if (currentBirthday != null &&
                !currentBirthday.equals(getString(R.string.select_your_birthday))) {
            return currentBirthday;
        }
        return null;
    }

    @OnClick(R.id.profile_next)
    public void profileScreenNextButton(View view) {
        if (view.isEnabled()) {
            String currentUserName = adapter.getItem(USERNAME - 1).getItemDescription();
            if (currentUserName != null &&
                    !currentUserName.equals(getString(R.string.choose_a_username))) {
                checkIfBagUserAliasExistsInFireBaseSaveIfNot(currentUserName);

                firebaseUsersRef.child(currentUser.getUniqueId())
                        .child(getString(R.string.firebase_user_phone_number_url_part))
                        .setValue(getCurrentSetPhoneNumber());

                firebaseUsersRef.child(currentUser.getUniqueId())
                        .child(getString(R.string.firebase_user_bag_username_url_part))
                        .setValue(getCurrentSetUserName());

                firebaseUsersRef.child(currentUser.getUniqueId())
                        .child(getString(R.string.firebase_cached_user_data_url_part))
                        .child(getString(R.string.firebase_user_birthday_url_part))
                        .setValue(getCurrentSetBirthday());

            } else {
                makeText(getActivity(), "Please choose a valid username", LENGTH_LONG).show();
            }
        }
    }

    private void launchNextScreen() {
        startActivity(FTXLocationActivity.intent(getActivity()));
        getActivity().overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);
    }

}
