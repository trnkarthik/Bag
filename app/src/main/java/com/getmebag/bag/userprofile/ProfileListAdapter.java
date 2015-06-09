package com.getmebag.bag.userprofile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.IconTextView;
import android.widget.TextView;

import com.getmebag.bag.R;
import com.getmebag.bag.dialog.CommonAlertDialogFragment;
import com.getmebag.bag.dialog.DatePickerDialogFragment;
import com.getmebag.bag.dialog.DialogActionsListener;
import com.getmebag.bag.dialog.PhoneNumberDialogFragment;
import com.getmebag.bag.dialog.UserNameDialogFragment;
import com.getmebag.bag.model.CachedUserData;
import com.getmebag.bag.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by karthiktangirala on 4/19/15.
 */
public class ProfileListAdapter extends ArrayAdapter<ProfileItem> {

    private final FragmentManager supportFragmentManager;

    // NOTE : Don't change these values. UserProfileFragment#updateProfileItem() will be effected
    public final static int USERNAME = 1;
    public final static int PHONE_NUMBER = 2;
    public final static int BIRTHDAY = 3;


    public ProfileListAdapter(Context context, int textViewResourceId,
                              ArrayList<ProfileItem> profileItems,
                              FragmentManager supportFragmentManager) {
        super(context, textViewResourceId, profileItems);
        this.supportFragmentManager = supportFragmentManager;
    }

    public ProfileListAdapter(Context context, int resource, List<ProfileItem> items,
                              FragmentManager supportFragmentManager) {
        super(context, resource, items);
        this.supportFragmentManager = supportFragmentManager;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_item_profile, null);
            holder = new ViewHolder();

            holder.indicationIcon = (IconTextView) convertView.findViewById(R.id.profile_list_item_indication_icon);
            holder.description = (TextView) convertView.findViewById(R.id.profile_list_item_description);
            holder.descriptionHeader = (TextView) convertView.findViewById(R.id.profile_list_item_description_header);
            holder.actionIcon = (IconTextView) convertView.findViewById(R.id.profile_list_item_action_icon);
            holder.ctaIcon = (IconTextView) convertView.findViewById(R.id.profile_list_item_cta_icon);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        setUp(holder, position);

