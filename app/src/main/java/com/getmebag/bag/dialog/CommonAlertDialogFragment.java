package com.getmebag.bag.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.getmebag.bag.R;
import com.getmebag.bag.app.BagApplication;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static android.text.TextUtils.isEmpty;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class CommonAlertDialogFragment extends DialogFragment {

    private static final String FIELD_MESSAGE = "CommonAlertDialogFragment.Message";
    private static final String FIELD_TITLE = "CommonAlertDialogFragment.Title";
    private static final String FIELD_POSITIVE_BUTTON_TEXT = "CommonAlertDialogFragment.PositiveButton";
    private static final String FIELD_NEGATIVE_BUTTON_TEXT = "CommonAlertDialogFragment.NegativeButton";
    private static final String FIELD_NEUTRAL_BUTTON_TEXT = "CommonAlertDialogFragment.NeutralButton";
    private static final String FIELD_DIALOG_DISMISS_ON_NEUTRAL_BUTTON =
            "CommonAlertDialogFragment.DialogDismissOnNeutralButtonClick";
    private static final String FIELD_DIALOG_CANCEL_ON_TOUCH_OUTSIDE =
            "CommonAlertDialogFragment.DialogCanceledOnTouchOutside";

    @InjectView(R.id.common_dialog_title)
    public TextView commonDialogTitle;

    @InjectView(R.id.common_dialog_message)
    public TextView commonDialogMessage;

    @InjectView(R.id.common_dialog_positive_button)
    public Button commonDialogPositiveButton;

    @InjectView(R.id.common_dialog_negative_button)
    public Button commonDialogNegativeButton;

    public DialogActionsListener dialogActionsListener;

    String message;
    String title;
    String positiveButtonText;
    String negativeButtonText;
    String neutralButtonText;
    Boolean dialogDismissOnNeutralButtonClick;
    Boolean canceledOnTouchOutside;

    @Inject
    public CommonAlertDialogFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ((BagApplication) getActivity().getApplication()).inject(this);

        final Bundle bundle = getArguments();
        title = bundle.getString(FIELD_TITLE);
        message = bundle.getString(FIELD_MESSAGE);
        positiveButtonText = bundle.getString(FIELD_POSITIVE_BUTTON_TEXT);
        negativeButtonText = bundle.getString(FIELD_NEGATIVE_BUTTON_TEXT);
        neutralButtonText = bundle.getString(FIELD_NEUTRAL_BUTTON_TEXT);
        dialogDismissOnNeutralButtonClick = bundle.getBoolean(FIELD_DIALOG_DISMISS_ON_NEUTRAL_BUTTON);
        canceledOnTouchOutside = bundle.getBoolean(FIELD_DIALOG_CANCEL_ON_TOUCH_OUTSIDE);

        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.common_alert_dialog);

        ButterKnife.inject(this, dialog);

        setTextIfNotNull(commonDialogTitle, title);
        setTextIfNotNull(commonDialogMessage, message);

        setButtonIfNotNull(commonDialogPositiveButton, positiveButtonText);
        setButtonIfNotNull(commonDialogNegativeButton, negativeButtonText);
        setButtonIfNotNull(commonDialogPositiveButton, neutralButtonText);

        if (positiveButtonText != null) {
            commonDialogPositiveButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogActionsListener.setOnDialogPositiveButtonListener("");
                }
            });
        }

        if (neutralButtonText != null) {
            commonDialogPositiveButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialogDismissOnNeutralButtonClick) {
                        dismiss();
                    } else {
                        dialogActionsListener.setOnDialogNeutralButtonListener("", getDialog());
                    }
                }
            });
        }

        dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);

        return dialog;
    }

    private void setButtonIfNotNull(Button button, String buttonText) {
        if (buttonText != null) {
            button.setVisibility(VISIBLE);
            button.setText(buttonText);
        } else {
            button.setVisibility(GONE);
        }
    }

    private void setTextIfNotNull(TextView view, String title) {
        if (title != null) {
            view.setVisibility(VISIBLE);
            view.setText(Html.fromHtml(title));
        } else {
            view.setVisibility(GONE);
        }
    }

    public static class Builder {

        private String message;
        private String title;
        private String positiveButtonText;
        private String negativeButtonText;
        private String neutralButtonText;
        private Boolean dialogDismissOnNeutralButtonClick = false;
        private Boolean canceledOnTouchOutside = false;

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setPositiveButtonText(String positiveButtonText) {
            this.positiveButtonText = positiveButtonText;
            return this;
        }

        public Builder setNegativeButtonText(String negativeButtonText) {
            this.negativeButtonText = negativeButtonText;
            return this;
        }

        public Builder setNeutralButtonText(String neutralButtonText) {
            this.neutralButtonText = neutralButtonText;
            return this;
        }

        public Builder setDialogDismissOnNeutralButtonClick(Boolean dialogDismissOnNeutralButtonClick) {
            this.dialogDismissOnNeutralButtonClick = dialogDismissOnNeutralButtonClick;
            return this;
        }

        public Builder setCanceledOnTouchOutside(Boolean canceledOnTouchOutside) {
            this.canceledOnTouchOutside = canceledOnTouchOutside;
            return this;
        }

        public CommonAlertDialogFragment build() {
            CommonAlertDialogFragment fragment = new CommonAlertDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putString(FIELD_MESSAGE, isEmpty(message)
                    ? "" : message);
            bundle.putString(FIELD_TITLE, title);
            bundle.putString(FIELD_POSITIVE_BUTTON_TEXT, positiveButtonText);
            bundle.putString(FIELD_NEGATIVE_BUTTON_TEXT, negativeButtonText);
            bundle.putString(FIELD_NEUTRAL_BUTTON_TEXT, neutralButtonText);
            bundle.putBoolean(FIELD_DIALOG_DISMISS_ON_NEUTRAL_BUTTON, dialogDismissOnNeutralButtonClick);
            bundle.putBoolean(FIELD_DIALOG_CANCEL_ON_TOUCH_OUTSIDE, canceledOnTouchOutside);
            fragment.setArguments(bundle);
            return fragment;
        }

    }

}
