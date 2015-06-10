package com.getmebag.bag.userprofile;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.getmebag.bag.R;
import com.getmebag.bag.annotations.CurrentUser;
import com.getmebag.bag.base.BagAuthBaseFragment;
import com.getmebag.bag.dialog.DialogActionsListener;
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

import static android.text.TextUtils.isEmpty;
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
    @CurrentUser
    BagUser currentUser;

    Map<String, Boolean> usernameAvailability = new HashMap<>();

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
//        TODO : Sort this out
        Picasso.with(getActivity())
                .load(currentUser.getCachedUserData().getProfilePictureURL())
                .placeholder(R.drawable.temp)
//                .error(R.drawable.user_placeholder_error)
                .into(profilePicture);
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

                        Toast.makeText(getActivity(), date, Toast.LENGTH_LONG).show();
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

                        Toast.makeText(getActivity(), phoneNumber, Toast.LENGTH_LONG).show();
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
//        String currentUserNameWithOutSpaces = stripSpaces(currentUser.getCachedUserData().getUserName());
        ProfileItem.Builder userNameBuilder = ProfileItem.builder();
        if (isThisLoggedInFTX.get()) {
//            checkIfBagUserAliasExistsInFireBase(currentUserNameWithOutSpaces);
            userNameBuilder
                    .setItemIndicationIcon(getString(R.string.icon_font_user))
                    .setItemDescription(getString(R.string.choose_a_username))
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
                            Toast.makeText(getActivity(), username, Toast.LENGTH_LONG).show();
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
                    .setItemDescription(getString(R.string.choose_a_username));
        }

        profileItems.add(position, userNameBuilder.build());
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
        if (view.isEnabled()) {
            startActivity(FTXLocationActivity.intent(getActivity()));
            getActivity().overridePendingTransition(R.anim.slide_in_right,
                    R.anim.slide_out_left);
        } else {
            Toast.makeText(getActivity(), "Please choose a valid username", Toast.LENGTH_LONG).show();
        }
    }

}