        return convertView;
    }

    private void setUp(ViewHolder holder, int position) {
        ProfileItem profileItem = getItem(position);

        holder.indicationIcon.setText(profileItem.getItemIndicationIcon());
        setUpDescription(holder, profileItem);

        if (profileItem.getItemActionType() != 0) {
            setViewOnclick(holder.description, profileItem.getItemActionType(),
                    profileItem.getDialogActionsListener());
        }

        setDescriptionHeader(holder.descriptionHeader, profileItem.getItemDescriptionHeader(),
                profileItem.getItemDescriptionHeaderColor());

        setActionIcon(holder.actionIcon, profileItem.getItemActionIcon(),
                profileItem.getItemActionIconSize(), profileItem.getItemActionType(),
                profileItem.getDialogActionsListener());

        setCTAIcon(holder.ctaIcon, profileItem.getItemCTAIcon(),
                profileItem.getItemCTADialogMessage(), profileItem.getItemCTADialogTitle());

    }

    private void setUpDescription(ViewHolder holder, ProfileItem profileItem) {
        holder.description.setText(profileItem.getItemDescription());
        if (profileItem.getItemDescriptionColor() != 0) {
            holder.description.setTextColor(profileItem.getItemDescriptionColor());
        } else {
            holder.description.setTextColor(getContext().getResources().getColor(R.color.default_theme_dark_text_color));
        }
    }

    private void setCTAIcon(IconTextView ctaIcon, String itemCTAIconValue,
                            final String itemCTADialogMessage, final String itemCTADialogTitle) {
        if (!TextUtils.isEmpty(itemCTAIconValue)) {
            ctaIcon.setVisibility(VISIBLE);
            ctaIcon.setText(itemCTAIconValue);
            ctaIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showAlertDialog(itemCTADialogMessage, itemCTADialogTitle);
                }
            });
        } else {
            ctaIcon.setVisibility(GONE);
        }
    }

    private void showAlertDialog(final String itemCTADialogMessage, final String itemCTADialogTitle) {
        if (itemCTADialogTitle != null) {
            CommonAlertDialogFragment commonAlertDialogFragment = new CommonAlertDialogFragment.Builder()
                    .setMessage(itemCTADialogMessage)
                    .setTitle(itemCTADialogTitle)
                    .setNeutralButtonText(getContext().getString(R.string.dialog_ok))
                    .setDialogDismissOnNeutralButtonClick(true)
                    .setCanceledOnTouchOutside(false)
                    .build();

            commonAlertDialogFragment.show(supportFragmentManager,
                    getContext().getString(R.string.dialog_common_alert));
        } else {
            CommonAlertDialogFragment commonAlertDialogFragment = new CommonAlertDialogFragment.Builder()
                    .setMessage(itemCTADialogMessage)
                    .setNeutralButtonText(getContext().getString(R.string.dialog_ok))
                    .setDialogDismissOnNeutralButtonClick(true)
                    .setCanceledOnTouchOutside(false)
                    .build();

            commonAlertDialogFragment.show(supportFragmentManager,
                    getContext().getString(R.string.dialog_common_alert));
        }
    }

    private void setActionIcon(IconTextView actionIcon, String itemActionIconValue,
                               int itemActionIconSize, int itemActionType,
                               DialogActionsListener dialogActionsListener) {
        if (!TextUtils.isEmpty(itemActionIconValue)) {
            actionIcon.setVisibility(VISIBLE);
            actionIcon.setText(itemActionIconValue);
            actionIcon.setTextSize((itemActionIconSize != 0) ? itemActionIconSize : 16);
            setViewOnclick(actionIcon, itemActionType, dialogActionsListener);
        } else {
            actionIcon.setVisibility(GONE);
        }
    }

    private void setViewOnclick(View view, int itemActionType, final DialogActionsListener listener) {

        switch (itemActionType) {
            case USERNAME:
                final CachedUserData itemCachedData = getItem(0).getCachedUserData();
                final String selectedUserName = getItem(0).getItemDescription();
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserNameDialogFragment.Builder userInputDialogFragmentBuilder =
                                new UserNameDialogFragment.Builder();

                        if (!TextUtils.isEmpty(itemCachedData.getFirstName())) {
                            userInputDialogFragmentBuilder
                                    .setFirstName(itemCachedData.getFirstName());
                        }
                        if (!TextUtils.isEmpty(itemCachedData.getLastName())) {
                            userInputDialogFragmentBuilder
                                    .setLastName(itemCachedData.getLastName());
                        }
                        if (!TextUtils.isEmpty(itemCachedData.getUserName())) {
                            userInputDialogFragmentBuilder
                                    .setGivenName(itemCachedData.getUserName());
                        }
                        if (!TextUtils.isEmpty(selectedUserName) &&
                                !(selectedUserName.equals(getContext()
                                        .getString(R.string.choose_a_username)))) {
                            userInputDialogFragmentBuilder
                                    .setSelectedUserName(selectedUserName);
                        }

                        UserNameDialogFragment userNameDialogFragment =
                                userInputDialogFragmentBuilder.build();

                        userNameDialogFragment.show(supportFragmentManager,
                                getContext().getString(R.string.dialog_user_profile_username_selector));
                        if (listener != null) {
                            userNameDialogFragment.dialogActionsListener = listener;
                        }
                    }
                });
                break;
            case PHONE_NUMBER:
                final String selectedPhoneNumber = getItem(1).getItemDescription();
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PhoneNumberDialogFragment.Builder phoneNumberDialogFragmentBuilder =
                                new PhoneNumberDialogFragment.Builder();

                        if (!TextUtils.isEmpty(selectedPhoneNumber) &&
                                !(selectedPhoneNumber.equals(getContext()
                                        .getString(R.string.enter_your_phone_number)))) {
                            phoneNumberDialogFragmentBuilder
                                    .setSelectedPhoneNumber(selectedPhoneNumber);
                        }

                        PhoneNumberDialogFragment phoneNumberDialogFragment =
                                phoneNumberDialogFragmentBuilder.build();

                        phoneNumberDialogFragment.show(supportFragmentManager,
                                getContext().getString(R.string.dialog_user_profile_phone_number_selector));
                        if (listener != null) {
                            phoneNumberDialogFragment.dialogActionsListener = listener;
                        }
                    }
                });
                break;
            case BIRTHDAY:
                final String birthdayText = getItem(2).getItemDescription();
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!TextUtils.isEmpty(birthdayText) &&
                                !(birthdayText.equals(getContext()
                                        .getString(R.string.select_your_birthday)))) {
                            showDatePickerDialog(birthdayText, listener);
                        } else {
                            String currentDate = DateUtils
                                    .dateToString(new Date(), DateUtils.MM_DD_YYYY);
                            showDatePickerDialog(currentDate, listener);
                        }
                    }
                });
                break;
        }
    }

    private void showDatePickerDialog(String birthdayText, DialogActionsListener listener) {
        DatePickerDialogFragment datePickerDialogFragment = new DatePickerDialogFragment.Builder()
                .setSelectedDate(birthdayText)
                .build();
        datePickerDialogFragment.show(supportFragmentManager,
                getContext().getString(R.string.dialog_user_profile_date_picker));
        if (listener != null) {
            datePickerDialogFragment.dialogActionsListener = listener;
        }
    }

    private void setDescriptionHeader(TextView descriptionHeader, String itemDescriptionHeaderValue,
                                      int itemDescriptionHeaderColor) {
        if (!TextUtils.isEmpty(itemDescriptionHeaderValue)) {
            descriptionHeader.setVisibility(VISIBLE);
            descriptionHeader.setText(itemDescriptionHeaderValue);
            if (itemDescriptionHeaderColor != 0) {
                descriptionHeader.setTextColor(itemDescriptionHeaderColor);
            } else {
                descriptionHeader.setTextColor(descriptionHeader
                        .getResources()
                        .getColor(R.color.default_theme_static_text_color));
            }
        } else {
            descriptionHeader.setVisibility(GONE);
        }
    }

    static class ViewHolder {
        IconTextView indicationIcon;
        TextView description;
        TextView descriptionHeader;
        IconTextView actionIcon;
        IconTextView ctaIcon;
    }

}
