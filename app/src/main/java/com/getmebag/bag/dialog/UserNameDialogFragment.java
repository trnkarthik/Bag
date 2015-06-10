package com.getmebag.bag.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.getmebag.bag.R;
import com.getmebag.bag.app.BagApplication;

import org.lucasr.twowayview.TwoWayView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static android.text.TextUtils.isEmpty;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class UserNameDialogFragment extends BagBaseDialogFragment {

    private static final String FIELD_SELECTED_USERNAME = "UserInputDialogFragment.SelectedUserName";
    private static final String FIELD_FIRSTNAME = "UserInputDialogFragment.FirstName";
    private static final String FIELD_LASTNAME = "UserInputDialogFragment.LastName";
    private static final String FIELD_GIVENNAME = "UserInputDialogFragment.GivenName";

    @InjectView(R.id.user_dialog_confirm_button)
    public Button userDialogButton;

    @InjectView(R.id.user_dialog_edittext)
    public EditText userDialogEditText;

    @InjectView(R.id.user_dialog_rules_table)
    public TableLayout tableLayout;

    public TableRow tableRow1;
    public TextView bullet1;
    public TextView rule1;

    public TableRow tableRow2;
    public TextView bullet2;
    public TextView rule2;

    public DialogActionsListener dialogActionsListener;
    private TwoWayView suggestionsList;

    String selectedUserName;
    String firstName;
    String lastName;
    String givenName;
    private ArrayList<String> items = new ArrayList<>();
    private ArrayAdapter<String> suggestionsAdapter;

    @Inject
    public UserNameDialogFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ((BagApplication) getActivity().getApplication()).inject(this);

        final Bundle bundle = getArguments();
        firstName = stripSpaces(bundle.getString(FIELD_FIRSTNAME));
        lastName = stripSpaces(bundle.getString(FIELD_LASTNAME));
        givenName = stripSpaces(bundle.getString(FIELD_GIVENNAME));
        selectedUserName = stripSpaces(bundle.getString(FIELD_SELECTED_USERNAME));

        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.user_input_dialog);

        ButterKnife.inject(this, dialog);

        tableRow1 = (TableRow) tableLayout.findViewById(R.id.user_dialog_rules_table_row1);
        tableRow2 = (TableRow) tableLayout.findViewById(R.id.user_dialog_rules_table_row2);

        bullet1 = (TextView) tableRow1.findViewById(R.id.user_dialog_rules_table_row1_bullet);
        bullet2 = (TextView) tableRow2.findViewById(R.id.user_dialog_rules_table_row2_bullet);

        rule1 = (TextView) tableRow1.findViewById(R.id.user_dialog_rules1);
        rule2 = (TextView) tableRow2.findViewById(R.id.user_dialog_rules2);

        userDialogEditText.setText(selectedUserName);
        setEditTextCursorAtTheEnd();

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        userDialogEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence string, int start, int before, int count) {
                if (!isEmpty(string.toString())) {

                    suggestionsList.setVisibility(GONE);
                    userDialogButton.setVisibility(VISIBLE);

                    if (!TextUtils.isEmpty(string)) {
                        rule1.setTextColor((!isLowerCaseAlphaNumericOrUnderscore(String.valueOf(string))) ?
                                getResources().getColor(R.color.default_theme_warning_messages)
                                : getResources().getColor(R.color.gray01));
                    }

                    userDialogButton.setEnabled(
                            isLowerCaseAlphaNumericOrUnderscore(String.valueOf(string)) &&
                                    (string.length() > 3) && (string.length() <= 20));

                    //show only when length of username > 20
                    if (string.length() > 20) {
                        userDialogEditText.setError(getString(R.string.username_max_chars_rule));
                    }

                    rule2.setTextColor(((string.length() <= 3) || (string.length() > 20)) ?
                            getResources().getColor(R.color.default_theme_warning_messages)
                            : getResources().getColor(R.color.gray01));

                } else {
                    suggestionsList.setVisibility(VISIBLE);
                    userDialogButton.setVisibility(GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        userDialogButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogActionsListener.setOnDialogNeutralButtonListener(userDialogEditText.getText().toString(), getDialog());
            }
        });

        getSuggestions();

        suggestionsAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.list_item_user_name_suggestions, R.id.list_item_suggestion, items);
        suggestionsList = (TwoWayView) dialog.findViewById(R.id.user_dialog_suggestions_list);
        suggestionsList.setAdapter(suggestionsAdapter);

        suggestionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                userDialogEditText.setText(items.get(position));
                setEditTextCursorAtTheEnd();
                suggestionsList.setVisibility(GONE);
                userDialogButton.setVisibility(VISIBLE);
            }
        });

        return dialog;
    }

    private void getSuggestions() {
        validateAndCheckAvailability(givenName);
        validateAndCheckAvailability(firstName);
        validateAndCheckAvailability(lastName);
        validateAndCheckAvailabilityOfCombinations(firstName, lastName);
        validateAndCheckAvailabilityOfCombinations(String.valueOf(firstName.charAt(0)), lastName);
        validateAndCheckAvailabilityOfCombinations(firstName, String.valueOf(lastName.charAt(0)));
    }

    private void validateAndCheckAvailabilityOfCombinations(String firstName, String lastName) {
        validateAndCheckAvailability(firstName + lastName);
        validateAndCheckAvailability(lastName + firstName);
    }

    private void validateAndCheckAvailability(String givenName) {
        if (!isEmpty(givenName) && (givenName.length() > 3)
                && (givenName.length() <= 20) && isAlphaNumericOrUnderscore(givenName)) {
            checkIfBagUserAliasExistsInFireBase(givenName.toLowerCase());
        }
    }

    public boolean isAlphaNumericOrUnderscore(String s) {
        String pattern = "^[a-zA-Z0-9_]*$";
        if (s.matches(pattern)) {
            return true;
        }
        return false;
    }

    public boolean isLowerCaseAlphaNumericOrUnderscore(String s) {
        String pattern = "^[a-z0-9_]*$";
        if (s.matches(pattern)) {
            return true;
        }
        return false;
    }

    private String stripSpaces(String string) {
        if (!isEmpty(string)) {
            return string.replaceAll("\\s+", "");
        }
        return string;
    }

    private void setEditTextCursorAtTheEnd() {
        if (!isEmpty(userDialogEditText.getText())) {
            userDialogEditText.setSelection(userDialogEditText.getText().length());
        }
    }

    private void checkIfBagUserAliasExistsInFireBase(final String alias) {
        Firebase firebase = new Firebase(getString(R.string.firebase_bag_user_alias_url) +
                alias);
        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    //Username available
                    items.add(alias);
                    suggestionsAdapter.notifyDataSetChanged();
                } else {
                    //Username taken.So do nothing
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                //If not sure, just add it to list.
                // We will re-verify it before creating alias on next anyway
                items.add(alias);
            }
        });
    }

    public static class Builder {

        private String selectedUserName;
        private String firstName;
        private String lastName;
        private String givenName;

        public Builder setSelectedUserName(String selectedUserName) {
            this.selectedUserName = selectedUserName;
            return this;
        }

        public Builder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder setGivenName(String givenName) {
            this.givenName = givenName;
            return this;
        }

        public UserNameDialogFragment build() {
            UserNameDialogFragment fragment = new UserNameDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putString(FIELD_SELECTED_USERNAME, isEmpty(selectedUserName)
                    ? "" : selectedUserName);
            bundle.putString(FIELD_FIRSTNAME, firstName);
            bundle.putString(FIELD_LASTNAME, lastName);
            bundle.putString(FIELD_GIVENNAME, givenName);
            fragment.setArguments(bundle);
            return fragment;
        }

    }

}
