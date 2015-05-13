package com.getmebag.bag.userprofile;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.IconTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.getmebag.bag.R;
import com.getmebag.bag.dialog.UserInputDialogFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by karthiktangirala on 4/19/15.
 */
public class ProfileListAdapter extends ArrayAdapter<ProfileItem> implements UserInputDialogFragment.onSubmitListener {

    private final FragmentManager supportFragmentManager;

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
        holder.description.setText(profileItem.getItemDescription());

        if (profileItem.getItemActionType() != 0) {
            setViewOnclick(holder.description, profileItem.getItemActionType());
        }

        setDescriptionHeader(holder.descriptionHeader, profileItem.getItemDescriptionHeader(),
                profileItem.getItemDescriptionHeaderColor());

        setActionIcon(holder.actionIcon, profileItem.getItemActionIcon(),
                profileItem.getItemActionIconSize(), profileItem.getItemActionType());

        setCTAIcon(holder.ctaIcon, profileItem.getItemCTAIcon(),
                profileItem.getItemCTADialogMessage(), profileItem.getItemCTADialogTitle());

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
        new AlertDialog.Builder(getContext())
                .setMessage(itemCTADialogMessage)
                .setTitle(itemCTADialogTitle)
                .setNeutralButton(getContext().getString(R.string.dialog_ok), null)
                .show();
    }

    private void setActionIcon(IconTextView actionIcon, String itemActionIconValue,
                               int itemActionIconSize, int itemActionType) {
        if (!TextUtils.isEmpty(itemActionIconValue)) {
            actionIcon.setVisibility(VISIBLE);
            actionIcon.setText(itemActionIconValue);
            actionIcon.setTextSize((itemActionIconSize != 0) ? itemActionIconSize : 16);
            setViewOnclick(actionIcon, itemActionType);
        } else {
            actionIcon.setVisibility(GONE);
        }
    }

    private void setViewOnclick(View actionIcon, int itemActionType) {

        switch (itemActionType) {
            case USERNAME:
            case PHONE_NUMBER:
                actionIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserInputDialogFragment fragment1 = new UserInputDialogFragment();
                        fragment1.show(supportFragmentManager, "Some");
                        fragment1.mListener = ProfileListAdapter.this;
                    }
                });
                break;
            case BIRTHDAY:
                actionIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DialogFragment newFragment = new DatePickerFragment();
                        //TODO : Get ids from resources file
                        newFragment.show(supportFragmentManager, "DatePicker");
                    }
                });
                break;
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

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
        }
    }

    static class ViewHolder {
        IconTextView indicationIcon;
        TextView description;
        TextView descriptionHeader;
        IconTextView actionIcon;
        IconTextView ctaIcon;
    }

    @Override
    public void setOnSubmitListener(String arg) {
        Toast.makeText(getContext(), arg, Toast.LENGTH_LONG).show();
    }

}
