package com.getmebag.bag.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.getmebag.bag.R;
import com.getmebag.bag.app.BagApplication;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static android.text.TextUtils.isEmpty;

public class PhoneNumberDialogFragment extends DialogFragment {

    private static final String FIELD_SELECTED_PHONE_NUMBER =
            "PhoneNumberDialogFragment.SelectedPhoneNumber";

    @InjectView(R.id.user_phone_number_dialog_confirm_button)
    public Button userPhoneNumberDialogButton;

    @InjectView(R.id.user_phone_number_dialog_edittext)
    public EditText userPhoneNumberDialogEditText;

    @InjectView(R.id.user_dialog_skip_for_now)
    public TextView userPhoneNumberDialogSkipForNow;

    public DialogActionsListener dialogActionsListener;

    String selectedPhoneNumber;

    @Inject
    public PhoneNumberDialogFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ((BagApplication) getActivity().getApplication()).inject(this);

        final Bundle bundle = getArguments();
        selectedPhoneNumber = bundle.getString(FIELD_SELECTED_PHONE_NUMBER);

        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.user_phone_number_dialog);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        ButterKnife.inject(this, dialog);

        userPhoneNumberDialogEditText.setText(selectedPhoneNumber);
        userPhoneNumberDialogButton.setEnabled(!isEmpty(selectedPhoneNumber));
        setEditTextCursorAtTheEnd();


        userPhoneNumberDialogEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence string, int start, int before, int count) {
                userPhoneNumberDialogButton.setEnabled(!isEmpty(string.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        userPhoneNumberDialogButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogActionsListener.setOnDialogNeutralButtonListener(userPhoneNumberDialogEditText.getText().toString(), getDialog());
            }
        });

        userPhoneNumberDialogSkipForNow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialogActionsListener.setOnDialogNeutralButtonListener("", getDialog());
                showAlertDialog(getActivity(), getActivity()
                        .getString(R.string.user_phone_number_add_later));
            }
        });

        return dialog;
    }

    private void showAlertDialog(final Context context, final String message) {
        CommonAlertDialogFragment commonAlertDialogFragment = new CommonAlertDialogFragment.Builder()
                .setMessage(message)
                .setNeutralButtonText(context.getString(R.string.dialog_ok))
                .setDialogDismissOnNeutralButtonClick(true)
                .setCanceledOnTouchOutside(false)
                .build();

        commonAlertDialogFragment.show(getActivity().getSupportFragmentManager(),
                getString(R.string.dialog_common_alert));
    }

    private void setEditTextCursorAtTheEnd() {
        if (!isEmpty(userPhoneNumberDialogEditText.getText())) {
            userPhoneNumberDialogEditText.setSelection(userPhoneNumberDialogEditText.getText().length());
        }
    }

    @OnClick(R.id.user_dialog_skip_for_now)
    public void onSkipButtonClicked(View view) {

    }

    public static class Builder {

        private String selectedPhoneNumber;

        public Builder setSelectedPhoneNumber(String selectedPhoneNumber) {
            this.selectedPhoneNumber = selectedPhoneNumber;
            return this;
        }

        public PhoneNumberDialogFragment build() {
            PhoneNumberDialogFragment fragment = new PhoneNumberDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putString(FIELD_SELECTED_PHONE_NUMBER, isEmpty(selectedPhoneNumber)
                    ? "" : selectedPhoneNumber);
            fragment.setArguments(bundle);
            return fragment;
        }

    }

}
